package com.edu.scheduler;

import java.util.concurrent.ScheduledFuture;

/**
 * 
 * ClassName: Scheduler <br/>
 * Function: 定时任务调度器接口 <br/>
 * date: 2016年7月28日 下午1:31:18 <br/>
 * 
 * @author hison.zhang
 * @version
 * @since JDK 1.7
 */
public interface Scheduler {

	/**
	 * 
	 * schedule:提交一个按 Cron 表达式确定执行周期的任务 <br/>
	 * 
	 * @author hison.zhang
	 * @param task task 定时任务
	 * @param cron cron Cron表达式
	 * @return 可用于提取结果或取消的 ScheduledFuture
	 */
	ScheduledFuture<?> schedule(ScheduledTask task, String cron);

	/**
	 * 
	 * schedule:提交定时任务, 执行调度会结束后关闭或可通过返回的 ScheduledFuture 取消。<br/>
	 * 
	 * @author hison.zhang
	 * @param task 定时任务
	 * @param trigger 触发器
	 * @return 可用于提取结果或取消的 ScheduledFuture
	 */
	public ScheduledFuture<?> schedule(ScheduledTask task, Trigger trigger);
}
