package com.edu.game.jct.fight.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.helpers.FormattingTuple;
import org.slf4j.helpers.MessageFormatter;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.stereotype.Service;

import com.edu.game.FixTimeDelayQueue;
import com.edu.game.MapService;
import com.edu.game.Player;
import com.edu.game.Request;
import com.edu.game.SessionManager;
import com.edu.game.SystemConfig;
import com.edu.game.jct.fight.exception.FightException;
import com.edu.game.jct.fight.exception.FightExceptionCode;
import com.edu.game.jct.fight.model.BattleInfo;
import com.edu.game.jct.fight.model.report.RoundReport;
import com.edu.game.jct.fight.service.config.BattleType;
import com.edu.game.jct.fight.service.config.FighterType;
import com.edu.game.jct.fight.service.convertor.FighterConvertor;
import com.edu.game.jct.fight.service.core.Fighter;
import com.edu.game.lock.ChainLock;
import com.edu.game.lock.LockUtils;
import com.edu.utils.DelayedElement;

/**
 * 战斗服务<br/>
 * 战斗锁次序:synchronized(Battle) -> ChainLock(Player...) -> synchronized(LockObject)
 * @author frank
 */
@Service
public class BattleServiceImpl implements BattleService, ApplicationContextAware, ApplicationListener<ContextClosedEvent> {

	private static final Logger logger = LoggerFactory.getLogger(BattleServiceImpl.class);

	@Autowired
	private SessionManager sessionManager;
	@Autowired
	private SystemConfig systemConfig;
	@Autowired
	private MapService mapService;

	/** 不同类型的战斗配置器 */
	private ConcurrentHashMap<BattleType, BattleConfig> configs = new ConcurrentHashMap<BattleType, BattleConfig>(BattleType.values().length);
	/** 不同的战斗单位转换器 */
	@SuppressWarnings("rawtypes")
	private ConcurrentHashMap<FighterType, FighterConvertor> convertors = new ConcurrentHashMap<FighterType, FighterConvertor>(FighterType.values().length);

	/** 战斗对象的标识生成器 */
	private AtomicInteger idBuilder = new AtomicInteger();
	/** 当前的存在的全部战斗对象 */
	private ConcurrentHashMap<Integer, Battle> battles = new ConcurrentHashMap<Integer, Battle>();
	/** 玩家与战斗对象之间的对应关系(一对一) */
	private ConcurrentHashMap<Player, Integer> playerBattle = new ConcurrentHashMap<Player, Integer>();

	/** 战斗超时队列 */
	private FixTimeDelayQueue<DelayedElement<OvertimeWork>> overtimeQueue;
	/** 该服务对象是否在工作中 */
	private boolean running;
	/** 该服务对象状态变更的读写锁 */
	private ReentrantReadWriteLock rwLock = new ReentrantReadWriteLock();

