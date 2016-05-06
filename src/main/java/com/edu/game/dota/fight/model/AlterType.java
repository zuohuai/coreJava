package com.edu.game.dota.fight.model;

/**
 * 值修改类型
 * @author Kent
 */
public enum AlterType {

	/** 生命0 */
	HP(true),
	/** 生命上限1 */
	HP_MAX(true),
	/** 灵力/怒气2 */
	MP(true),
	/** 攻击 3 */
	ATTACK(true),
	/** 攻击速度 4 */
	ATTACK_SPEED(false),
	/** 防御 5 */
	DEFENSE(true),
	/** 行动间隔 6 */
	INTERVAL(false),
	/** 移动间隔 7 */
	MOVE(false),
	/** 暴击 8 */
	CRIT(false),
	/** 暴击伤害 9 */
	CRIT_HARM(false),
	/** 抗暴击 10 */
	UNCRIT(false),
	/** 抗暴击伤害 11 */
	UNCRIT_HARM(false),
	/** 命中 12 */
	HIT(false),
	/** 闪避 13 */
	DODGY(false),
	/** 硬直时间 14 */
	HR_TIME(false),
	/** 霸体 15 */
	UNHR(false),
	/** 霸体保护时间 16 */
	UNHR_TIME(false),
	/** 异常抗性 17 */
	CR(false),
	/** 抗性穿透 18 */
	UNCR(false),
	/** 伤害率 19 */
	HARM(false),
	/** 免伤率 20 */
	UNHARM(false),
	/** 暴击能量加成 21 */
	MP_PLUS(false),
	/** 战斗单元BUFF 22 */
	UNIT_BUFF(true),
	/** 驱散战斗单元BUFF 23 */
	UNIT_UNBUFF(true),
	/** 场景BUFF 24 */
	AREA_BUFF(true),
	/** 驱散场景BUFF 25 */
	AREA_UNBUFF(true),
	/** 位置变化 26 */
	POSISTION(true),
	/** 状态 27 */
	STATE(true),
	/** 受击状态 28 */
	DAMAGE_STATE(true),
	/** 星级系数 29 */
	STAR_RADIO(false),
	/** 超必杀伤害加成 30 */
	SSA(false),
	/** 普通攻击伤害加成 31 */
	CAA(false),
	/** 技能攻击伤害加成 32 */
	SA(false),
	/** 动作时间修正系数 33 */
	NT_EFFECT(true),
	/** 移动时间修正系数 34 */
	MOVE_EFFECT(true);

	/** 是否作为战报输出 */
	private boolean reportable;

	private AlterType(boolean reportable) {
		this.reportable = reportable;
	}

	public boolean isReportable() {
		return reportable;
	}

}
