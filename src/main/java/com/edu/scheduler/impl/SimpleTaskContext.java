package com.edu.scheduler.impl;

import java.util.Date;

import com.edu.scheduler.TaskContext;

/**
 * 
 * ClassName: SimpleTaskContext <br/>
 * Function: 简单的任务上下文对象 <br/>
 * date: 2016年7月28日 下午2:38:48 <br/>
 * 
 * @author hison.zhang
 * @version
 * @since JDK 1.7
 */
public class SimpleTaskContext implements TaskContext {

	/** 计划执行时间 */
	private volatile Date lastScheduledTime;
	/** 实际执行时间 */
	private volatile Date lastActualTime;
	/** 实际完成时间 */
	private volatile Date lastCompletionTime;

	public void update(Date scheduledTime, Date actualTime, Date completionTime) {
		this.lastScheduledTime = scheduledTime;
		this.lastActualTime = actualTime;
		this.lastCompletionTime = completionTime;
	}

	public Date lastScheduledTime() {
		return this.lastScheduledTime;
	}

	public Date lastActualTime() {
		return this.lastActualTime;
	}

	public Date lastCompletionTime() {
		return this.lastCompletionTime;
	}

}
