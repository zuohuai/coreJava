package com.eyu.snm.module.fight.service.buff;

/**
 * buff类型
 * @author shenlong
 */
public enum BuffType {

	/** 战斗单元一次直接生效型 */
	UNIT_ONCE,
	/** 战斗单元间隔生效型 */
	UNIT_INTERVAL,
	/** 战斗单元永久型 */
	UNIT_FOREVER,
	/** 战场buff,子弹类型 */
	BULLET_BUFF,
	/** 战场buff */
	AREA;

}
