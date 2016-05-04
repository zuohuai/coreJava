package com.edu.game.jct.fight.service.effect;

/**
 * 状态上下文常用键值定义
 * @author administrator
 */
public interface StateCtxKeys {

	/** BUFF配置的标识 */
	String BUFF = "BUFF";

	/** BUFF的归类 */
	String TAGS = "TAGS";

	/** BUFF的修改内容 */
	String ALTERS = "ALTERS";

	/** 值计算类型 */
	String CAL_TYPE = "CAL_TYPE";

	/** 被动效果的配置标识 */
	String PASSIVE = "PASSIVE";

	/** 驱散类型 */
	String DISPEL = "DISPEL";

	/** 比率值 */
	String RATE = "RATE";

	/** 调整参数 */
	String FACTOR = "FACTOR";

	/** 状态 */
	String STATE = "STATE";

	/** 召唤单位配置 */
	String UNITS = "UNITS";

	/** 效果数组 */
	String EFFECTS = "EFFECTS";

	/** 是否忽略命中与闪避 */
	String NOT_MISS = "NOT_MISS";

	/** 忽略{@link StateCtxKeys#NOT_MISS} */
	String IGNORE_NOT_MISS = "IGNORE_NOT_MISS";

	/** 清除CD排除的技能ID */
	String EXCLUDE = "EXCLUDE";

	// 分身技能添加的键值

	/** 目标位置 */
	String POSITION = "POSITION";

	/** 技能配置 */
	String SKILLS = "SKILLS";

	/** 武将基础ID */
	String BASEID = "BASEID";

	/** 模型配置 */
	String MODEL = "MODEL";

	/** 名称配置 */
	String NAME = "NAME";

	/** 数值配置 */
	String VALUES = "VALUES";

	/** 比率值 */
	String RATES = "RATES";

	/** 分身被动效果的配置标识 */
	String CLONE_PASSIVE = "CLONE_PASSIVE";

}
