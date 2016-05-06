package com.eyu.snm.module.fight.service.move;

import com.eyu.snm.module.fight.model.report.ActionReport;
import com.eyu.snm.module.fight.service.core.Unit;

/**
 * 移动接口
 * @author Frank
 */
public interface MoveEffect {

	/** 获取移动类型 */
	MoveType getType();

	/** 执行移动效果 */
	ActionReport execute(Unit owner);

}
