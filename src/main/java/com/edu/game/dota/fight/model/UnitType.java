package com.edu.game.dota.fight.model;

import com.eyu.snm.module.fight.service.move.MoveType;

/**
 * 战斗单位类型
 * @author Frank
 */
public enum UnitType {

	/** 前军 */
	FRONT,
	/** 中军 */
	MIDDLE,
	/** 后军 */
	BACK;

	public MoveType toMoveType() {
		switch (this) {
		case FRONT:
			return MoveType.FRONT;
		case MIDDLE:
			return MoveType.MIDDLE;
		case BACK:
			return MoveType.BACK;
		default:
			throw new RuntimeException("不支持[" + name() + "]类型转换");
		}
	}

}