	/** 初始化方法 */
	@SuppressWarnings("rawtypes")
	@PostConstruct
	public void initialize() {
		overtimeQueue = new FixTimeDelayQueue<DelayedElement<OvertimeWork>>(systemConfig.getDelayTime());
		// 初始化战斗配置器信息
		Map<String, BattleConfig> configBeans = this.applicationContext.getBeansOfType(BattleConfig.class);
		for (BattleConfig config : configBeans.values()) {
			if (configs.put(config.getType(), config) != null) {
				FormattingTuple message = MessageFormatter.format("战斗类型[{}]的配置对象重复", config.getType());
				logger.error(message.getMessage());
				throw new FightException(FightExceptionCode.CONFIG_ERROR, message.getMessage());
			}
		}
		// 初始化战斗单位转换器信息
		Map<String, FighterConvertor> convertorBeans = this.applicationContext.getBeansOfType(FighterConvertor.class);
		for (FighterConvertor convertor : convertorBeans.values()) {
			if (convertors.put(convertor.getType(), convertor) != null) {
				FormattingTuple message = MessageFormatter.format("战斗单位[{}]的转换器重复", convertor.getType());
				logger.error(message.getMessage());
				throw new FightException(FightExceptionCode.CONFIG_ERROR, message.getMessage());
			}
		}
		// 超时处理线程
		Thread otThread = new Thread(new Runnable() {
			@Override
			public void run() {
				while (true) {
					try {
						DelayedElement<OvertimeWork> e = overtimeQueue.take();
						OvertimeWork work = e.getContent();
						Battle battle = battles.get(work.getId());
						if (battle == null) {
							continue;
						}
						Lock lock = rwLock.readLock();
						lock.lock();
						try {
							if (!running) {
								logger.info("战斗服务已经停止，超时处理被忽略!");
								continue;
							}
							synchronized (battle) {
								boolean end = false;
								boolean error = false;
								try {
									if (!battle.isValid(work)) {
										// 无效的超时任务直接不处理
										continue;
									}
									// 处理有效的超时任务
									battle.process(work, true);
									if (battle.isEnd() || work.getType() == OvertimeType.BATTLE) {
										end = true;
									} else {
										// 添加下个回合的超时处理
										work = battle.getRoundOtw();
										e = DelayedElement.valueOf(work, work.getOvertime());
										overtimeQueue.put(e);
									}
								} catch (RuntimeException ex) {
									end = true;
									error = true;
									// 战斗执行出现异常
									battle.onError(ex);
									logger.error("战斗过程中发生异常，战斗被终止", ex);
								} finally {
									if (end) {
										Date restoreTime = battle.getConfig().getRestoreOvertime();
										// 如果超时处理或者报错那么就移除战斗缓存,quyy(2013/07/28)
										if (restoreTime == null || work.getType() == OvertimeType.RESTORE || error || (restoreTime != null && battle.isRestoreEnd())) {
											// 战斗结束时，解除用户与战斗之间的关系
											for (Player owner : battle.getOwners()) {
												playerBattle.remove(owner);
											}
											OvertimeWork bOtw = battle.getOtw();
											if (bOtw != null) {
												overtimeQueue.remove(DelayedElement.valueOf(bOtw, bOtw.getOvertime()));
											}
											battles.remove(battle.getId());

											// 推送战斗状态
											for (Player pushTarget : battle.getOwners()) {
												mapService.pushFightState(pushTarget, false);
											}
										} else if (restoreTime != null && !error) {
											// 有战斗恢复时间存在，等待战斗恢复
											OvertimeWork rotw = OvertimeWork.valueOfRestore(battle.getId(), restoreTime);
											overtimeQueue.add(DelayedElement.valueOf(rotw, rotw.getOvertime()));
											battle.setRestoreOtw(rotw);
										}
									}
								}
							}
						} finally {
							lock.unlock();
						}
					} catch (Exception ex) {
						logger.error("战斗超时处理发生未知异常", ex);
					}
				}
			}
		}, "战斗超时");
		otThread.setDaemon(true);
		otThread.start();
		// 初始化服务状态
		Lock lock = rwLock.writeLock();
		lock.lock();
		try {
			running = true;
		} finally {
			lock.unlock();
		}
	}

	/**
	 * 获取玩家当前的战斗标识
	 * @param player 玩家对象
	 * @return 不存在会返回null
	 */
	public Integer getCurrentBattle(Player player) {
		Lock lock = rwLock.readLock();
		lock.lock();
		try {
			if (!running) {
				throw new FightException(FightExceptionCode.STATE_ERROR, "战斗服务已经关闭");
			}
			return playerBattle.get(player);
		} finally {
			lock.unlock();
		}
	}

	/**
	 * 修复玩家战斗状态
	 * @param player
	 */
	public void repair(Player player) {
		Lock lock = rwLock.writeLock();
		lock.lock();
		try {
			// 获取玩家身上的战斗标识
			Integer temp = playerBattle.get(player);
			if (temp == null) {
				return;
			}
			Battle current = battles.get(temp);
			// 存在战斗标识，且又没有战斗的移除
			if (current == null) {
				playerBattle.remove(player);
			}
		} finally {
			lock.unlock();
		}
	}

	/**
	 * 获取玩家当前的战斗信息
	 * @param player
	 * @return
	 */
	public BattleInfo getBattleInfo(Player player) {
		Lock lock = rwLock.readLock();
		lock.lock();
		try {
			if (!running) {
				throw new FightException(FightExceptionCode.STATE_ERROR, "战斗服务已经关闭");
			}
			Integer battleId = getCurrentBattle(player);
			if (battleId == null) {
				throw new FightException(FightExceptionCode.BATTLE_NOT_FOUND);
			}
			Battle battle = battles.get(battleId);
			if (battle == null) {
				throw new FightException(FightExceptionCode.BATTLE_NOT_FOUND);
			}
			synchronized (battle) {
				if (battle.isEnd()) {
					throw new FightException(FightExceptionCode.BATTLE_NOT_FOUND);
				}
				return BattleInfo.valueOf(battle);
			}
		} finally {
			lock.unlock();
		}
	}

