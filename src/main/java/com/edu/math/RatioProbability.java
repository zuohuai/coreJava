package com.edu.math;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

/**
 * 随机数的另外一种实现
 * @author Administrator
 * @param <T>
 */
public class RatioProbability<T> {

	/** 概率与结果的映射关系 */
	private LinkedHashMap<Integer, T> odds = new LinkedHashMap<Integer, T>();
	/** 种子 */
	private List<Integer> xrange = new ArrayList<>();
	/** 总数量 */
	private int total = -1;
	/** 随机值 */
	private Random random = null;

	/** 构造器 */
	private RatioProbability(Map<T, Integer> values) {
		int total = 0;
		int before = 1;
		for (Entry<T, Integer> entry : values.entrySet()) {
			T key = entry.getKey();
			total += entry.getValue();
			for (int j = before; j <= total; j++) {
				odds.put(j, key);
				xrange.add(j);
			}
			before = total + 1;
		}
		this.total = total;
		this.random = new Random();
	}

	/** 构造方法 */
	public static <T> RatioProbability<T> valueOf(Map<T, Integer> values) {
		return new RatioProbability<T>(values);
	}

	/** 获取人品结果 */
	public T getResult() {

		int index = new Double(random.nextDouble() * (total - 1)).intValue();
		Collections.shuffle(xrange);
		int key = xrange.get(index);
		return odds.get(key);
	}

	public int getTotal() {
		return total;
	}
}
