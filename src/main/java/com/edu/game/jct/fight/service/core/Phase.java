package com.edu.game.jct.fight.service.core;

/**
 * 效果执行的阶段
 * @author Administrator
 *
 */
public enum Phase {
	/**
	 * 回合开始阶段(该阶段用于执行回合技能,先攻击方到防御方)
	 * */
	ROUND_START,
	/**
	 * 行动:调整
	 * 生成效果:被动技能(行动发起者)
	 */
	ACTION_AJUST,
	
	/**
	 * 行动:防御
	 * 生成效果:被动技能(行动承受者)
	 */
	ACTION_DEFENCE,
	
	/**
	 * 行动:结束
	 * 生成效果:被动技能(行动发起者)
	 */
	ACTION_END,
	
	/**
	 * 回合结束阶段/BUFF运算阶段(按出手顺序进行战斗单元上的Buff运算)
	 */
	ROUND_END
}