	/**
	 * 开始战斗
	 * @param type 战斗类型
	 * @param attacker 攻击方
	 * @param defender 防守方
	 * @param callback 战斗回调
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public BattleInfo start(BattleType type, Battler attacker, Battler defender, BattleCallback callback) {
		Lock lock = rwLock.readLock();
		lock.lock();
		try {
			if (!running) {
				throw new FightException(FightExceptionCode.STATE_ERROR, "战斗服务已经关闭");
			}
			// 参数检查
			BattleConfig config = configs.get(type);
			if (config == null) {
				FormattingTuple message = MessageFormatter.format("战斗类型[{}]对应的战斗配置不存在", type);
				throw new FightException(FightExceptionCode.ARGUMENT_ERROR, message.getMessage());
			}
			if (attacker == null) {
				throw new FightException(FightExceptionCode.ARGUMENT_ERROR, "攻击方参数缺失");
			}
			if (defender == null) {
				throw new FightException(FightExceptionCode.ARGUMENT_ERROR, "防守方参数缺失");
			}
			// 构建战斗对象
			Fighter aFighter = transform(attacker, true, config.getType());
			if (aFighter == null) {
				throw new FightException(FightExceptionCode.ATTACKER_NOT_FOUND);
			}
			Fighter dFighter = transform(defender, false, config.getType());
			if (dFighter == null) {
				throw new FightException(FightExceptionCode.DEFENDER_NOT_FOUND);
			}

			Integer battleId = idBuilder.incrementAndGet();
			Battle battle = config.build(battleId, aFighter, dFighter, callback);

			// 设置与检查玩家与战斗之间的关系是否合法
			boolean rollback = false;
			battles.put(battleId, battle);
			List<Player> owners = new ArrayList<Player>(battle.getOwners());
			int i = 0;
			List<Player> tmp = new ArrayList<Player>();
			for (; i < owners.size(); i++) {
				Player player = owners.get(i);
				Integer prev = playerBattle.putIfAbsent(player, battleId);
				if (prev != null) {
					rollback = true;
					break;
				}
				tmp.add(player);
			}
			if (rollback) {
				for (Player player : tmp) {
					playerBattle.remove(player);
				}
				battles.remove(battleId);
				throw new FightException(FightExceptionCode.PLAYER_ALREADY_IN_BATTLE);
			}
			// 设置战斗超时处理 TODO
			OvertimeWork otw = battle.getOtw();
			if (otw != null) {
				DelayedElement<OvertimeWork> e = DelayedElement.valueOf(otw, otw.getOvertime());
				overtimeQueue.put(e);
			}
			//设置回合超时处理 TODO
			otw = battle.getRoundOtw();
			if (otw != null) {
				DelayedElement<OvertimeWork> e = DelayedElement.valueOf(otw, otw.getOvertime());
				overtimeQueue.put(e);
			}

			// 推送战斗状态
			for (Player pushTarget : battle.getOwners()) {
				mapService.pushFightState(pushTarget, true);
			}
			// 返回初始化战斗信息
			return battle.createBattleInfo();
		} finally {
			lock.unlock();
		}
	}

	@Override
	@SuppressWarnings("rawtypes")
	public Battle immediate(BattleType type, Battler attacker, Battler defender) {
		Lock lock = rwLock.readLock();
		lock.lock();
		try {
			if (!running) {
				throw new FightException(FightExceptionCode.STATE_ERROR, "战斗服务已经关闭");
			}
			// 参数检查
			BattleConfig config = configs.get(type);
			if (config == null) {
				FormattingTuple message = MessageFormatter.format("战斗类型[{}]对应的战斗配置不存在", type);
				throw new FightException(FightExceptionCode.ARGUMENT_ERROR, message.getMessage());
			}
			if (attacker == null) {
				throw new FightException(FightExceptionCode.ARGUMENT_ERROR, "攻击方参数缺失");
			}
			if (defender == null) {
				throw new FightException(FightExceptionCode.ARGUMENT_ERROR, "防守方参数缺失");
			}
			// 构建战斗对象
			Fighter aFighter = transform(attacker, true, config.getType());
			if (aFighter == null) {
				throw new FightException(FightExceptionCode.ATTACKER_NOT_FOUND);
			}
			Fighter dFighter = transform(defender, false, config.getType());
			if (dFighter == null) {
				throw new FightException(FightExceptionCode.DEFENDER_NOT_FOUND);
			}
			// 创建战斗对象
			Integer battleId = idBuilder.incrementAndGet();
			Battle battle = config.build(battleId, aFighter, dFighter, new CallbackTemplate() {
				@Override
				public boolean onBattleEnd(Battle battle, RoundReport last) {
					return true;
				}
			});
			OvertimeWork work = OvertimeWork.valueOfBattle(battle.getId(), new Date());
			try {
				battle.process(work, false);
			} catch (RuntimeException ex) {
				if (ex instanceof FightException) {
					throw ex;
				} else {
					throw new FightException(FightExceptionCode.PROCESS_ERROR, ex);
				}
			}
			// 返回初始化战斗信息
			return battle;
		} finally {
			lock.unlock();
		}
	}

	@Override
	public void cancel(Player player) {
		Lock lock = rwLock.readLock();
		lock.lock();
		try {
			if (!running) {
				throw new FightException(FightExceptionCode.STATE_ERROR, "战斗服务已经关闭");
			}
			Integer battleId = playerBattle.get(player);
			if (battleId == null) {
				return;
			}
			Battle battle = battles.get(battleId);
			if (battle == null) {
				return;
			}
			synchronized (battle) {
				if (battle.isEnd()) {
					return;
				}
				Object result = null;
				// 执行战斗取消通知
				ChainLock playerLock = LockUtils.getLock(player);
				playerLock.lock();
				try {
					result = battle.cancel(battle);
				} catch (Exception ex) {
					logger.error("取消战斗时发生未知异常", ex);
				} finally {
					playerLock.unlock();
				}
				// 战斗结束时，解除用户与战斗之间的关系
				for (Player owner : battle.getOwners()) {
					playerBattle.remove(owner);
				}
				OvertimeWork bOtw = battle.getOtw();
				if (bOtw != null) {
					overtimeQueue.remove(DelayedElement.valueOf(bOtw, bOtw.getOvertime()));
				}
				bOtw = battle.getRoundOtw();
				if (bOtw != null) {
					overtimeQueue.remove(DelayedElement.valueOf(bOtw, bOtw.getOvertime()));
				}
				battles.remove(battle.getId());

				// 推送战斗状态
				for (Player pushTarget : battle.getOwners()) {
					mapService.pushFightState(pushTarget, false);
				}

				// 推送战斗取消通知
				Request<Object> request = Request.valueOf(result);
				sessionManager.send(request, player.getId());
			}
		} finally {
			lock.unlock();
		}
	}

	@Override
	public void skip(Player player) {
		Lock lock = rwLock.readLock();
		lock.lock();
		try {
			if (!running) {
				throw new FightException(FightExceptionCode.STATE_ERROR, "战斗服务已经关闭");
			}
			Integer battleId = playerBattle.get(player);
			if (battleId == null) {
				return;
			}
			Battle battle = battles.get(battleId);
			if (battle == null) {
				return;
			}
			if (!battle.getConfig().isSkip()) {
				throw new FightException(FightExceptionCode.CONFIG_ERROR, "跳过战斗不被允许");
			}
			synchronized (battle) {
				try {
					if (battle.isEnd()) {
						return;
					}
					OvertimeWork work = OvertimeWork.valueOfBattle(battle.getId(), new Date());
					battle.process(work, true);
				} catch (RuntimeException ex) {
					// 战斗执行出现异常
					battle.onError(ex);
					logger.error("跳过战斗过程发生异常，战斗被终止", ex);
				} finally {
					// 战斗结束时，解除用户与战斗之间的关系
					for (Player owner : battle.getOwners()) {
						playerBattle.remove(owner);
					}
					battles.remove(battle.getId());

					// 推送战斗状态
					for (Player pushTarget : battle.getOwners()) {
						mapService.pushFightState(pushTarget, false);
					}
					OvertimeWork bOtw = battle.getOtw();
					if (bOtw != null) {
						overtimeQueue.remove(DelayedElement.valueOf(bOtw, bOtw.getOvertime()));
					}
					bOtw = battle.getRoundOtw();
					if (bOtw != null) {
						overtimeQueue.remove(DelayedElement.valueOf(bOtw, bOtw.getOvertime()));
					}
					// 移除复活超时信息
					bOtw = battle.getRestoreOtw();
					if (bOtw != null) {
						overtimeQueue.remove(DelayedElement.valueOf(bOtw, bOtw.getOvertime()));
						battle.removeRestoreOtw();
					}
				}
			}
		} finally {
			lock.unlock();
		}
	}

	/**
	 * 恢复战斗(revive方法取代，暂留)
	 * @param battleId 战斗标识
	 * @param attacker 替换的攻击方(允许为null)
	 * @param defender 替换的防守方(允许为null)
	 * @return
	 */
	@Deprecated
	public BattleInfo restore(int battleId, Fighter attacker, Fighter defender) {
		Lock lock = rwLock.readLock();
		lock.lock();
		try {
			if (!running) {
				throw new FightException(FightExceptionCode.STATE_ERROR, "战斗服务已经关闭");
			}
			Battle battle = battles.get(battleId);
			if (battle == null) {
				return null;
			}
			synchronized (battle) {
				if (!battle.isEnd()) {
					return null;
				}
				battle.restore(attacker, defender);
				overtimeQueue.remove(DelayedElement.valueOf(OvertimeWork.valueOfRestore(battle.getId(), new Date()), new Date()));
				return battle.createBattleInfo();
			}
		} finally {
			lock.unlock();
		}
	}

