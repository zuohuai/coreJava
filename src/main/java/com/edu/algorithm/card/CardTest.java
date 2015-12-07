package com.edu.algorithm.card;

/**
 * 模拟随机发牌
 * @author jy
 */
public class CardTest {
	public static void main(String[] args) {
		Cards cards = new Cards();
		cards.shuffle();
		cards.showCards();
	}
}
