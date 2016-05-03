package com.edu.game.jct.fight.model;

/**
 * 战斗战斗单元比率属性(累加关系)
 * @author Administrator
 *
 */
public enum UnitRate {
	/**物理攻击*/
	DEFENCE_P,
	/**魔法攻击*/
	DEFENCE_M,
	/**命中*/
	HIT,
	/**闪避*/
	DODGY,
	/**暴击*/
	CRIT,
	/**免暴*/
	UN_CRIT,
	/**暴击伤害*/
	HURT_CRIT,
	/**破击*/
	FATAL,
	/**免破击*/
	UNFATAL,
	/**格挡*/
	BLOCK,
	/**免格挡*/
	UNBLOCK,
	/**格挡率*/
	HURT_BLOCK,
	/**伤害率*/
	HARM,
	/**免伤率*/
	UNHARM,
	/**昏迷率*/
	DISABLE,
	/**抗昏迷率*/
	UNDISABLE,
	/**混乱率*/
	CHAOS,
	/**抗混乱率*/
	UN_CHAOS,
	/**迷糊率*/
	FUZZY
}
