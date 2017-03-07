package com.edu.scheduler;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 
 * ClassName: Scheduled <br/>  
 * Function: 计划任务触发条件声明注释(使用 Cron 表达式) <br/>  
 * Reason: TODO ADD REASON(可选). <br/>  
 * date: 2016年7月28日 下午1:30:24 <br/>  
 *  
 * @author hison.zhang  
 * @version   
 * @since JDK 1.7
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Scheduled {

	/** 任务名 */
	String name();

	/** 表达式值 */
	String value();
	
	/** 表达式值类型 */
	ValueType type() default ValueType.EXPRESSION;
	
	/** 当无法获取表达式值时使用的默认值 */
	String defaultValue() default "";
	
}
