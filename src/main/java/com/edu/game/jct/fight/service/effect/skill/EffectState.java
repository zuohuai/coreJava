package com.edu.game.jct.fight.service.effect.skill;

import java.util.HashMap;

import com.edu.game.jct.fight.service.effect.StateCtxKeys;
import com.edu.game.jct.fight.service.effect.select.SelectType;
import com.edu.game.resource.JsonObject;

/**
 * 效果状态
 * @author Frank
 */
public class EffectState implements Cloneable, JsonObject {

	/** 技能效果标识 */
	private String id;
	/** 选择目标 */
	private SelectType target;
	/** 技能效果执行时所需的公式上下文内容 */
	private HashMap<String, Object> ctx;

	/**
	 * 获取上下文中的配置值
	 * @param key 配置键{@link StateCtxKeys}
	 * @param clz 配置值类型
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T> T getCtxValue(String key, Class<T> clz) {
		return (T) ctx.get(key);
	}
	
	/**
	 * 获取上下文中的配置值
	 * @param key 配置键{@link StateCtxKeys}
	 * @param clz 配置值类型
	 * @param defaultValue 默认值
	 * @return
	 */
	public <T> T getCtxValue(String key, Class<T> clz, T defaultValue) {
		T value = getCtxValue(key, clz);
		if (value == null) {
			return defaultValue;
		} else {
			return value;
		}
	}

	/**
	 * 设置上下文中的配置值
	 * @param key 配置键{@link StateCtxKeys}
	 * @param value 值
	 */
	public void setCtxValue(String key, Object value) {
		ctx.put(key, value);
	}
	
	@SuppressWarnings("unchecked")
	public HashMap<String, Object> cloneCtx() {
		if (ctx != null) {
			return (HashMap<String, Object>) ctx.clone();
		} else {
			return new HashMap<String, Object>(2);
		}
	}

	public EffectState clone(String id) {
		EffectState state = clone();
		state.id = id;
		return state;
	}

	@Override
	protected EffectState clone() {
		try {
			return (EffectState) super.clone();
		} catch (CloneNotSupportedException e) {
			throw new IllegalStateException("对象无法被克隆", e);
		}
	}

	// Getter and Setter ...
	
	public String getId() {
		return id;
	}
	
	public SelectType getTarget() {
		return target;
	}

	protected void setTarget(SelectType target) {
		this.target = target;
	}

	protected void setCtx(HashMap<String, Object> ctx) {
		this.ctx = ctx;
	}

}
