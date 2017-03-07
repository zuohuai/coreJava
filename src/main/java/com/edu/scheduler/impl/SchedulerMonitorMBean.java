package com.edu.scheduler.impl;

import javax.management.MXBean;

@MXBean
public interface SchedulerMonitorMBean {
	/**
	 * 定时任务池大小
	 */
	int getSchedulerQueueSize();
	
	/**
	 * 池正在执行的线程数
	 */
	int getPoolActiveCount();
	
}
