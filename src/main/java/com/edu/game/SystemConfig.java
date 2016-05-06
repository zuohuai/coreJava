package com.edu.game;

import org.springframework.stereotype.Component;

@Component
public class SystemConfig {
	/** 延迟队列的修正时间 */
	private int delayTime = 500;

	public int getDelayTime() {
		return delayTime;
	}
}
