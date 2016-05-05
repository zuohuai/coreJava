package com.edu.game.jct.fight.service.effect.skill;

/**
 * 主动技能效果类型
 * @author Frank
 */
public enum EffectType {

	/** HP物理伤害 */
	HP_P_DAMAGE,
	/** HP法术伤害 */
	HP_M_DAMAGE,
	/** 属性伤害 */
	VALUES_DAMAGE,
	/** HP回复 */
	HP_RECOVER,
	/** 施放BUFF */
	BUFF_CAST,
	/** 驱散BUFF */
	BUFF_DISPEL,
	/** 复活 */
	REVIVE,
	/** 召唤 */
	SUMMON,
	/** 分身 */
	CLONE,
	/** 组合效果 */
	COMBINATION,
	/** 清除CD */
	CD_CLEAR;

}
