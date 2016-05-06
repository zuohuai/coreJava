package com.edu.game.dota.fight.model;

/**
 * 战斗单位的数值属性
 * @author Frank
 */
public enum UnitValue {

	/** 生命0 */
	HP(AlterType.HP, true),
	/** 生命上限1 */
	HP_MAX(AlterType.HP_MAX, true),
	/** 灵力/怒气2 */
	MP(AlterType.MP, true),
	/** 攻击3 */
	ATTACK(AlterType.ATTACK, false),
	/** 攻击速度4 */
	ATTACK_SPEED(AlterType.ATTACK_SPEED, false),
	/** 防御5 */
	DEFENSE(AlterType.DEFENSE, false),
	/** 行动间隔6 */
	INTERVAL(AlterType.INTERVAL, false),
	/** 移动间隔 7 */
	MOVE(AlterType.MOVE, false),
	/** 暴击8 */
	CRIT(AlterType.CRIT, false),
	/** 暴击伤害9 */
	CRIT_HARM(AlterType.CRIT_HARM, false),
	/** 抗暴击10 */
	UNCRIT(AlterType.UNCRIT, false),
	/** 抗暴击伤害11 */
	UNCRIT_HARM(AlterType.UNCRIT_HARM, false),
	/** 命中12 */
	HIT(AlterType.HIT, false),
	/** 闪避13 */
	DODGY(AlterType.DODGY, false),
	/** 硬直时间14 */
	HR_TIME(AlterType.HR_TIME, false),
	/** 霸体15 */
	UNHR(AlterType.UNHR, false),
	/** 霸体保护时间16 */
	UNHR_TIME(AlterType.UNHR_TIME, false),
	/** 异常抗性17 */
	CR(AlterType.CR, false),
	/** 抗性穿透18 */
	UNCR(AlterType.UNCR, false),
	/** 伤害率19 */
	HARM(AlterType.HARM, false),
	/** 免伤率20 */
	UNHARM(AlterType.UNHARM, false),
	/** 暴击能量加成21 */
	MP_PLUS(AlterType.MP_PLUS, false),
	/** 星级系数22 */
	STAR_RADIO(AlterType.STAR_RADIO, false),
	/** 超必杀伤害加成 23 */
	SSA(AlterType.SSA, false),
	/** 普通攻击伤害加成24 */
	CAA(AlterType.CAA, false),
	/** 技能攻击伤害加成25 */
	SA(AlterType.SA, false),
	/** 动作时间修正系数 26 */
	NT_EFFECT(AlterType.NT_EFFECT, true),
	/** 移动时间修正系数 27 */
	MOVE_EFFECT(AlterType.MOVE_EFFECT, true);

	/** 对应的值变更类型 */
	private AlterType alter;
	private boolean reportable;

	private UnitValue(AlterType alter, boolean reportable) {
		this.alter = alter;
		this.reportable = reportable;
	}

	public AlterType toAlterType() {
		return alter;
	}

	public boolean isReportable() {
		return reportable;
	}

}
