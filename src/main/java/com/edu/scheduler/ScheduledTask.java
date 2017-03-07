package com.edu.scheduler;

/***
 * 
 * ClassName: ScheduledTask <br/>  
 * Function: 定时任务接口 <br/>  
 * Reason: TODO ADD REASON(可选). <br/>  
 * date: 2016年7月28日 下午1:30:44 <br/>  
 *  
 * @author hison.zhang  
 * @version   
 * @since JDK 1.7
 */
public interface ScheduledTask extends Runnable {

	/**
	 * 获取当前任务的任务名
	 * @return
	 */
	String getName();
}
