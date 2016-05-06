package com.eyu.snm.module.fight.service.buff;

import java.util.HashMap;
import java.util.Map;

import com.eyu.common.resource.JsonObject;
import com.eyu.common.utils.lang.NumberUtils;
import com.eyu.snm.module.fight.model.UnitValue;

/**
 * BUFF状态
 * @author shenlong
 */
public class BuffState implements Cloneable, JsonObject {

	/** BUFF配置标识 */
	private String id;
	/** 执行时间 */
	private int time;
	/** 初始执行时间 */
	private int initTime;
	/** 间隔时间 */
	private Integer interval;
	/** buff特殊效果 */
	private int specialEffect;
	/** 对应的SkillId */
	private String skillId;
	/** 执行时所需的上下文内容 */
	private HashMap<String, Object> ctx;
	/** 是否永久类型buff(用于回合结束时重置) */
	private boolean forever;

	public BuffState clone(String id) {
		BuffState state = clone();
		state.setId(id);
		state.ctx = cloneCtx();
		return state;
	}

	@Override
	protected BuffState clone() {
		try {
			return (BuffState) super.clone();
		} catch (CloneNotSupportedException e) {
			throw new IllegalStateException("对象无法被克隆", e);
		}
	}

	/**
	 * 克隆上下文对象
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public HashMap<String, Object> cloneCtx() {
		if (ctx != null) {
			HashMap<String, Object> result = (HashMap<String, Object>) ctx.clone();
			if (ctx.containsKey(StateCtxKeys.ALTERS)) {
				result.put(StateCtxKeys.ALTERS, ((HashMap<UnitValue, Integer>) ctx.get(StateCtxKeys.ALTERS)).clone());
			}
			if (ctx.containsKey(StateCtxKeys.ALTER_VALUE)) {
				result.put(StateCtxKeys.ALTER_VALUE, ((HashMap<UnitValue, Integer>) ctx.get(StateCtxKeys.ALTER_VALUE)).clone());
			}
			return result;
		} else {
			return new HashMap<String, Object>(3);
		}
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getSpecialEffect() {
		return specialEffect;
	}

	public int getInterval() {
		return interval;
	}

	public int getTime() {
		return time;
	}

	public void setTime(int time) {
		this.time = time;
	}

	public HashMap<String, Object> getContent() {
		return ctx;
	}

	public int getInitTime() {
		return initTime;
	}

	public String getSkillId() {
		return skillId;
	}

	public Map<String, Object> getCtx() {
		return ctx;
	}

	public boolean isForever() {
		return forever;
	}

	public void setForever(boolean forever) {
		this.forever = forever;
	}

	/**
	 * 获取上下文中的配置值
	 * @param key 配置键{@link StateCtxKeys}
	 * @param clz 配置值类型
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T> T getCtxValue(String key, Class<T> clz) {
		Object value = (Object) ctx.get(key);
		if (value instanceof Number) {
			T result = NumberUtils.valueOf(clz, (Number) value);
			return (T) result;
		} else {
			return (T) value;
		}
	}

	/**
	 * 获取上下文中的配置值
	 * @param key 配置键{@link StateCtxKeys}
	 * @param clz 配置值类型
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T> T getCtxValue(String key, Class<T> clz, T defaultValue) {
		Object value = (Object) ctx.get(key);
		if (value == null) {
			return defaultValue;
		} else if (value instanceof Number) {
			T result = NumberUtils.valueOf(clz, (Number) value);
			return (T) result;
		} else {
			return (T) value;
		}
	}

}
