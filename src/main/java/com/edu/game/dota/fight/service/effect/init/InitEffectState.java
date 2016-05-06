package com.eyu.snm.module.fight.service.effect.init;

import java.util.Map;

/**
 * 初始化技能效果
 * @author shenlong
 */
public class InitEffectState {
	
	/** 效果类型 */
	private InitType type;
	/** 选择器链 */
	private String selector;
	/** 是否可重复施放 */
	private boolean repeat;
	/** 执行效果内容 */
	private Map<String, Object> ctx;
	
	
	public InitType getType() {
		return type;
	}

	public String getSelector() {
		return selector;
	}

	public Map<String, Object> getCtx() {
		return ctx;
	}

	public boolean isRepeat() {
		return repeat;
	}

}
