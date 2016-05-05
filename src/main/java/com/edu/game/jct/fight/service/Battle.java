package com.edu.game.jct.fight.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.helpers.FormattingTuple;
import org.slf4j.helpers.MessageFormatter;

import com.edu.game.Player;
import com.edu.game.Request;
import com.edu.game.SessionManager;
import com.edu.game.jct.fight.exception.FightException;
import com.edu.game.jct.fight.exception.FightExceptionCode;
import com.edu.game.jct.fight.model.BattleInfo;
import com.edu.game.jct.fight.model.BattleResult;
import com.edu.game.jct.fight.model.FighterInfo;
import com.edu.game.jct.fight.model.ReturnVo;
import com.edu.game.jct.fight.model.UnitInfo;
import com.edu.game.jct.fight.model.UnitState;
import com.edu.game.jct.fight.model.UnitValue;
import com.edu.game.jct.fight.model.report.BattleReport;
import com.edu.game.jct.fight.model.report.InitReport;
import com.edu.game.jct.fight.model.report.InitTargetReport;
import com.edu.game.jct.fight.model.report.RoundReport;
import com.edu.game.jct.fight.service.config.BattleType;
import com.edu.game.jct.fight.service.core.Context;
import com.edu.game.jct.fight.service.core.Fighter;
import com.edu.game.jct.fight.service.core.Unit;
import com.edu.game.jct.fight.service.effect.init.InitEffect;
import com.edu.game.jct.fight.service.effect.init.InitEffectFactory;
import com.edu.game.jct.fight.service.effect.init.InitEffectState;
import com.edu.game.lock.ChainLock;
import com.edu.game.lock.LockUtils;

/**
 * 战斗对象
 * @author frank
 */
public class Battle {

	private static final Logger logger = LoggerFactory.getLogger(Battle.class);

	/** 战斗配置对象 */
	private BattleConfig config;
	/** 会话管理器 */
	private SessionManager sessionManager;
	/** 战斗回调 */
	private BattleCallback callback;

	/** 战斗标识 */
	private int id;
	/** 战斗所有者与战斗单元的关系 */
	private Map<Player, Fighter> owners = new HashMap<Player, Fighter>(1);
	/** 攻击方 */
	private Fighter attacker;
	/** 防守方 */
	private Fighter defender;

	/** 当前的战斗回合 */
	private Round current;
	/** 当前回合数 */
	private int round;
	/** 战报对象 */
	private BattleReport report;
	/** 战斗超时处理信息 */
	private OvertimeWork otw;
	/** 战斗结束标记 */
	private boolean end;
	/** 战斗结果 */
	private BattleResult result;

	/** 战斗信息(提供给具体玩法保存战斗相关的信息) */
	private Map<String, Object> infos;
	/** 恢复超时处理信息 */
	private OvertimeWork restoreOtw;
	/** 恢复处理是都真正的结束，用来移除超时信息 ,true标识结束 */
	private boolean restoreEnd = false;

	/** 构造器 */
	private Battle(SessionManager sessionManager, BattleConfig config, int id, Fighter attacker, Fighter defender,
			BattleCallback callback) {
		// 初始化赋值
		this.sessionManager = sessionManager;
		this.config = config;
		this.id = id;
		this.attacker = attacker;
		this.defender = defender;
		this.callback = callback;
		for (Player player : attacker.getOwners().keySet()) {
			this.owners.put(player, attacker);
		}
		for (Player player : defender.getOwners().keySet()) {
			this.owners.put(player, defender);
		}
		this.report = BattleReport.valueOf(attacker, defender, config.getType());
		Date overtime = config.getBattleOvertime();
		this.otw = OvertimeWork.valueOfBattle(id, overtime);
		// 开始战斗并初始化第一回合
		start(true);
	}

	/**
	 * 恢复战斗
	 * @param attacker
	 * @param defender
	 */
	void restore(Fighter attacker, Fighter defender) {
		if (attacker != null) {
			this.attacker = attacker;
			for (Player player : attacker.getOwners().keySet()) {
				this.owners.put(player, attacker);
			}
		}
		if (defender != null) {
			this.defender = defender;
			for (Player player : defender.getOwners().keySet()) {
				this.owners.put(player, defender);
			}
		}
		this.end = false;
		this.result = null;
		start(false);
	}

