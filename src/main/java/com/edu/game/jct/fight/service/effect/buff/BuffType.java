package com.edu.game.jct.fight.service.effect.buff;

/**
 * BUFF类型
 * @author Frank
 */
public enum BuffType {

	/** 重复型:多次修改战斗单位数值 */
	REPEAT,
	/** 单次型:一次性修改战斗单位数值 */
	ONCE,
	/** 定时型:在BUFF移除时一次性修改战斗单位数值 */
	TIME,
	/** 被动效果型:给目标添加被动效果 */
	PASSIVE,
	/** 状态型:添加时修改状态,移除时恢复状态(会检查状态免疫) */
	STATE;

}
