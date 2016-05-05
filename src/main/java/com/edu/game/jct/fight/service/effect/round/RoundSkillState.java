package com.edu.game.jct.fight.service.effect.round;

import java.util.HashMap;
import java.util.Map;

import com.edu.game.resource.JsonObject;

/**
 * 回合技能状态
 * @author qu.yy
 */
public class RoundSkillState implements Cloneable, JsonObject {

	/** 技能标识 */
	private String id;
	/** 选中状态 */
	private boolean state;
	/** 技能释放验证上下文 */
	private Map<String, Object> ctx;

	public RoundSkillState clone(String id) {
		RoundSkillState state = clone();
		state.id = id;
		state.ctx = new HashMap<String, Object>();
		if (this.ctx != null) {
			for (String key : this.ctx.keySet()) {
				state.ctx.put(key, this.ctx.get(key));
			}
		}
		return state;
	}

	@Override
	protected RoundSkillState clone() {
		try {
			return (RoundSkillState) super.clone();
		} catch (CloneNotSupportedException e) {
			throw new IllegalStateException("对象无法被克隆", e);
		}
	}

	/**
	 * 选中回合技能
	 */
	public void choose() {
		this.state = true;
	}

	/**
	 * 结束重置选中状态
	 */
	public void endRound() {
		this.state = false;
	}

	/**
	 * 添加技能释放条件
	 * @param key {@link RoundSkillKeys}
	 * @param value
	 */
	public void addCtx(String key, Object value) {
		if (ctx == null) {
			ctx = new HashMap<String, Object>();
		}
		ctx.put(key, value);
	}

	/**
	 * 获取释放技能的条件
	 * @param key key {@link RoundSkillKeys}
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <E> E getCtx(String key) {
		if (ctx == null) {
			return null;
		}
		return (E) ctx.get(key);
	}

	// Getter and Setter...

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public boolean isState() {
		return state;
	}

	public void setState(boolean state) {
		this.state = state;
	}

	public Map<String, Object> getCtx() {
		return ctx;
	}

	public void setCtx(Map<String, Object> ctx) {
		this.ctx = ctx;
	}

}
