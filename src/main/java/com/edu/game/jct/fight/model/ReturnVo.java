package com.edu.game.jct.fight.model;

import com.edu.game.jct.fight.service.config.BattleType;

/**
 * 战斗退费信息VO
 * @author Frank
 */
public class ReturnVo {

	/** 所在的战斗类型 */
	private BattleType type;
	/** 玩法附加信息内容 */
	private Object additions;

	// Getter and Setter ...

	public BattleType getType() {
		return type;
	}

	public void setType(BattleType type) {
		this.type = type;
	}

	public Object getAdditions() {
		return additions;
	}

	public void setAdditions(Object additions) {
		this.additions = additions;
	}

	// Static method's
	public static ReturnVo valueOf(BattleType type, Object additions) {
		ReturnVo vo = new ReturnVo();
		vo.type = type;
		vo.additions = additions;
		return vo;

	}
}
