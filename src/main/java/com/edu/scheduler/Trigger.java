package com.edu.scheduler;

import java.util.Date;

/**
 * 
 * ClassName: Trigger <br/>  
 * Function: 触发器接口，用于确定下次任务的执行时间  <br/>  
 * date: 2016年7月28日 下午1:32:09 <br/>  
 *  
 * @author hison.zhang  
 * @version   
 * @since JDK 1.7
 */
public interface Trigger {

	/**
	 * 获取下次执行时间
	 * @param triggerContext
	 * @return
	 */
	Date nextTime(TaskContext triggerContext);

}
