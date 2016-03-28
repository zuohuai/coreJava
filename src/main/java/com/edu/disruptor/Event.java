package com.edu.disruptor;

import com.lmax.disruptor.EventFactory;

/**
 * 事件对象
 * @param <T> 事件体类型
 */
public class Event<T> {

	/** 事件名 */
	private String name;

	/** 事件体 */
	private T body;
	
	public static <T> Event<T> valueOf(String name, T body) {
		Event<T> event = new Event<T>();
		event.setBody(body);
		event.setName(name);
		return event;
	}

	
	public final static EventFactory<Event<Object>> EVENT_FACTORY = new EventFactory<Event<Object>>() {
		public Event<Object> newInstance() {
			Event<Object> result = new Event<Object>();
			return result;
		}
	};
	
	/**
	 * 构造方法
	 * @param name 事件名
	 */
	public Event(String name) {
		this.name = name;
	}
	/**
	 * 构造方法
	 * @param name 事件名
	 */
	public Event() {

	}

	/**
	 * 构造方法
	 * @param name 事件名
	 * @param body 事件体
	 */
	public Event(String name, T body) {
		this.name = name;
		this.body = body;
	}

	/**
	 * 获取 事件名
	 * @return
	 */
	public String getName() {
		return name;
	}

	/**
	 * 设置事件名称
	 * @param name
	 */
	void setName(String name) {
		this.name = name;
	}
	/**
	 * 获取 事件体
	 * @return
	 */
	public T getBody() {
		return body;
	}

	/**
	 * 设置 事件体
	 * @param body 事件体
	 */
	public void setBody(T body) {
		this.body = body;
	}

}
