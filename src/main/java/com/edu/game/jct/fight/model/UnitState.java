package com.edu.game.jct.fight.model;
public interface UnitState {

	/** 正常状态(0) */
	int NORMAL = 0;

	/** 死亡(1) */
	int DEAD = 1 << 0;

	/** 隐身/无法被选择(2) */
	int UNVISUAL = 1 << 1;
	
	/** 变身(4) */
	int TRANSFORM = 1 << 2;

	/** 禁止行动/晕(16) */
	int DISABLE = 1 << 4;

	/** 免疫:禁止行动(32) */
	int IMMUNE_DISABLE = 1 << 5;
	
	/** 混乱/反选主动技能目标(64) */
	int CHAOS = 1 << 6;

	/** 免疫:混乱/反选主动技能目标(128) */
	int IMMUNE_CHAOS = 1 << 7;
	
}