package com.edu.game.dota.fight.service;

import com.eyu.snm.module.fight.model.FighterType;

/**
 * 作战标识<br/>
 * 通过该对象来标识，战斗计算的攻击方或防守方
 * @author frank
 */
public final class FighterId<T> {

	/** 战斗单位类型 */
	private final FighterType type;
	/** 战斗单位标识信息 */
	private final T content;

	/** 构造方法 */
	private FighterId(FighterType type, T content) {
		this.type = type;
		this.content = content;
	}

	/**
	 * 获取战斗单元类型描述
	 * @return
	 */
	public FighterType getType() {
		return type;
	}

	/**
	 * 获取战斗单元标识
	 * @return
	 */
	public T getContent() {
		return content;
	}

	// Static Method's ...

	/** 便捷构造方法 */
	public static <T> FighterId<T> valueOf(FighterType type, T id) {
		return new FighterId<T>(type, id);
	}

}