	/**
	 * 复活战斗单位
	 * @param battleId 战斗单位ID
	 * @param isAttack 是否复活攻击方，否则就是防守方
	 * @return
	 */
	@Override
	public BattleInfo revive(Player player, boolean isAttacker, ReviveCallback callback) {
		Lock lock = rwLock.readLock();
		lock.lock();
		try {
			if (!running) {
				throw new FightException(FightExceptionCode.STATE_ERROR, "战斗服务已经关闭");
			}
			Integer battleId = playerBattle.get(player);
			if (battleId == null) {
				throw new FightException(FightExceptionCode.BATTLE_NOT_FOUND);
			}
			Battle battle = battles.get(battleId);
			if (battle == null) {
				throw new FightException(FightExceptionCode.BATTLE_NOT_FOUND);
			}
			synchronized (battle) {
				if (!battle.isEnd()) {
					return null;
				}

				// 锁住Player
				ChainLock playerLock = LockUtils.getLock(player);
				playerLock.lock();
				try {
					boolean isRevive = callback.revive(battle);
					// 玩家复活合法则执行复活操作
					if (isRevive) {
						battle.revive(isAttacker);
						battle.revive(battle);
					}
				} finally {
					playerLock.unlock();
				}
				// 添加下回合超时时间
				OvertimeWork otw = OvertimeWork.valueOfRound(battle.getId(), battle.getRound(), battle.getConfig().getRoundOvertime());
				overtimeQueue.put(DelayedElement.valueOf(otw, otw.getOvertime()));
				// 移除复活超时信息
				otw = battle.getRestoreOtw();
				if (otw != null) {
					overtimeQueue.remove(DelayedElement.valueOf(otw, otw.getOvertime()));
					battle.removeRestoreOtw();
				}
				return battle.createBattleInfo();
			}
		} finally {
			lock.unlock();
		}
	}