	/**
	 * 复活战斗单位
	 * @param isAttacker
	 * @return false标识战斗单位里面存在没有死亡的单位,true标识复活成功
	 */
	boolean revive(boolean isAttacker) {
		boolean revive = false;
		if (isAttacker) {
			revive = attacker.revive();
		} else {
			revive = defender.revive();
		}
		if (revive) {
			this.end = false;
			this.result = null;
			start(false);
		}
		return revive;
	}

	/**
	 * 战斗过程中出现异常的通知方法
	 * @param ex 发生的异常
	 */
	public void onError(RuntimeException ex) {
		Map<Long, ReturnVo> rets = callback.onError(ex);
		if (rets == null || rets.isEmpty()) {
			// 没有返还信息的推送
			Request<ReturnVo> request = Request.valueOf(null);
			List<Long> targets = new ArrayList<Long>(owners.size());
			for (Player player : owners.keySet()) {
				targets.add(player.getId());
			}
			sessionManager.send(request, targets.toArray());
		} else {
			// 带有返还信息的推送
			for (Player player : owners.keySet()) {
				Long playerId = player.getId();
				Request<ReturnVo> request = Request.valueOf(rets.get(playerId));
				sessionManager.send(request, playerId);
			}
		}
	}

	/**
	 * 战斗取消时的回调调用
	 * @return
	 */
	public Object cancel(Battle battle) {
		return callback.onCancel(battle);
	}

	/**
	 * 复活战斗单位回调调用
	 * @param battle
	 */
	public void revive(Battle battle) {
		callback.onRevive(battle);
	}

	/**
	 * 获取总伤害值
	 * @param isAttacker 是否攻击方
	 * @return
	 */
	public int getTotalDamage(boolean isAttacker) {
		int start = 0;
		FighterInfo info = report.getInitFighters()[isAttacker ? 0 : 1];
		for (UnitInfo[] us : info.getCurrent()) {
			for (UnitInfo u : us) {
				if (u != null) {
					start += u.getHp();
				}
			}
		}
		int end = 0;
		Fighter fighter = isAttacker ? attacker : defender;
		for (Unit[] us : fighter.getCurrents()) {
			for (Unit u : us) {
				if (u != null) {
					end += u.getValue(UnitValue.HP);
				}
			}
		}
		return start - end;
	}

	/**
	 * 设置战斗信息
	 * @param key 信息键
	 * @param value 信息值
	 */
	public void setInfo(String key, Object value) {
		if (infos == null) {
			infos = new HashMap<String, Object>(1);
		}
		infos.put(key, value);
	}

	/**
	 * 获取战斗信息
	 * @param key 信息键
	 * @param defualtValue 默认值
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T> T getInfo(String key, T defualtValue) {
		if (infos == null) {
			return defualtValue;
		}
		T result = (T) infos.get(key);
		if (result == null) {
			return defualtValue;
		}
		return result;
	}

	// 战斗流程方法

	/** 开始战斗 */
	public void start(boolean doInit) {
		if (doInit) {
			// 执行初始化效果
			InitEffectFactory factory = InitEffectFactory.getInstance();
			Context ctx = new Context();
			for (Unit owner : attacker.getAllLive()) {
				// 迭代每个初始化效果
				for (InitEffectState state : owner.getInitEffectState()) {
					InitEffect skill = factory.getInitEffect(state.getId());
					// 获取迭代效果执行目标
					List<Unit> targets = skill.select(state, owner, attacker, defender);
					if (targets.isEmpty()) {
						continue;
					}
					InitReport initReport = InitReport.valueOf(owner, state);
					for (Unit target : targets) {
						InitTargetReport initTargetReport = InitTargetReport.valueOf(target);
						skill.execute(state, owner, target, ctx, initTargetReport);
						initReport.addTarget(initTargetReport);
					}
					report.addInit(initReport);
				}
			}
			for (Unit owner : defender.getAllLive()) {
				// 迭代每个初始化效果
				for (InitEffectState state : owner.getInitEffectState()) {
					InitEffect skill = factory.getInitEffect(state.getId());
					// 获取迭代效果执行目标
					List<Unit> targets = skill.select(state, owner, defender, attacker);
					if (targets.isEmpty()) {
						continue;
					}
					InitReport initReport = InitReport.valueOf(owner, state);
					for (Unit target : targets) {
						InitTargetReport initTargetReport = InitTargetReport.valueOf(target);
						skill.execute(state, owner, target, ctx, initTargetReport);
						initReport.addTarget(initTargetReport);
					}
					report.addInit(initReport);
				}
			}
			ctx.effect();
		}
		// 直接进入下一回合
		nextRound();
	}

