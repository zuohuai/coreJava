package com.edu.scheduler;

import java.util.Date;

/**
 * 
 * ClassName: TaskContext <br/>  
 * Function: 任务上下文对象，封装任务的计划时间和完成时间  <br/>  
 * date: 2016年7月28日 下午1:31:52 <br/>  
 *  
 * @author hison.zhang  
 * @version   
 * @since JDK 1.7
 */
public interface TaskContext {

	/**
	 * 返回最后排定的任务执行时间，或者为null，如果没有安排过。
	 * @return
	 */
	Date lastScheduledTime();

	/**
	 * 返回的最后一个任务的实际执行时间，或者为null，如果没有安排过。
	 * @return
	 */
	Date lastActualTime();

	/**
	 * 返回该任务的最后完成时间，或NULL如果没有安排过。
	 * @return
	 */
	Date lastCompletionTime();

}
