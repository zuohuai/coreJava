package com.eyu.snm.module.fight.service.effect;

/**
 * 效果配置信息key
 * @author shenlong
 */
public interface EffectKeys {
	/** 判定时是否依赖之前是否命中 */
	String RELAY = "relay";
	/** BUFF配置的标识 */
	String BUFF = "buff";
	/** 属性变更 */
	String ATTR = "alters";
	/** 修正下一次行动时间 */
	String UPDATETIME = "updateTime";
	/** 打断类型 */
	String BREAKTYPE = "breakType";
	/** 命中目标数量 */
	String TARGET_NUM = "targetNum";
	/** 暴击目标数量 */
	String CRIT_NUM = "critNum";
	/** 指定位置 */
	String POSITION = "position";
	/** 公式ID */
	String FORMULA_ID = "formulaId";

}