	/**
	 * 玩家设置自己在战斗中主将使用的技能
	 * @param owner 玩家对象
	 * @param skill 被设置的技能标识
	 * @param noWait 是否不等待回合时间，立即返回
	 */
	public RoundReport choseMajorSkill(Player owner, String skill, boolean noWait) {
		Lock lock = rwLock.readLock();
		lock.lock();
		try {
			if (!running) {
				throw new FightException(FightExceptionCode.STATE_ERROR, "战斗服务已经关闭");
			}
			final Integer battleId = playerBattle.get(owner);
			if (battleId == null) {
				throw new FightException(FightExceptionCode.BATTLE_NOT_FOUND);
			}
			Battle battle = battles.get(battleId);
			if (battle == null) {
				throw new FightException(FightExceptionCode.BATTLE_NOT_FOUND);
			}
			if (noWait && !battle.getConfig().isNoWait()) {
				throw new FightException(FightExceptionCode.BATTLE_CANNOT_NOWAIT);
			}
			synchronized (battle) {
				boolean end = false;
				boolean error = false;
				try {
					// 设置使用技能
					battle.choseMajorSkill(owner, skill);
					if (!noWait) {
						return null;
					}
					// 检查战斗是否准备就绪(可以生成回合战报)
					if (!battle.isReady()) {
						return null;
					}
					// 移除回合超时处理
					OvertimeWork otw = battle.getRoundOtw();
					if (otw != null) {
						overtimeQueue.remove(DelayedElement.valueOf(otw, otw.getOvertime()));
					}
					// 执行回合处理
					RoundReport ret = battle.next(owner);
					if (!battle.isEnd()) {
						// 添加下个回合的超时处理
						otw = battle.getRoundOtw();
						if (otw != null) {
							DelayedElement<OvertimeWork> e = DelayedElement.valueOf(otw, otw.getOvertime());
							overtimeQueue.put(e);
						}
					} else {
						end = true;
					}
					return ret;
				} catch (RuntimeException ex) {
					if (ex instanceof FightException) {
						FightException e = (FightException) ex;
						if (e.getCode() == FightExceptionCode.SKILL_CANNOT_CHOSE) {
							throw e;
						}
					}
					end = true;
					error = true;
					// 战斗执行出现异常
					battle.onError(ex);
					logger.error("战斗过程中发生异常，战斗被终止", ex);
					if (ex instanceof FightException) {
						throw ex;
					} else {
						throw new FightException(FightExceptionCode.PROCESS_ERROR, ex);
					}
				} finally {
					if (end) {
						Date restoreTime = battle.getConfig().getRestoreOvertime();
						// 如果没有超时设定或者报错那么就移除战斗缓存,quyy(2013/07/28)
						// 复活超时的战斗且战斗已经标识未结束也移除战斗信息
						if (restoreTime == null || error || (restoreTime != null && battle.isRestoreEnd())) {
							// 战斗结束时，解除用户与战斗之间的关系
							for (Player fighter : battle.getOwners()) {
								playerBattle.remove(fighter);
							}
							battles.remove(battle.getId());

							OvertimeWork otw = battle.getOtw();
							if (otw != null) {
								overtimeQueue.remove(DelayedElement.valueOf(otw, otw.getOvertime()));
							}

							// 推送战斗状态
							for (Player pushTarget : battle.getOwners()) {
								mapService.pushFightState(pushTarget, false);
							}
						} else if (!error) {
							// 有战斗恢复时间存在，等待战斗恢复
							OvertimeWork otw = OvertimeWork.valueOfRestore(battle.getId(), restoreTime);
							overtimeQueue.add(DelayedElement.valueOf(otw, otw.getOvertime()));
							battle.setRestoreOtw(otw);
						}
					}
				}
			}
		} finally {
			lock.unlock();
		}
	}

