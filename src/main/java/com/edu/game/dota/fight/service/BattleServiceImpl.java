package com.edu.game.dota.fight.service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import com.eyu.common.ramcache.aop.AutoLocked;
import com.eyu.common.ramcache.aop.IsLocked;
import com.eyu.common.scheduler.impl.FixTimeDelayQueue;
import com.eyu.common.utils.concurrent.DelayedElement;
import com.eyu.common.utils.json.JsonUtils;
import com.eyu.common.utils.thread.NamedThreadFactory;
import com.eyu.snm.module.common.SystemConfig;
import com.eyu.snm.module.fight.exception.FightException;
import com.eyu.snm.module.fight.model.FighterType;
import com.eyu.snm.module.fight.model.VerifyResult;
import com.eyu.snm.module.fight.model.info.BattleInfo;
import com.eyu.snm.module.fight.model.report.seed.OperationData;
import com.eyu.snm.module.fight.resource.IdHolder;
import com.eyu.snm.module.fight.service.config.BattleConfig;
import com.eyu.snm.module.fight.service.converter.FighterConvertor;
import com.eyu.snm.module.fight.service.core.Battle;
import com.eyu.snm.module.fight.service.core.Fighter;
import com.eyu.snm.module.fight.service.op.Operation;
import com.eyu.snm.module.player.service.Player;

/**
 * 战斗服务<br/>
 * @author frank
 */
@Service
public class BattleServiceImpl implements BattleService, ApplicationContextAware {

	private static final Logger logger = LoggerFactory.getLogger(BattleServiceImpl.class);

	/** 不同类型的战斗配置器 */
	private ConcurrentHashMap<BattleType, BattleConfig> configs = new ConcurrentHashMap<BattleType, BattleConfig>(
			BattleType.values().length);
	/** 不同的战斗单位转换器 */
	@SuppressWarnings("rawtypes")
	private ConcurrentHashMap<FighterType, FighterConvertor> convertors = new ConcurrentHashMap<FighterType, FighterConvertor>(
			FighterType.values().length);

	/** 战斗回调触发队列 */
	private FixTimeDelayQueue<DelayedElement<CallbackWrapper>> callbackQueue;
	private ExecutorService callbackPool;
	private Thread callbackDispatcher;
	@Autowired
	private IdHolder idHolder;

	// ======== 随机种子战斗友情的分割线 =======
	@Autowired
	private SystemConfig systemConfig;
	/** 战斗对象的标识生成器 */
	private AtomicInteger idBuilder = new AtomicInteger();
	// /** 玩家与战斗对象之间的对应关系(一对一) */
	// private ConcurrentHashMap<Long, Integer> playerBattle = new ConcurrentHashMap<Long, Integer>();
	/** 当前的存在的全部战斗对象 */
	private ConcurrentHashMap<Integer, Battle> battles = new ConcurrentHashMap<Integer, Battle>();
	/** 战斗超时队列 */
	private FixTimeDelayQueue<DelayedElement<OvertimeWork>> overtimeQueue;

	// ==================================================

	/** 初始化方法 */
	@SuppressWarnings("rawtypes")
	@PostConstruct
	public void initialize() {
		// 初始化战斗配置器信息
		Map<String, BattleConfig> configBeans = this.actx.getBeansOfType(BattleConfig.class);
		for (BattleConfig config : configBeans.values()) {
			if (configs.put(config.getType(), config) != null) {
				logger.error("战斗类型[{}]的配置对象重复", config.getType());
				throw new RuntimeException("战斗类型[" + config.getType() + "]的配置对象重复");
			}
		}

		if (configs.size() < BattleType.values().length) {
			throw new RuntimeException("战斗类型配置对象数量不足");
		}

		// 初始化战斗单位转换器信息
		Map<String, FighterConvertor> convertorBeans = this.actx.getBeansOfType(FighterConvertor.class);
		for (FighterConvertor convertor : convertorBeans.values()) {
			if (convertors.put(convertor.getType(), convertor) != null) {
				logger.error("战斗单位[{}]的转换器重复", convertor.getType());
				throw new RuntimeException("战斗单位[" + convertor.getType() + "]的转换器重复");
			}
		}

		if (convertors.size() < FighterType.values().length) {
			throw new RuntimeException("战斗单位转换器数量不足");
		}

		// 初始化战斗回调触发队列
		callbackQueue = new FixTimeDelayQueue<>(systemConfig.getDelayTime());
		callbackPool = Executors.newFixedThreadPool(1, new NamedThreadFactory("战斗模块:战斗回调"));
		callbackDispatcher = new Thread(new Runnable() {
			@Override
			@SuppressWarnings("unchecked")
			public void run() {
				while (!Thread.interrupted()) {
					try {
						final DelayedElement<CallbackWrapper> delayed = callbackQueue.take();
						callbackPool.submit(new Runnable() {
							@Override
							public void run() {
								CallbackWrapper wrapper = delayed.getContent();
								BattleCallback callback = wrapper.callback;
								callback.onBattleEnd(wrapper.attacker, wrapper.defender, wrapper.battle);
							}
						});
					} catch (Exception e) {
						logger.error("战斗回调分发线程发生异常", e);
					}
				}

				logger.info("战斗模块:战斗回调分发线程正常关闭");
			}
		}, "战斗模块:战斗回调分发");
		callbackDispatcher.setDaemon(true);
		callbackDispatcher.start();
		logger.info("战斗模块:战斗回调分发启动");

		// ======================== 手操部分 ==================
		overtimeQueue = new FixTimeDelayQueue<>(systemConfig.getDelayTime());
		Thread otThread = new Thread(new Runnable() {
			@Override
			public void run() {
				while (!Thread.interrupted()) {
					try {
						DelayedElement<OvertimeWork> e = overtimeQueue.take();
						OvertimeWork oWork = e.getContent();
						Battle battle = battles.get(oWork.getId());
						if (battle != null) {
							battle.verifyTimeOut();
						}
						// 移除超时的战斗数据
						battles.remove(oWork.getId());
						// playerBattle.remove(oWork.getOwner());
					} catch (Exception e) {
						logger.error("战斗超时处理线程发生异常", e);
					}
				}
			}
		}, "战斗超时处理线程");
		otThread.setDaemon(true);
		otThread.start();
		logger.info("战斗模块:战斗超时处理线程启动");
	}

