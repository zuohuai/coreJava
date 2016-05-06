package com.eyu.snm.module.fight.service.core;

import java.util.Date;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.eyu.common.utils.json.JsonUtils;
import com.eyu.common.utils.time.DateUtils;
import com.eyu.snm.module.fight.exception.FightException;
import com.eyu.snm.module.fight.exception.FightExceptionCode;
import com.eyu.snm.module.fight.model.BattleResult;
import com.eyu.snm.module.fight.model.ResultType;
import com.eyu.snm.module.fight.model.VerifyResult;
import com.eyu.snm.module.fight.model.report.ActionReport;
import com.eyu.snm.module.fight.model.report.Report;
import com.eyu.snm.module.fight.model.report.RoundReport;
import com.eyu.snm.module.fight.model.report.StageReport;
import com.eyu.snm.module.fight.model.report.seed.OperationData;
import com.eyu.snm.module.fight.model.report.seed.Random;
import com.eyu.snm.module.fight.resource.IdHolder;
import com.eyu.snm.module.fight.service.BattleType;
import com.eyu.snm.module.fight.service.RandomCallBack;
import com.eyu.snm.module.fight.service.action.Action;
import com.eyu.snm.module.fight.service.config.BattleConfig;
import com.eyu.snm.module.fight.service.effect.init.InitSkillEffector;
import com.eyu.snm.module.fight.service.op.ActionOperation;
import com.eyu.snm.module.fight.service.op.Operation;
import com.eyu.snm.module.fight.service.op.TimeOutOperation;
import com.eyu.snm.module.fight.service.op.UniqueOperation;

/**
 * 战斗场景对象<br/>
 * 该对象作为整场战斗的容器，负责承载和管理整场战斗内的全部对象
 * @author Frank
 */
public class Battle {

	private static final Logger logger = LoggerFactory.getLogger(Battle.class);

	/** 标识 */
	private int id;
	/** 类型 */
	private BattleType type;
	/** 配置对象 */
	private BattleConfig config;
	/** 战斗结果 */
	private BattleResult battleResult;
	/** 回合战斗结果 */
	private ResultType roundResult;
	/** 战报信息 */
	private Report report;

	/** 攻击方列表 */
	private List<Fighter> attackers;
	/** 防守方列表 */
	private List<Fighter> defenders;

	/** 回合战斗中 */
	private boolean fighting;
	/** 回合计数器 */
	private int round;

	/** 当前回合的战场区域 */
	private Area area;
	/** 当前回合的攻击方 */
	private Fighter attacker;
	/** 当前回合的防守方 */
	private Fighter defender;

	/** 战斗的持续时长(毫秒) */
	private long duration = System.currentTimeMillis();
	/** 回合初始时间(毫秒) */
	private long roundInitTime;
	/** 累计大招黑屏时间,客户端如果在黑屏时间杀光所有对手也需在黑屏结束后离场 */
	private int blackTime;

	/** 操作队列 */
	private SortedSet<Operation> operations = new TreeSet<Operation>();
	/** 随机种子 */
	private Random random;
	/** 随机种子战斗回调 */
	private RandomCallBack randomCallBack;

	/** 战场元素标识生成器 */
	private short idGenerator = Constant.BATTLE_MAX + 1;
	/** fighter的标识生成器 */
	private AtomicInteger idBuilder = new AtomicInteger();
	/** 获取下一个op ID */
	private AtomicInteger opIdBuilder = new AtomicInteger();
	/** 战报编码映射器 */
	private IdHolder holder;

	/** 手操数据 */
	private List<OperationData>[] opDatas;

