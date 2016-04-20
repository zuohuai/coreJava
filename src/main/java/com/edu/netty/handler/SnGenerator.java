package com.edu.netty.handler;

import java.util.concurrent.atomic.AtomicLong;

/**
 * ID 生成器
 * @author Administrator
 *
 */
public class SnGenerator {
	
	/** 开始值 */
	private static final long START = 1;
	
	private AtomicLong sequence = new AtomicLong(START);

	/**
	 * 获取下一个序列号
	 * @return
	 */
	public long next() {
		long result = 0;
		while (result <= 0) {
			result = sequence.getAndIncrement();
			if (result == Long.MAX_VALUE) {
				sequence.compareAndSet(Long.MIN_VALUE, START);
			}
		}
		return result;
	}
}
