package com.edu.disruptor;

/**
 * 事件处理方法
 * @author Administrator
 *
 * @param <T> 待处理事件对象
 */
public interface Receiver<T> {

	void onEvent(Event<T> event);
}