	/** 获取最快发生的操作 */
	public Operation getSoonOp() {
		// 回合战斗进行中，返回行动操作
		if (fighting) {
			return operations.first();
		}
		// 回合战斗已经结束，但还有后续回合，返回回合开始操作
		if (isOver()) {
			// 生成战斗结果战报信息
			report.setResult(battleResult.getResult());
		} else {
			return new Operation() {
				private long timing = System.currentTimeMillis();

				@Override
				public long getTiming() {
					return timing;
				}

				@Override
				public Operation execute(Battle battle) {
					doRoundStart();
					fighting = true;
					roundResult = null;
					return getSoonOp();
				}

				@Override
				public int compareTo(Operation o) {
					return (int) (timing - o.getTiming());
				}

				@Override
				public Element getOwner() {
					return null;
				}

				@Override
				public String toString() {
					return "Openning[" + DateUtils.date2String(new Date(timing), "mm:ss.SSS") + "]";
				}

				@Override
				public int getId() {
					return getNextOpId();
				}

			};
		}
		// 整场战斗已经结束，返回 null
		return null;
	}

	/** 回合开始的逻辑处理 */
	protected void doRoundStart() {
		// 确认回合开始时间
		roundInitTime = duration;

		// 战斗超时的攻击方和防守方处理通过超时处理进行，不在回合开始中进行处理
		// 重置战场
		config.reset(this);

		// 重置阶段结果
		roundResult = null;

		// 清空战场并重置战斗单位出场位置
		area = Area.valueOf(this, attacker, defender);

		// ---------- 初始化修正阶段
		// 攻击方所有需要生效的效果
		InitSkillEffector.effect(attacker, round);
		// 防御方所有需要生效的效果
		InitSkillEffector.effect(defender, round);

		// 初始化操作队列
		doInitialize();

		// 修正回合计数器
		round++;

		// 生成回合初始化战报信息
		RoundReport rr = RoundReport.valueOf(attacker, defender, report, holder);
		report.addRoundReport(rr);
		if (logger.isDebugEnabled()) {
			logger.debug(JsonUtils.object2String(rr));
		}
	}

	/**
	 * 获取反方的战斗单位组
	 * @param fighter 攻击方或防守方的战斗单位组
	 * @return
	 */
	public Fighter getOpposite(Fighter fighter) {
		if (attacker == fighter) {
			return defender;
		}
		if (defender == fighter) {
			return attacker;
		}
		throw new IllegalArgumentException("战斗单位组不是当前的攻击方或防守方");
	}

	/**
	 * 执行战斗中要被触发的行为<br/>
	 * 该方法假设战斗中的行为触发是有序的被执行
	 * @param operation 当前时间点触发的行为
	 * @return
	 */
	public OperationResult execute(ActionOperation operation) {

		ActionReport ar = null;

		// 更新战斗时长和移除被执行的操作对象
		updateTime(operation);
		operations.remove(operation);

		// 检查行动所有者是否有效
		Element owner = operation.getOwner();
		if (!owner.isValid(this)) {
			return OperationResult.valueOf(ar, getSoonOp());
		}

		// 获取行动对应的具体行为
		Action act = owner.getAction(duration, this);
		if (act != null) {
			// 执行行为并返回战报
			ar = act.execute();
			// 如果是阶段技能战报则需要判断回合是否结束
			if (ar instanceof StageReport) {
				if (isRoundOver()) {
					fighting = false;
					// 记录回合战斗结果
					RoundReport last = report.lastRoundReport();
					if (last != null) {
						last.setResult(roundResult);
					}
				}
			}
		}

		// 获取行为所有者的下一个行动
		Operation next = operation.getOwner().getNextOp();

		if (next != null) {
			operations.add(next);
		}

		// 返回行动结果
		return OperationResult.valueOf(ar, getSoonOp());
	}

	public void removeOperation(Operation op) {
		operations.remove(op);
	}

	public void addOperation(Operation op) {
		operations.add(op);
	}

	/**
	 * 检查战斗是否已经结束
	 * @return
	 */
	public boolean isOver() {

		boolean result = config.judgeResult(this, battleResult);
		if (result) {
			battleOver();
		}
		return result;
	}

