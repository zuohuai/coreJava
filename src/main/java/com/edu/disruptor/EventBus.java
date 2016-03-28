package com.edu.disruptor;

/**
 * 事件总线接口
 * @author 
 */
public interface EventBus {

	/**
	 * 发送事件
	 * @param event 事件对象，不允许为 null
	 * @throws IllegalArgumentException 事件对象为 null 时引发
	 */
	void post(Event<?> event);

	/**
	 * 同步发送事件
	 * @param event 事件对象，不允许为 null
	 * @throws IllegalArgumentException 事件对象为 null 时引发
	 */
	void syncPost(Event<?> event);
}
