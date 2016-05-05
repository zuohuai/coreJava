package com.edu.game.jct.fight.service.effect.passive;

import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import com.edu.game.jct.fight.service.core.Phase;
import com.edu.game.jct.fight.service.effect.StateCtxKeys;
import com.edu.game.resource.JsonObject;

/**
 * 被动效果状态
 * @author Frank
 */
public class PassiveState implements Cloneable, JsonObject {

	public static final Comparator<PassiveState> COMPARATOR_PRIORITY = new Comparator<PassiveState>() {
		@Override
		public int compare(PassiveState o1, PassiveState o2) {
			int ret = o2.priority - o1.priority;
			if (ret == 0) {
				return o1.id.compareTo(o2.id);
			}
			return ret;
		}
	};

	/** 被动技能标识 */
	private String id;
	/** 技能优先级 */
	private int priority;
	/** 生效次数(null表示永远生效) */
	private Integer times;
	/** 有效的执行阶段 */
	private Set<Phase> phases;
	/** 效果执行时所需的公式上下文内容 */
	private HashMap<String, Object> ctx;
	
	/**
	 * 检查是否有效
	 * @return
	 */
	public boolean isVaild() {
		if (times == null) {
			return true;
		}
		if (times > 0) {
			return true;
		}
		return false;
	}
	
	/**
	 * 扣除生效次数
	 * @return true:次数已经为0需要移除,false:不需要移除
	 */
	public boolean decreaseTimes() {
		if (times == null) {
			return false;
		}
		times--;
		if (times <= 0) {
			return true;
		}
		return false;
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
	 * 克隆公式上下文对象
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public HashMap<String, Object> cloneCtx() {
		if (ctx != null) {
			return (HashMap<String, Object>) ctx.clone();
		} else {
			return new HashMap<String, Object>(3);
		}
	}

	public PassiveState clone(String id) {
		PassiveState state = clone();
		state.setId(id);
		if (state.phases == null) {
			state.phases = new HashSet<Phase>(0);
		}
		return state;
	}

	@Override
	protected PassiveState clone() {
		try {
			return (PassiveState) super.clone();
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

	public int getPriority() {
		return priority;
	}

	protected void setPriority(int priority) {
		this.priority = priority;
	}

	public Integer getTimes() {
		return times;
	}

	protected void setTimes(Integer times) {
		this.times = times;
	}

	public Set<Phase> getPhases() {
		return phases;
	}

	protected void setPhases(Set<Phase> phases) {
		this.phases = phases;
	}

	public HashMap<String, Object> getCtx() {
		return ctx;
	}

	protected void setCtx(HashMap<String, Object> ctx) {
		this.ctx = ctx;
	}

}