	/** 进入下一回合 */
	private void nextRound() {
		this.round++;
		this.current = Round.valueOf(this);
	}

	// 其它方法

	/**
	 * 获取友方战斗单位对象
	 * @param unit 选择依据
	 * @return
	 */
	public Fighter getFriend(Unit unit) {
		if (unit.getId().startsWith(Unit.ATTACER_PREFIX)) {
			return attacker;
		} else {
			return defender;
		}
	}

	/**
	 * 获取敌方战斗单位对象
	 * @param unit 选择依据
	 * @return
	 */
	public Fighter getEnemy(Unit unit) {
		if (unit.getId().startsWith(Unit.ATTACER_PREFIX)) {
			return defender;
		} else {
			return attacker;
		}
	}

	/**
	 * 获取全部活着的出战单元
	 * @return 不会返回null
	 */
	public List<Unit> getAllLive() {
		List<Unit> result = new ArrayList<Unit>();
		Collections.addAll(result, attacker.getAllLive().toArray(new Unit[0]));
		Collections.addAll(result, defender.getAllLive().toArray(new Unit[0]));
		return result;
	}

	/**
	 * 获取全部出战单元
	 * @return
	 */
	public List<Unit> getAllUnits() {
		List<Unit> result = new ArrayList<Unit>();
		for (Unit[] us : attacker.getCurrents()) {
			for (Unit u : us) {
				if (u == null)
					continue;
				else
					result.add(u);
			}
		}
		for (Unit[] us : defender.getCurrents()) {
			for (Unit u : us) {
				if (u == null)
					continue;
				else
					result.add(u);
			}
		}
		return result;
	}

	/**
	 * 获取战斗所有者集合
	 * @return
	 */
	public Collection<Player> getOwners() {
		return owners.keySet();
	}

	/**
	 * 获取战斗超时处理对象
	 * @return 不存在返回null
	 */
	public OvertimeWork getOtw() {
		return otw;
	}

	/**
	 * 获取回合超时处理对象
	 * @return 不存在返回null
	 */
	public OvertimeWork getRoundOtw() {
		return current.getOtw();
	}

	/**
	 * 获取战斗描述信息
	 * @return
	 */
	public BattleInfo createBattleInfo() {
		return BattleInfo.valueOf(this);
	}

	/**
	 * 设置玩家战斗单元当前回合所使用的技能
	 * @param player 玩家标识
	 * @param skill 选择的技能标识
	 */
	public void choseMajorSkill(Player player, String skill) {
		Fighter fighter = owners.get(player);
		if (fighter == null) {
			FormattingTuple message = MessageFormatter.format("战斗[{}]没有玩家[{}]对应的战斗单元", id, player);
			logger.error(message.getMessage());
			throw new FightException(FightExceptionCode.STATE_ERROR, message.getMessage());
		}
		ChainLock lock = LockUtils.getLock(owners.keySet().toArray());
		lock.lock();
		try {
			fighter.choseMajorSkill(player, skill);
		} finally {
			lock.unlock();
		}
	}

