package com.eyu.snm.module.fight.service.effect.init;

import java.util.List;
import java.util.Map;

import com.eyu.snm.module.fight.service.core.Unit;

/**
 * 初始化效果接口(具体的初始化技能执行效果)
 * @author shenlong
 */
public interface InitEffect {

	/** 初始化类型 */
	InitType getInitType();

	/**
	 * 执行效果
	 * @param content
	 * @param targets
	 */
	void execute(Map<String, Object> content, List<Unit> targets);

}
