package com.edu.game.jct.fight.model;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import com.edu.game.jct.fight.service.alter.Alter;
import com.edu.game.jct.fight.service.alter.DegreeAlter;
import com.edu.game.jct.fight.service.alter.LifeAlter;
import com.edu.game.jct.fight.service.alter.MpInitAlter;
import com.edu.game.jct.fight.service.alter.RateAlter;
import com.edu.game.jct.fight.service.alter.StateAlter;
import com.edu.game.jct.fight.service.alter.TempValueAlter;
import com.edu.game.jct.fight.service.alter.UnitValueAlter;

/**
 * 值修改类型
 * @author administrator
 */
@SuppressWarnings("rawtypes")
public enum AlterType {

	// 数值属性

	/** 生命 */
	HP("HP", new UnitValueAlter(UnitValue.HP), 1),
	/** 生命上限 */
	HP_MAX("HP_MAX", new UnitValueAlter(UnitValue.HP_MAX), 1),
	/** 生命(最大值与生命数值) */
	LIFE("LIFE", new LifeAlter(), 1),
	/** 怒气 */
	MP("MP", new UnitValueAlter(UnitValue.MP), 1),
	/** 怒气上限 */
	MP_MAX("MP_MAX", new UnitValueAlter(UnitValue.MP_MAX), 1),
	/** 初始化怒气(只能战斗开始前使用) */
	MP_INIT("MP_INIT", new MpInitAlter(), 1),
	/** 怒气增长附加 */
	MP_ADD("MP_ADD", new UnitValueAlter(UnitValue.MP_ADD), 1),
	/** 护盾 */
	SHIELD("SHIELD", new UnitValueAlter(UnitValue.SHIELD), 1),
	/** 攻击:物理 */
	ATTACK_P("ATTACK_P", new UnitValueAlter(UnitValue.ATTACK_P), 1),
	/** 攻击:法术 */
	ATTACK_M("ATTAKC_M", new UnitValueAlter(UnitValue.ATTACK_M), 1),
	/** 速度 */
	SPEED("SPEED", new UnitValueAlter(UnitValue.SPEED), 1),
	/** 状态 */
	STATE("STATE", new StateAlter(), 1),
	/** 防御:物理 */
	DEFENCE_P("DEFENCE_P", new TempValueAlter(), 1),
	/** 防御:法术 */
	DEFENCE_M("DEFENCE_M", new TempValueAlter(), 1),

	// 比率属性(累加关系)