	/**
	 * 设置玩家战斗回合使用的技能
	 * @param player 玩家标识
	 * @param skill 选择技能标识
	 */
	public void choseRoundSkill(Player player, String skill) {
		Fighter fighter = owners.get(player);
		if (fighter == null) {
			FormattingTuple message = MessageFormatter.format("战斗[{}]没有玩家[{}]对应的战斗单元", id, player);
			logger.error(message.getMessage());
			throw new FightException(FightExceptionCode.STATE_ERROR, message.getMessage());
		}

		ChainLock lock = LockUtils.getLock(owners.keySet().toArray());
		lock.lock();
		try {
			// TODO
			fighter.choseRoundSkill(current, skill);
		} finally {
			lock.unlock();
		}
	}

	/**
	 * 判断能否自动战斗
	 * @param player
	 */
	public boolean canAuto(Player player) {
		return true;
	}

	/**
	 * 是否已经准备就绪(玩家是否已经设置好技能)
	 * @return
	 */
	public boolean isReady() {
		if (attacker.isReady() && defender.isReady()) {
			return true;
		}
		return false;
	}

	/**
	 * 检查超时任务还是否有效
	 * @param work 超时任务
	 * @return true:有效,false:无效
	 */
	public boolean isValid(OvertimeWork work) {
		switch (work.getType()) {
		case ROUND:
			if (work.getRound() == current.getRound()) {
				return true;
			}
			break;
		case RESTORE:
			if (end && restoreOtw != null) {
				return true;
			}
		default:
			if (!end) {
				return true;
			}
			break;
		}
		return false;
	}

	/**
	 * 执行超时任务，并将过程推送给客户端
	 * @param work 超时任务
	 */
	public void process(OvertimeWork work, boolean isPush) {
		RoundReport report = null;
		switch (work.getType()) {
		case ROUND:
			report = next(null);
			if (!owners.isEmpty()) {
				Request<RoundReport> request = Request.valueOf(report);
				Set<Long> targets = new HashSet<Long>(owners.size());
				for (Player player : owners.keySet()) {
					targets.add(player.getId());
				}
				sessionManager.send(request, targets.toArray());
			}
			break;
		case RESTORE:
			if (end && restoreOtw != null) {
				restoreOvertime();
			}
			break;
		default:
			while (!end) {
				report = next(null);
			}
			if (isPush && !owners.isEmpty()) {
				Request<RoundReport> request = Request.valueOf(report);
				Set<Long> targets = new HashSet<Long>(owners.size());
				for (Player player : owners.keySet()) {
					targets.add(player.getId());
				}
				sessionManager.send(request, targets.toArray());
			}
			break;
		}
	}

	/**
	 * 获取当前回合的战斗信息
	 * @param player 当前的调用者
	 * @return
	 */
	public RoundReport next(Player player) {
		ChainLock lock = null;
		if (owners.size() > 0) {
			lock = LockUtils.getLock(owners.keySet().toArray());
			lock.lock();
		}
		try {
			// 执行回合运算并记录战报信息
			current.execute();
			RoundReport ret = current.getReport();
			report.addRound(ret);

			// 进行战斗结果判定
			if (attacker.isAllDead() && defender.isAllDead()) {
				// 双方全部死光光
				result = BattleResult.ALL_DEAD;
			} else if (attacker.isAllDead()) {
				// 攻击方死光光
				result = BattleResult.DEFENDER;
			} else if (defender.isAllDead()) {
				// 防守方死光光
				result = BattleResult.ATTACKER;
			} else if (round >= config.getMaxRound()) {
				// 最大回合数(平手)
				result = BattleResult.TIE;
			}

			// 战斗已经结束，进行战斗后回调处理
			if (result != null) {
				ret.addBattleResult(result);
				if (!config.hasNext()) {
					end();
				}
				// 战斗已经结束
				end = callback.onBattleEnd(this, ret);
				// 如果战斗没有结束，要重新补充执行ROUND_END阶段的扣除BUFF效果
				if (!end) {
					current.executeRoundEnd();
				}
			}
			if (!end) {
				// 战斗未结束，进入下一回合
				result = null;
				ret.addBattleResult(null);
				nextRound();
			} else {
				// 战斗结束添加战斗结果修正
				ret.addBattleResult(result);
				if (config.hasNext()) {
					end();
				}
			}
			return ret;
		} finally {
			if (lock != null) {
				lock.unlock();
			}
		}
	}

