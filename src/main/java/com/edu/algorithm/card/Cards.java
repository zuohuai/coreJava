package com.edu.algorithm.card;

import java.util.Random;

/**
 * 代表一副牌
 * @author jy
 */
public class Cards {
	/** 定义好所有的花色 */
	private CardSuit[] suits = { CardSuit.HEARTS, CardSuit.SQUARE, CardSuit.CLUB, CardSuit.BLACK };
	/** 创建一个容器,用来容纳牌 */
	private Card[] cards = null;
	/** 13张牌 */
	private int amount = 13;
	/** 总的排数 */
	private int total = 52;
	/** 随机边界 */
	private int bound = 3;

	public Cards() {
		init();
	}

	private void init() {
		cards = new Card[total];
		int index = 0;
		for (CardSuit cardSuit : suits) {
			for (int i = 1; i <= amount; i++) {
				cards[index] = new Card(cardSuit, i);
				index++;
			}
		}
	}

	public void showCards() {
		for (Card card : cards) {
			System.out.println(card);
		}
	}

	/**
	 * 其实考虑使用Collections中的shuffle来做随机操作，好像很偷懒
	 * 这种随机方式似乎有一点问题，太容易被猜透了
	 */
	public void shuffle() {
		Card[] target = new Card[total];
		int i = 0, j = 26, index = 0, n;
		Random random = new Random();
		while (index < total) {
			n = random.nextInt(bound) + 1;
			while (n > 0 && i < 26) {
				target[index] = cards[i];
				index++;
				i++;
				n--;
			}

			n = random.nextInt(bound) + 1;
			while (n > 0 && j < 52) {
				target[index] = cards[j];
				index++;
				j++;
				n--;
			}
		}

		n = random.nextInt(bound)+1;
		for (int k = 0; k < total; k++) {
			cards[k] = target[(n+k)%52];
		}
	}
}
