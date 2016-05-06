package com.edu.game.dota.fight.model.report;

import java.util.ArrayList;
import java.util.List;

/**
 * 战报快速比较
 * @author shenlong
 */
public class QuickCompare {

	private List<Compare> compares = new ArrayList<>();

	public void add(int relateTime, int value) {
		compares.add(new Compare(relateTime, value));
	}

	public List<Compare> getCompares() {
		return compares;
	}

	public void setCompares(List<Compare> compares) {
		this.compares = compares;
	}

	class Compare {

		/** 相对时间 */
		private int relateTime;
		/** 伤害值 */
		private int value;

		public int getRelateTime() {
			return relateTime;
		}

		public int getValue() {
			return value;
		}

		public Compare(int relateTime, int value) {
			this.relateTime = relateTime;
			this.value = value;
		}

	}

}
