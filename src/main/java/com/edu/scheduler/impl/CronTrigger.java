package com.edu.scheduler.impl;

import java.util.Date;
import java.util.TimeZone;

import com.edu.scheduler.TaskContext;
import com.edu.scheduler.Trigger;

/**
 * 
 * ClassName: CronTrigger <br/>  
 * Function: Cron定时表达式触发器 <br/>  
 * Reason: TODO ADD REASON(可选). <br/>  
 * date: 2016年7月28日 下午1:37:02 <br/>  
 *  
 * @author hison.zhang  
 * @version   
 * @since JDK 1.7
 */
public class CronTrigger implements Trigger {

	private final CronSequenceGenerator sequenceGenerator;

	public CronTrigger(String expression) {
		this(expression, TimeZone.getDefault());
	}

	public CronTrigger(String cronExpression, TimeZone timeZone) {
		this.sequenceGenerator = new CronSequenceGenerator(cronExpression, timeZone);
	}

	@Override
	public Date nextTime(TaskContext context) {
		Date date = context.lastCompletionTime();
		if (date != null) {
			Date scheduled = context.lastScheduledTime();
			if (scheduled != null && date.before(scheduled)) {
				date = scheduled;
			}
		} else {
			date = new Date();
		}
		
		Date result = this.sequenceGenerator.next(date);
		return result;
	}

}