	/** 防御:物理 */
	RATE_DEFENCE_P("RATE:" + UnitRate.DEFENCE_P, new RateAlter(UnitRate.DEFENCE_P), 1),
	/** 防御:法术 */
	RATE_DEFENCE_M("RATE:" + UnitRate.DEFENCE_M, new RateAlter(UnitRate.DEFENCE_M), 1),
	/** 命中 */
	RATE_HIT("RATE:" + UnitRate.HIT, new RateAlter(UnitRate.HIT), 1),
	/** 闪避 */
	RATE_DODGY("RATE:" + UnitRate.DODGY, new RateAlter(UnitRate.DODGY), 1),
	/** 暴击 */
	RATE_CRIT("RATE:" + UnitRate.CRIT, new RateAlter(UnitRate.CRIT), 1),
	/** 免暴击 */
	RATE_UNCRIT("RATE:" + UnitRate.UNCRIT, new RateAlter(UnitRate.UNCRIT), 1),
	/** 伤害:暴击 */
	RATE_HURT_CRIT("RATE:" + UnitRate.HURT_CRIT, new RateAlter(UnitRate.HURT_CRIT), 1),
	/** 破击 */
	RATE_FATAL("RATE:" + UnitRate.FATAL, new RateAlter(UnitRate.FATAL), 1),
	/** 免破击 */
	RATE_UNFATAL("RATE:" + UnitRate.UNFATAL, new RateAlter(UnitRate.UNFATAL), 1),
	/** 格挡 */
	RATE_BLOCK("RATE:" + UnitRate.BLOCK, new RateAlter(UnitRate.BLOCK), 1),
	/** 免格挡 */
	RATE_UNBLOCK("RATE:" + UnitRate.UNBLOCK, new RateAlter(UnitRate.UNBLOCK), 1),
	/** 格挡率 */
	RATE_HURT_BLOCK("RATE:" + UnitRate.HURT_BLOCK, new RateAlter(UnitRate.HURT_BLOCK), 1),
	/** 伤害率 */
	RATE_HARM("RATE:" + UnitRate.HARM, new RateAlter(UnitRate.HARM), 1),
	/** 免伤率 */
	RATE_UNHARM("RATE:" + UnitRate.UNHARM, new RateAlter(UnitRate.UNHARM), 1),
	/** 昏迷率 */
	RATE_DISABLE("RATE:" + UnitRate.DISABLE, new RateAlter(UnitRate.DISABLE), 1),
	/** 抗昏迷率 */
	RATE_UNDISABLE("RATE:" + UnitRate.UNDISABLE, new RateAlter(UnitRate.UNDISABLE), 1),
	/** 混乱率 */
	RATE_CHAOS("RATE:" + UnitRate.CHAOS, new RateAlter(UnitRate.CHAOS), 1),
	/** 抗混乱率 */
	RATE_UNCHAOS("RATE:" + UnitRate.UNCHAOS, new RateAlter(UnitRate.UNCHAOS), 1),
	/** 迷糊率 */
	RATE_FUZZY("RATE:" + UnitRate.FUZZY, new RateAlter(UnitRate.FUZZY), 1),

	// 比率属性(乘除关系)

	/** 伤害率 */
	DEGREE_HARM("DEGREE:" + UnitDegree.HARM, new DegreeAlter(UnitDegree.HARM), 1),
	/** 免伤率 */
	DEGREE_UNHARM("DEGREE:" + UnitDegree.UNHARM, new DegreeAlter(UnitDegree.UNHARM), 1),
	/** 治疗率 */
	DEGREE_CURE("DEGREE:" + UnitDegree.CURE, new DegreeAlter(UnitDegree.CURE), 1),
	/** 降治疗率 */
	DEGREE_UNCURE("DEGREE:" + UnitDegree.UNCURE, new DegreeAlter(UnitDegree.UNCURE), 1),
	/** 恢复率 */
	DEGREE_HEALING("DEGREE:" + UnitDegree.HEALING, new DegreeAlter(UnitDegree.HEALING), 1),
	/** 降恢复率 */
	DEGREE_UNHEALING("DEGREE:" + UnitDegree.UNHEALING, new DegreeAlter(UnitDegree.UNHEALING), 1);

	private static Map<String, AlterType> map = new HashMap<String, AlterType>(AlterType.values().length);

	static {
		for (AlterType type : AlterType.values()) {
			map.put(type.getKey(), type);
		}
	}

	/** 优先级比较器(由高到低) */
	public static final Comparator<AlterType> comparator = new Comparator<AlterType>() {
		@Override
		public int compare(AlterType o1, AlterType o2) {
			return o2.priority - o1.priority;
		}
	};

	/** 构造方法 */
	private AlterType(String key, Alter alter, int priority) {
		this.key = key;
		this.alter = alter;
		this.priority = priority;
	}

	/** 属性键(客户端要求与战斗单元属性对应) */
	private final String key;
	/** 修改器 */
	private final Alter alter;
	/** 优先级(属性作用到战斗单位上的顺序,优先级越高越先执行) */
	private final int priority;

	public Alter getAlter() {
		return alter;
	}

	public String getKey() {
		return key;
	}

	public int getPriority() {
		return priority;
	}

	public Object toValue(String value) {
		return alter.toValue(value);
	}

	// Static Method's ...

	/** 按{@link AlterType#getKey()}获取指定的值修改类型 */
	public static AlterType getAlterType(String key) {
		return map.get(key);
	}

}
