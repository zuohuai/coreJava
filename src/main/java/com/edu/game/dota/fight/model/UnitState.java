package com.edu.game.dota.fight.model;

/**
 * 战斗单位的状态定义
 * @author frank
 */
public interface UnitState {

	// 特殊状态
	/** 禁止施放大招 */
	int BIG_FORBID = 0b0010_1010_1000_0000;

	// 基本状态

	/** 存活(0):战斗单位还活着 */
	int NORMAL = 0;

	/** 死亡(1):战斗单位已经死亡 */
	int DEAD = 1 << 0;

	// 附加状态

	/** 隐身(32):战斗单位不会成为其它战斗单位的行动目标,但自己还能继续行动 */
	int UNVISUAL = 1 << 5;

	/** 混乱(128):战斗单位会攻击自身队友 */
	int CHAOS = 1 << 7;

	/** 免疫混乱(256):战斗单位能免疫混乱效果 */
	int IMMUNE_CHAOS = 1 << 8;

	/** 禁魔(512):战斗单位不能施放大招 */
	int FORBID = 1 << 9;

	/** 免疫禁魔(1024):战斗单位能免疫禁魔效果 */
	int IMMUNE_FORBID = 1 << 10;

	/** 禁止行动(2048):战斗单位无法行动 */
	int DISABLE = 1 << 11;

	/** 免疫禁止行动(4096):战斗单位不会被禁止行动 */
	int IMMUNE_DISABLE = 1 << 12;

	/** 禁止攻击(8192) */
	int FORBID_ATTACK = 1 << 13;

}
