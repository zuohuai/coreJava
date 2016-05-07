package com.edu.random;

import java.util.Date;

import org.junit.Test;

import com.edu.utils.time.DateUtils;

public class RandomTest {

	@Test
	public void test_random() throws Exception {
		Date current = new Date();
		Random random = new Random(current.getTime());
		RandomUtils.isHit(random, 100, 0.1);
		
		current = DateUtils.addMinutes(current, 1);
		RandomUtils.isHit(random, 100, 0.1);
	}
}

class RandomUtils {
	public static boolean isHit(Random random, int max, double rate) {
		int nextInt = random.nextInt(max);
		System.out.println(nextInt);
		return nextInt < (int) Math.round(rate * max);
	}
}

/**
 * 随机函数测试
 * @author Administrator
 */
class Random {

	private int seed;

	private static final int multiplier = 214013;
	private static final int addend = 0xB;

	private static final int mask = 0x7fff;

	public Random(long seed) {
		this.seed = (int) (seed / 1000);
	}

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
		return val;
	}

	public int getSeed() {
		return seed;
	}
}
