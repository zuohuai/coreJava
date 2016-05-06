package com.eyu.snm.module.fight.service.core;

/**
 * 战斗阶段定义
 * @author Frank
 */
public enum Stage {

	/** 战斗创建 */
	BATTLE_CREATE(false),

	/** 修正阶段 */
	CORRECT(false),

	/** 回合开始 */
	ROUND_START(true),

	/** 角色死亡阶段 */
	UNIT_DEAD(true),

	/** 回合结束 */
	ROUND_OVER(true),

	/** 战斗结束 */
	BATTLE_OVER(false);

	/** 是否战斗中执行 */
	private boolean isInBattle;

	private Stage(boolean isInBattle) {
		this.isInBattle = isInBattle;
	}

	public boolean isInBattle() {
		return isInBattle;
	}

}