	/**
	 * 获取该场战斗的战斗类型
	 * @return
	 */
	public BattleType getType() {
		return config.getType();
	}

	/**
	 * 战斗结束后的后处理<br/>
	 * 目前只用于处理变身后的怒气值修正
	 */
	private void end() {
		for (Fighter fighter : new Fighter[] { attacker, defender }) {
			for (Unit unit : fighter.getOwners().values()) {
				if (unit.hasState(UnitState.TRANSFORM)) {
					unit.setValue(UnitValue.MP, 0);
				}
			}
		}
	}

	/** 战斗恢复超时处理 */
	private void restoreOvertime() {
		ChainLock lock = null;
		if (owners.size() > 0) {
			lock = LockUtils.getLock(owners.keySet().toArray());
			lock.lock();
		}
		try {
			callback.restoreTimeOut(this);
		} finally {
			if (lock != null) {
				lock.unlock();
			}
		}
	}

	/**
	 * 设置复活超时处理
	 * @param restoreOtw
	 */
	public void setRestoreOtw(OvertimeWork restoreOtw) {
		this.restoreOtw = restoreOtw;
	}

	/**
	 * 移除复活超时处理
	 */
	public void removeRestoreOtw() {
		this.restoreOtw = null;
	}

	/**
	 * 设置复活结束标识
	 * @param restoreEnd
	 */
	public void setRestoreEnd(boolean restoreEnd) {
		this.restoreEnd = restoreEnd;
	}

	// Getter and Setter ...

	public BattleConfig getConfig() {
		return config;
	}

	protected void setConfig(BattleConfig config) {
		this.config = config;
	}

	public SessionManager getSessionManager() {
		return sessionManager;
	}

	protected void setSessionManager(SessionManager sessionManager) {
		this.sessionManager = sessionManager;
	}

	public BattleCallback getCallback() {
		return callback;
	}

	protected void setCallback(BattleCallback callback) {
		this.callback = callback;
	}

	public int getId() {
		return id;
	}

	protected void setId(int id) {
		this.id = id;
	}

	public Fighter getAttacker() {
		return attacker;
	}

	protected void setAttacker(Fighter attacker) {
		this.attacker = attacker;
	}

	public Fighter getDefender() {
		return defender;
	}

	protected void setDefender(Fighter defender) {
		this.defender = defender;
	}

	public Round getCurrent() {
		return current;
	}

	protected void setCurrent(Round current) {
		this.current = current;
	}

	public int getRound() {
		return round;
	}

	protected void setRound(int round) {
		this.round = round;
	}

	public BattleReport getReport() {
		return report;
	}

	protected void setReport(BattleReport report) {
		this.report = report;
	}

	public boolean isEnd() {
		return end;
	}

	protected void setEnd(boolean end) {
		this.end = end;
	}

	public BattleResult getResult() {
		return result;
	}

	public void setResult(BattleResult result) {
		this.result = result;
	}

	protected void setOwners(Map<Player, Fighter> owners) {
		this.owners = owners;
	}

	public OvertimeWork getRestoreOtw() {
		return restoreOtw;
	}

	public boolean isRestoreEnd() {
		return restoreEnd;
	}

	protected void setOtw(OvertimeWork otw) {
		this.otw = otw;
	}

	// Static Method's ...

	/**
	 * 构造方法
	 * @param sessionManager 会话管理器
	 * @param config 战斗配置对象
	 * @param id 战斗标识
	 * @param attacker 攻击方
	 * @param defender 防守方
	 * @param callback 过程回调
	 * @return
	 */
	public static Battle valueOf(SessionManager sessionManager, BattleConfig config, int id, Fighter attacker,
			Fighter defender, BattleCallback callback) {
		return new Battle(sessionManager, config, id, attacker, defender, callback);
	}

}
