package com.edu.game.dota.fight.model.report;

import io.netty.buffer.ByteBuf;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * 动作战报信息
 * @author Frank
 */
@JsonInclude(Include.NON_NULL)
public abstract class ActionReport {

	/** 时间点 */
	protected long timing;
	/** 所有者 */
	protected short owner;
	/** 附加信息内容 */
	protected Object addition;
	/** 相对时间 */
	protected transient int relateTime;

	/** 所属战报 */
	protected Report report;

	/**
	 * 获取动作类型
	 * @return {@link ActionType}
	 */
	public abstract ActionType getType();

	/**
	 * 战报编码
	 * @param buffer
	 */
	public abstract void encode(ByteBuf buffer);

	public long getTiming() {
		return timing;
	}

	public short getOwner() {
		return owner;
	}

	public Object getAddition() {
		return addition;
	}

	public int getRelateTime() {
		return relateTime;
	}

}
