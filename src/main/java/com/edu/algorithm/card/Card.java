package com.edu.algorithm.card;

/**
 * 代表一张牌
 * @author jy
 */
public class Card {
	/** 花色 */
	private CardSuit cardSuit;
	/** 编号 */
	private int value;

	public Card(CardSuit cardSuit, int value) {
		this.cardSuit = cardSuit;
		this.value = value;
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append(cardSuit.getName()+":");
		switch (value) {
		case 1:
			sb.append('A');
			break;
		case 11:
			sb.append('J');
			break;
		case 12:
			sb.append('Q');
			break;
		case 13:
			sb.append('K');
			break;
		default:
			sb.append(value);
		}
		return sb.toString();
	}
}
