package com.edu.game.jct.fight.service.effect.buff;

import java.util.Comparator;
import java.util.HashMap;

import com.edu.game.jct.fight.model.AlterType;
import com.edu.game.jct.fight.service.effect.StateCtxKeys;
import com.edu.game.resource.JsonObject;


/**
 * BUFF状态
 * @author Frank
 */
public class BuffState implements Cloneable, JsonObject {
	
	
	public static final Comparator<BuffState> COMPARATOR_PRIORITY = new Comparator<BuffState>() {
		@Override
		public int compare(BuffState o1, BuffState o2) {
			int ret = o2.priority - o1.priority;
			if (ret == 0) {
				return o1.id.compareTo(o2.id);
			}
			return ret;
		}
	};

	/** BUFF标识 */
	private String id;
	/** BUFF类型 */
	private String tag;
	/** 技能优先级 */
	private int priority;
	/** 剩余回合数 */
	private int times;
	/** 驱散类型 */
	private String type;
	/** 执行时所需的上下文内容 */
	private HashMap<String, Object> ctx;

	/**
	 * 扣减剩余回合数
	 * @return
	 */
	public int decreaseTimes() {
		times--;
		return times;
	}

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
	 * 克隆上下文对象
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public HashMap<String, Object> cloneCtx() {
		if (ctx != null) {
			HashMap<String, Object> result = (HashMap<String, Object>) ctx.clone();
			if (ctx.containsKey(StateCtxKeys.ALTERS)) {
				result.put(StateCtxKeys.ALTERS, ((HashMap<AlterType, String>) ctx.get(StateCtxKeys.ALTERS)).clone());
			}
			return result;
		} else {
			return new HashMap<String, Object>(3);
		}
	}

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

	// Getter and Setter ...
	
	public String getId() {
		return id;
	}

	protected void setId(String id) {
		this.id = id;
	}

	public int getTimes() {
		return times;
	}

	protected void setTimes(int times) {
		this.times = times;
	}

	public String getTag() {
		return tag;
	}

	protected void setTag(String tag) {
		this.tag = tag;
	}

	public HashMap<String, Object> getCtx() {
		return ctx;
	}

	protected void setCtx(HashMap<String, Object> ctx) {
		this.ctx = ctx;
	}

	public DispelType getType() {
		return DispelType.valueOf(type);
	}

	protected void setType(String type) {
		this.type = type;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

}
