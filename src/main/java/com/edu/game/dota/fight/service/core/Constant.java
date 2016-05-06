package com.eyu.snm.module.fight.service.core;

/**
 * 常量定义信息
 * @author Frank
 */
public interface Constant {

	/** 怒气值上限 */
	int MP_MAX = 100;

	/** 一支队伍的最大人数 */
	int MEMBER = 5;

	/** 一场战斗初始最大人数(以攻防守方各十支部队计) */
	short BATTLE_MAX = 100;

	/** 第一个人入场时间 */
	int FIRST_ENTER = 500;

	/** 间隔入场时间 */
	int INTERVAL_ENTER = 500;

	/** 离场时间 */
	int LEAVE_TIME = 3000;

	/** 入场最快开大时间 */
	int UNIQUE_TIME = 5000;

	/** 随机比较数最大值 */
	int RANDOM_COMPARE = 100;

	/** 动作时间修正系数最大值 */
	int NT_EFFECT_MAX = 90;

	/** 动作时间修正系数最小值 */
	int NT_EFFECT_MIN = -500;

	/** 移动时间修正系数最大值 */
	int MOVE_EFFECT_MAX = 90;

	/** 移动时间修正系数最小值 */
	int MOVE_EFFECT_MIN = -100;
}
