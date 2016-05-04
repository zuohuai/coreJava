package com.edu.game.jct.fight.model.report;


/**
 * 技能效果的施放状态
 * @author Frank
 */
public interface EffectState {

	/** 正常状态(0) */
	int NORMAL = 0;

	/** 躲闪(1) */
	int DODGY = 1 << 0;

	/** 暴击(2) */
	int CRIT = 1 << 1;

	/** 破击(4) */
	int FATAL = 1 << 2;
	
	/** 失败(8) */
	int FAIL = 1 << 3;

}
