package com.edu.game.jct.fight.service.config;

import com.edu.game.jct.fight.model.FightType;

/**
 * 战斗类型
 * @author frank
 */
public enum BattleType {

	/** 模拟战斗(直接生成战报) */
	MOCK(FightType.TEST);

	private FightType type;

	BattleType(FightType type) {
		this.type = type;
	}

	public FightType getType() {
		return type;
	}

	public void setType(FightType type) {
		this.type = type;
	}

}
