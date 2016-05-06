package com.eyu.snm.module.fight.service.effect;

/**
 * 受击伤害位移码
 * @author shenlong
 */
public enum DamageState {

	/** 闪避(0) */
	MISS(0),
	/** 伤害免疫(4) */
	IMMUNE(4),
	/** 正常伤害(2*4) */
	NORMAL(8),
	/** 暴击伤害(3*4) */
	CRIT(12);

	private int offset;

	private DamageState(int offset) {
		this.offset = offset;
	}

	public int getOffset() {
		return offset;
	}

}
