package com.edu.game.jct.fight.model.report;

/**
 * BUFF 变更类型
 * @author Frank
 */
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
