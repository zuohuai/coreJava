package com.edu.game.jct.fight.service.effect.select;

/**
 * 目标选择类型定义
 * @author administrator
 */
public enum SelectType {
	// 敌方

	/** 敌方任意单体 */
	ENEMY_ONE,
	/** 敌方全体 */
	ENEMY_ALL,
	/** 敌方前排单体 */
	ENEMY_FRONT_ONE,
	/** 敌方前排全体 */
	ENEMY_FRONT_ALL,
	/** 敌方后排单体 */
	ENEMY_BACK_ONE,
	/** 敌方后排全体 */
	ENEMY_BACK_ALL,
	/** 敌方竖排双目标(从同排人数多到少，人数相同就进行随机) */
	ENEMY_ROW,
	/** 敌方生命最少的目标 */
	ENEMY_HP_MIN,
	/** 敌方生命最少的两个目标 */
	ENEMY_HP_TWO,
	/** 敌方生命最多的目标 */
	ENEMY_HP_MAX,
	/** 敌方生命比例最少的目标 */
	ENEMY_HP_SCALE_MIN,
	/** 敌方生命比例最少的两个目标 */
	ENEMY_HP_SCALE_MIN_TWO,
	/** 敌方生命比例最多的目标 */
	ENEMY_HP_SCALE_MAX,
	/** 敌方速度最低的目标 */
	ENEMY_SPEED_MIN,
	/** 敌方速度最高的目标 */
	ENEMY_SPEED_MAX,
	/** 敌方任意一个死人 */
	ENEMY_DEAD_ONE,
	/** 敌方全部死人 */
	ENEMY_DEAD_ALL,
	/** 敌方的玩家单位 */
	ENEMY_PLAYER,
	/** 敌方随机二个人 */
	ENEMY_TWO,
	/** 敌方随机三个人 */
	ENEMY_THREE,
	/** 敌方随机四个人 */
	ENEMY_FOUR,
	/** 敌方随机五个人 */
	ENEMY_FIVE,

	// 我方

	/** 我方任意单体 */
	FRIEND_ONE,
	/** 我方全体 */
	FRIEND_ALL,
	/** 我方前排单体 */
	FRIEND_FRONT_ONE,
	/** 我方前排全体 */
	FRIEND_FRONT_ALL,
	/** 我方后排单体 */
	FRIEND_BACK_ONE,
	/** 我方后排全体 */
	FRIEND_BACK_ALL,
	/** 我方竖排双目标(从同排人数多到少，人数相同就进行随机) */
	FRIEND_ROW,
	/** 我方生命最少的目标 */
	FRIEND_HP_MIN,
	/** 我方生命最少的两个目标 */
	FRIEND_HP_TWO,
	/** 我方生命最高的目标 */
	FRIEND_HP_MAX,
	/** 我方生命比例最少的目标 */
	FRIEND_HP_SCALE_MIN,
	/** 我方生命比例最少的两个目标 */
	FRIEND_HP_SCALE_MIN_TWO,
	/** 我方生命比例最多的目标 */
	FRIEND_HP_SCALE_MAX,
	/** 我方速度最低的目标 */
	FRIEND_SPEED_MIN,
	/** 我方速度最高的目标 */
	FRIEND_SPEED_MAX,
	/** 我方任意一个死人 */
	FRIEND_DEAD_ONE,
	/** 我方全部死人 */
	FRIEND_DEAD_ALL,
	/** 我方的玩家单位 */
	FRIEND_PLAYER,
	/** 自己 */
	MYSELF,
	/** 我方随机两个人 */
	FRIEND_TWO,
	/** 我方随机三个人 */
	FRIEND_THREE,
	/** 我方随机四个人 */
	FRIEND_FOUR,
	/** 我方随机五个人 */
	FRIEND_FIVE;

}