	/**
	 * 选择回合技能
	 * @param owner
	 * @param skill
	 */
	public void choseRoundSkill(Player owner, String skill) {
		Lock lock = rwLock.readLock();
		lock.lock();
		try {
			if (!running) {
				throw new FightException(FightExceptionCode.STATE_ERROR, "战斗服务已经关闭");
			}
			final Integer battleId = playerBattle.get(owner);
			if (battleId == null) {
				throw new FightException(FightExceptionCode.BATTLE_NOT_FOUND);
			}
			Battle battle = battles.get(battleId);
			if (battle == null) {
				throw new FightException(FightExceptionCode.BATTLE_NOT_FOUND);
			}
			// 设置大招
			synchronized (battle) {
				battle.choseRoundSkill(owner, skill);
			}
		} finally {
			lock.unlock();
		}
	}

	/**
	 * 进行自动战斗并获取当前回合的战报
	 * @param owner
	 * @return
	 */
	public RoundReport autoChose(Player owner) {
		Lock lock = rwLock.readLock();
		lock.lock();
		try {
			if (!running) {
				throw new FightException(FightExceptionCode.STATE_ERROR, "战斗服务已经关闭");
			}

			final Integer battleId = playerBattle.get(owner);
			if (battleId == null) {
				throw new FightException(FightExceptionCode.BATTLE_NOT_FOUND);
			}
			Battle battle = battles.get(battleId);
			if (battle == null) {
				throw new FightException(FightExceptionCode.BATTLE_NOT_FOUND);
			}
			synchronized (battle) {
				boolean end = false;
				boolean error = false;
				try {
					// 判断玩家是都有自动战斗的权限
					boolean flag = battle.canAuto(owner);
					if (!flag) {
						return null;
					}
					// 移除回合超时处理
					OvertimeWork otw = battle.getRoundOtw();
					if (otw != null) {
						overtimeQueue.remove(DelayedElement.valueOf(otw, otw.getOvertime()));
					}
					// 执行回合处理
					RoundReport ret = battle.next(owner);
					if (!battle.isEnd()) {
						// 添加下个回合的超时处理
						otw = battle.getRoundOtw();
						if (otw != null) {
							DelayedElement<OvertimeWork> e = DelayedElement.valueOf(otw, otw.getOvertime());
							overtimeQueue.put(e);
						}
					} else {
						end = true;
					}
					return ret;
				} catch (RuntimeException ex) {
					end = true;
					error = true;
					// 战斗执行出现异常
					battle.onError(ex);
					logger.error("战斗过程中发生异常，战斗被终止", ex);
					throw new FightException(FightExceptionCode.PROCESS_ERROR, ex);
				} finally {
					if (end) {
						Date restoreTime = battle.getConfig().getRestoreOvertime();
						if (restoreTime == null || error || (restoreTime != null && battle.isRestoreEnd())) {
							// 战斗结束时，解除用户与战斗之间的关系
							// 复活超时的战斗且战斗已经标识未结束也移除战斗信息
							for (Player fighter : battle.getOwners()) {
								playerBattle.remove(fighter);
							}
							battles.remove(battle.getId());

							OvertimeWork otw = battle.getOtw();
							if (otw != null) {
								overtimeQueue.remove(DelayedElement.valueOf(otw, otw.getOvertime()));
							}

							// 推送战斗状态 TODO 推送状态
							for (Player pushTarget : battle.getOwners()) {
								mapService.pushFightState(pushTarget, false);
							}
						} else if (!error) {
							// 有战斗恢复时间存在，等待战斗恢复
							OvertimeWork otw = OvertimeWork.valueOfRestore(battle.getId(), restoreTime);
							overtimeQueue.add(DelayedElement.valueOf(otw, otw.getOvertime()));
							battle.setRestoreOtw(otw);
						}
					}
				}
			}
		} finally {
			lock.unlock();
		}
	}

