package com.eyu.ahxy.module.fight.model.report;

import com.my9yu.common.protocol.annotation.Transable;

/**
 * BUFF 变更类型
 * @author Frank
 */
@Transable
public enum BuffAlterType {

	/** 添加 */
	ADD,
	/** 更新 */
	UPDATE,
	/** 移除 */
	REMOVE,
	/** 免疫(只用于表示BUFF由于免疫问题导致的施放失败) */
	IMMUNE;
}
