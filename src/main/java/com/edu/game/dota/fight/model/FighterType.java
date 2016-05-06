package com.edu.game.dota.fight.model;

/**
 * @author Kent
 */
public enum FighterType {

	/** 测试战斗对象类型 */
	MOCK,

	// 通用

	/** 玩家PVP */
	PLAYER_PVP,
	/** NPC PVP */
	NPC_PVP,
	/** NPC根据参数修正属性 */
	NPC_LEVEL_PVP,
	/** 多玩家PVP */
	PLAYERS_PVP,

	// 特殊玩法

	/** 剧本战役 */
	SCRIPT,
	/** 古墓战役 */
	TOMB,
	/** 军团模块 */
	CORPS,
	/** 官员弹劾 */
	OFFICER,
	/** 国战部队 */
	WAR_ARMY,
	/** 推图(追加指定武将) */
	PLAYER_RECLAIM_APPEND,
	/** 推图(主角+配置武将) */
	PLAYER_RECLAIM_REPLACE;

}