	/**
	 * 战斗回合是否结束
	 * @param round 对应的回合数
	 * @return
	 */
	public boolean isRoundOver() {
		if (attacker.isEmpty() && defender.isEmpty()) {
			roundResult = ResultType.ALL_DEAD;
		} else if (attacker.isEmpty()) {
			roundResult = ResultType.DEFENDER;
			defender.win();
		} else if (defender.isEmpty()) {
			roundResult = ResultType.ATTACKER;
			attacker.win();
		}

		if (roundResult == null) {
			return false;
		}
		// 加离场时间
		duration += Constant.LEAVE_TIME;
		return true;
	}

	/** 战斗结束 */
	public void battleOver() {
		// 战斗单元ID解码
		fighterDecode();
		// 做黑屏时间修正
		duration += blackTime;
	}

	/**
	 * 返回战斗结果
	 * @return
	 */
	public BattleResult getBattleResult() {
		return battleResult;
	}

	/**
	 * 获取战场元素的唯一标识<br>
	 * @return 唯一标识
	 */
	public short getNextId() {
		return idGenerator++;
	}

	/**
	 * 获取战斗进行了的时间
	 * @return
	 */
	public int getRelateTime() {
		return (int) (duration - report.getTiming());
	}

	/**
	 * 获取回合进行了的时间
	 * @return
	 */
	protected int getRoundTime() {
		return (int) (duration - roundInitTime);
	}

	/** 配置战场时间 */
	public void updateTime(Operation operation) {
		duration = operation.getTiming();
	}

	/** 修正大招黑屏时间 */
	public void updateBlackTime(Skill unique) {
		// 判断是否有黑屏时间
		Integer blackTime = unique.getBigSkillTime();
		if (blackTime == null) {
			return;
		}
		this.blackTime += blackTime;
	}

	public void timeOut() {
		config.timeOut(this);
		battleOver();
	}

	protected void reset() {
		// 攻击方修正,重置上一场战斗单元的数据,移除死亡单位
		if (attacker != null) {
			attacker.reset();
		}
		if (attacker == null || attacker.isEmpty()) {
			attacker = attackers.get(0);
			attackers.remove(0);
		}

		if (defender != null) {
			defender.reset();
		}
		if (defender == null || defender.isEmpty()) {
			defender = defenders.get(0);
			defenders.remove(0);
		}

		// 第一回合进行死亡单位清理(目前国战需要战斗结束时死亡单位的数据,单人副本,古墓等会直接沿用上一场的fighter会有死亡单位需要移除)
		// if (round == 0) {
		// attacker.reset();
		// defender.reset();
		// }

	}

	// 内部方法

	/** 初始化方法 */
	private void doInitialize() {
		operations = new TreeSet<Operation>();
		operations.add(TimeOutOperation.valueOf(duration + config.getBattleOvertime(), getNextOpId()));
		// 初始化战斗单元OP
		for (Unit u : attacker.getUnits()) {
			initUnitOp(u);
		}
		for (Unit u : defender.getUnits()) {
			initUnitOp(u);
		}

		// 无手操战斗
		if (opDatas == null) {
			return;
		}
		if (round >= opDatas.length) {
			throw new FightException(FightExceptionCode.JUSTIFY_FAILED);
		}
		List<OperationData> roundOpdata = opDatas[round];
		if (roundOpdata == null) {
			return;
		}
		// 插入手操大招op
		for (OperationData data : roundOpdata) {
			UniqueOperation uop = UniqueOperation.valueOf(data.getId(), data.getTime() + report.getTiming(), getNextOpId());
			operations.add(uop);
		}
	}

	/** 初始化战斗单元OP */
	private void initUnitOp(Unit u) {
		if (!u.isDead()) {
			operations.add(u.getNextOp());
		}
	}

	/** 战斗单元id转换 */
	private void fighterDecode() {
		attacker.decode();
		defender.decode();
		for (Fighter fighter : attackers) {
			fighter.decode();
		}
		for (Fighter fighter : defenders) {
			fighter.decode();
		}
	}

