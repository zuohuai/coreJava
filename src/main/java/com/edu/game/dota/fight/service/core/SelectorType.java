package com.eyu.snm.module.fight.service.core;

/**
 * 选择类型
 * @author Frank
 */
public enum SelectorType {

	/** 当前所有目标 */
	CURRENT,
	/** 预判目标 */
	TARGET,
	/** 自己 */
	MYSELF,
	/** 我方 */
	OUR,
	/** 敌方 */
	ENEMY,
	/** 范围内 */
	RANGE,
	/** 距离最近 */
	CLOSEST,
	/** 距离最远 */
	FURTHEST,
	/** 随机 */
	RANDOM,
	/** 重复随机 */
	REPEAT_RANDOM,
	/** 基于属性的选择 */
	VALUE,
	/** 基于属性排序的选择 */
	SORTER,
	/** 活着的 */
	ALIVE,
	/** 根据前中后排选择 */
	UNIT_TYPE,
	/** 过滤某种状态的战斗单元 */
	STATE_FILTER,
	/** 寻找某种状态的战斗单元 */
	STATE,

	// 技能列相关

	/** 自己附近的几列 */
	SELF_AROUND,
	/** 目标附近的几列,会被重复选取 */
	TARGET_AROUND_REPEAT,

	// 指定位置相关

	/** 目标位置上的所有单位 */
	TARGET_POSITION,
	/** 目标位置重复选择指定目标 */
	EACH_POSITION_REPEAT,

	// 数量

	/** 指定数量上限 */
	AMOUNT;

}
