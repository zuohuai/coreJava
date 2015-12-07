package com.edu.algorithm.card;

/**
 * 代表花色
 * @author jy
 */
public enum CardSuit {
	/** 红桃 */
	HEARTS("红桃"),
	/** 方块 */
	SQUARE("方块"),
	/** 黑桃 */
	BLACK("黑桃"),
	/** 梅花 */
	CLUB("梅花");
	/** 说明文字 */
	private String name;

	private CardSuit(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
}
