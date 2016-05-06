package com.edu.game.dota.fight.model.report.seed;

import com.eyu.snm.module.fight.service.core.Battle;

/**
 * 伪随机函数
 * @author shenlong
 */
public class Random {

	public Random(long longSeed, Battle battle) {
		seed = (int) (longSeed % 100000);
		this.battle = battle;
		seed = 6666;
	}

	private int seed;

	private Battle battle;

	private static final int multiplier = 214013;
	private static final int addend = 0xB;

	private static final int mask = 0x7fff;

	protected int next(int bits) {
		int nextseed = (seed * multiplier + addend) & mask;
		seed = nextseed;
		return (int) nextseed;
	}

	public int nextInt(int n) {
		if (n <= 0)
			throw new IllegalArgumentException("n must be positive");

		int bits, val;
		do {
			bits = next(31);
			val = bits % n;
		} while (bits - val + (n - 1) < 0);
		battle.getReport().addRandomValue(battle.getRelateTime(), val);
		return val;
	}

	public int getSeed() {
		return seed;
	}

}