	/**
	 * 将{@link Battler}转换为{@link Fighter}
	 * @param battler 作战标识
	 * @param isAttacker 攻击方/防守方
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Fighter transform(Battler battler, boolean isAttacker, BattleType battleType) {
		FighterType type = battler.getType();
		FighterConvertor convertor = convertors.get(type);
		if (convertor == null) {
			FormattingTuple message = MessageFormatter.format("战斗单位类型[{}]的转换器不存在", type);
			logger.error(message.getMessage());
			throw new FightException(FightExceptionCode.CONFIG_ERROR, message.getMessage());
		}
		Fighter fighter = convertor.convert(battler.getContent(), isAttacker);
		return fighter;
	}

	// 其它实现的方法

	private ApplicationContext applicationContext;

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

	@Override
	public void onApplicationEvent(ContextClosedEvent event) {
		Lock lock = rwLock.writeLock();
		lock.lock();
		try {
			// 设置服务状态
			running = false;
			if (logger.isInfoEnabled()) {
				logger.info("战斗服务收到容器关闭事件，开始取消进行中的战斗");
			}
		} finally {
			lock.unlock();
		}

		try {
			// 为了避免死锁很丑陋的实现方式
			// TODO 要看看有没有更好的解决办法
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			logger.error("战斗系统进行关闭等待时被非法打断", e);
		} finally {
			// 结束战斗
			for (Battle battle : battles.values()) {
				OvertimeWork work = battle.getOtw();
				synchronized (battle) {
					try {
						battle.process(work, false);
					} catch (RuntimeException ex) {
						// 战斗执行出现异常
						battle.onError(ex);
						logger.error("取消战斗过程中发生异常，战斗被终止", ex);
					} finally {
						// 战斗结束时，解除用户与战斗之间的关系
						for (Player owner : battle.getOwners()) {
							playerBattle.remove(owner);
						}
						battles.remove(battle.getId());
					}
				}
			}
			if (logger.isInfoEnabled()) {
				logger.info("战斗服务完成取消进行中的战斗");
			}
		}
	}

}
