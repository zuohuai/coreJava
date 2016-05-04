package com.edu.game.jct.fight.service.effect.init;

import java.util.Comparator;
import java.util.HashMap;

import com.edu.game.jct.fight.service.effect.select.SelectType;
import com.edu.game.resource.JsonObject;

/**
 * 初始化技能状态
 * @author administrator
 */
public class InitEffectState implements Cloneable, JsonObject {

	public static final Comparator<InitEffectState> COMPARATOR_PRIORITY = new Comparator<InitEffectState>() {
		@Override
		public int compare(InitEffectState o1, InitEffectState o2) {
			int ret = o2.priority - o1.priority;
			if (ret == 0) {
				return o1.id.compareTo(o2.id);
			}
			return ret;
		}
	};

	/** 技能效果标识 */
	private String id;
	/** 技能优先级 */
	private int priority;
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

	public InitEffectState clone(String id) {
		InitEffectState state = clone();
		state.id = id;
		return state;
	}

	@Override
	protected InitEffectState clone() {
		try {
			return (InitEffectState) super.clone();
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

	public int getPriority() {
		return priority;
	}

	protected void setPriority(int priority) {
		this.priority = priority;
	}

}