	@Override
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Battle immediate(BattleType type, FighterId aid, FighterId did, BattleCallback callback) {
		BattleConfig config = configs.get(type);
		List<Fighter> attackers = convertors.get(aid.getType()).convert(aid.getContent(), true);
		List<Fighter> defenders = convertors.get(did.getType()).convert(did.getContent(), false);
		Battle battle = buildBattle(attackers, defenders, config);

		// 操作循环
		Operation op = battle.getSoonOp();
		while (op != null) {
			op = op.execute(battle);
		}

		// 处理战斗回调
		if (callback != null) {
			Date end = new Date(battle.getDuration());
			CallbackWrapper wrapper = new CallbackWrapper(aid, did, callback, battle);
			DelayedElement<CallbackWrapper> delayed = DelayedElement.valueOf(wrapper, end);
			callbackQueue.add(delayed);
		}

		return battle;
	}

	/** 构造战斗对象 */
	private Battle buildBattle(List<Fighter> attackers, List<Fighter> defenders, BattleConfig config) {
		isIllegal(attackers);
		isIllegal(defenders);
		Battle battle = Battle.valueOf(attackers, defenders, config, idHolder);
		// 配置战斗单位组信息
		for (Fighter fighter : attackers) {
			fighter.setId(true, battle.getNextFighterId());
			fighter.setOwner(battle, config.isManual());
		}
		for (Fighter fighter : defenders) {
			fighter.setId(false, battle.getNextFighterId());
			fighter.setOwner(battle, false);
		}
		return battle;
	}

	private ApplicationContext actx;

	@Override
	public void setApplicationContext(ApplicationContext actx) throws BeansException {
		this.actx = actx;
	}

	/** 检测战斗对象数据是否合法 */
	private void isIllegal(List<Fighter> fighters) {
		for (Fighter fighter : fighters) {
			if (fighter.getUnits().size() == 0) {
				throw new IllegalArgumentException("战斗单元数量为0,构造FIGHTER失败");
			}
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	@AutoLocked
	public BattleInfo start(@IsLocked Player player, BattleType type, FighterId aid, FighterId fid, RandomCallBack callback) {
		// if (playerBattle.get(player.getId()) != null) {
		// throw new FightException(FightExceptionCode.ALREADY_IN_BATTLE);
		// }
		BattleConfig config = configs.get(type);
		List<Fighter> attackers = convertors.get(aid.getType()).convert(aid.getContent(), true);
		List<Fighter> defenders = convertors.get(fid.getType()).convert(fid.getContent(), false);
		Battle battle = buildBattle(attackers, defenders, config);
		battle.setRandomCallBack(callback);
		Integer battleId = idBuilder.incrementAndGet();
		// playerBattle.put(player.getId(), battleId);
		battles.put(battleId, battle);
		OvertimeWork oWork = OvertimeWork.valueOf(player.getId(), battleId, new Date(config.getVerifyTime() + battle.getDuration()));
		overtimeQueue.add(new DelayedElement<OvertimeWork>(oWork, oWork.getOvertime()));
		return BattleInfo.valueOf(battleId, battle.getSeed(), attackers, defenders);
	}

	@Override
	@AutoLocked
	public VerifyResult verify(@IsLocked Player player, int battleId, List<OperationData>[] opData) {
		// Integer playerBattleId = playerBattle.get(player.getId());
		// if (playerBattleId == null || playerBattleId != battleId) {
		// throw new FightException(FightExceptionCode.ALREADY_IN_BATTLE);
		// }
		boolean result = true;
		Battle battle = battles.get(battleId);
		try {
			// 操作循环
			Operation op = battle.getSoonOp();
			battle.insert(opData);
			while (op != null) {
				op = op.execute(battle);
			}
		} catch (FightException e) {
			logger.error("战斗验证失败", e);
			result = false;
		}

		System.err.println(JsonUtils.object2String(battle.getReport()));
		System.out.println();
		// // 战斗结束
		// // playerBattle.remove(player);
		battles.remove(battleId);
		overtimeQueue.remove(DelayedElement.valueOf(OvertimeWork.valueOf(player.getId(), battleId, null), null));
		if (result) {
//			return battle.getResult();
		}
		return null;
	}

}
