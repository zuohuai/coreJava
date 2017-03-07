package com.edu.scheduler.impl;

/**
 * 
 * ClassName: TaskRejectedException <br/>  
 * Function: 任务拒绝异常 <br/>  
 * date: 2016年7月28日 下午2:39:07 <br/>  
 *  
 * @author hison.zhang  
 * @version   
 * @since JDK 1.7
 */
public class TaskRejectedException extends RuntimeException {

	private static final long serialVersionUID = 6681519082476492615L;

	/** 创建一个没有任何表述信息的任务拒绝异常 */
	public TaskRejectedException() {
		super();
	}

	/**
	 * 创建一个任务拒绝异常
	 * @param message 异常信息
	 * @param cause 导致原因
	 */
	public TaskRejectedException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * 创建一个任务拒绝异常
	 * @param message 异常信息
	 */
	public TaskRejectedException(String message) {
		super(message);
	}

	/**
	 * 创建一个任务拒绝异常
	 * @param cause 导致原因
	 */
	public TaskRejectedException(Throwable cause) {
		super(cause);
	}

}