	// 手操
	public void insert(List<OperationData>[] opData) {
		this.opDatas = opData;
	}

	public OperationResult executeUnique(UniqueOperation operation) {
		operations.remove(operation);
		if (operation.getTiming() <= roundInitTime) {
			// throw new FightException(FightExceptionCode.JUSTIFY_FAILED); 如果直接判定认证失败,不太友好可以默认跳过
			logger.error("操作数据不合法!回合开始时间[{}]~~现在[{}]", roundInitTime, operation.getTiming());
			return OperationResult.valueOf(null, getSoonOp());
		}
		updateTime(operation);
		ActionReport ar = null;
		Unit unit = findUnit(operation.getUnitId());
		Action action = unit.getUniqueAction(operation.getTiming(), this);
		if (action == null) {
			return OperationResult.valueOf(ar, getSoonOp());
		}
		ar = action.execute();
		if (isRoundOver()) {
			fighting = false;
		}
		// 获取行为所有者的下一个行动
		Operation next = unit.getNextOp();
		if (next != null) {
			operations.add(next);
		}
		return OperationResult.valueOf(ar, getSoonOp());
	}

	public Unit findUnit(short unitId) {
		if (unitId > Constant.BATTLE_MAX / 2) {
			return defender.getUnit(unitId);
		}
		return attacker.getUnit(unitId);
	}

	public int getSeed() {
		return random.getSeed();
	}

	// Getter and Setter ...

	/** 获取随机种子 */
	public Random getRandom() {
		return random;
	}

	/** 获取战斗验证结果 */
	public VerifyResult getResult() {
		return randomCallBack.backResult(this);
	}

	/** 未手到客户端手操请求处理 */
	public void verifyTimeOut() {
		randomCallBack.timeOut(this);
	}

	public RandomCallBack getRandomCallBack() {
		return randomCallBack;
	}

	/** 设置随机种子战斗回调 */
	public void setRandomCallBack(RandomCallBack randomCallBack) {
		this.randomCallBack = randomCallBack;
	}

	public int getNextFighterId() {
		return idBuilder.getAndIncrement();
	}

	/** 获取下一个操作OP id */
	public int getNextOpId() {
		return opIdBuilder.getAndIncrement();
	}

	// Getter and Setter ...

	public int getId() {
		return id;
	}

	public BattleType getType() {
		return type;
	}

	public BattleConfig getConfig() {
		return config;
	}

	public int getRound() {
		return round;
	}

	public Report getReport() {
		return report;
	}

	public Area getArea() {
		return area;
	}

	public Fighter getAttacker() {
		return attacker;
	}

	/** 配置战斗攻击方 */
	public void setAttacker(Fighter attacker) {
		this.attacker = attacker;
	}

	public Fighter getDefender() {
		return defender;
	}

	/** 配置战斗防守方 */
	public void setDefender(Fighter defender) {
		this.defender = defender;
	}

	public long getDuration() {
		return duration;
	}

	public List<Fighter> getAttackers() {
		return attackers;
	}

	public List<Fighter> getDefenders() {
		return defenders;
	}

	public List<OperationData>[] getOpDatas() {
		return opDatas;
	}

	public void setOpDatas(List<OperationData>[] opDatas) {
		this.opDatas = opDatas;
	}

	public long getRoundInitTime() {
		return roundInitTime;
	}

	public void setRoundInitTime(long roundInitTime) {
		this.roundInitTime = roundInitTime;
	}

	public static Battle valueOf(List<Fighter> attackers, List<Fighter> defenders, BattleConfig config, IdHolder idHolder) {
		Battle battle = new Battle();
		battle.config = config;
		battle.type = config.getType();
		battle.attackers = attackers;
		battle.defenders = defenders;
		battle.battleResult = BattleResult.valueOf(attackers, defenders);
		battle.report = Report.valueOf(config, battle.getDuration());
		battle.random = new Random(System.currentTimeMillis(), battle);
		battle.holder = idHolder;
		return battle;
	}
}
