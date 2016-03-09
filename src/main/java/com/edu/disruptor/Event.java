package com.edu.disruptor;

import com.lmax.disruptor.EventFactory;


/**
 * 事件定义
 * @author Administrator
 *
 * @param <T>
 */
public class Event<T> {
	/** 事件名称 */
	private String name;
	/** 事件体 */
	private T body;

	public static <T> Event<T> valueOf(String name, T body) {
		return new Event<T>(name, body);
	}

	public Event() {
		
	}

	public Event(String name, T body) {
		this.name = name;
		this.body = body;
	}

	public String getName() {
		return name;
	}

	public T getBody() {
		return body;
	}

	public final static EventFactory<Event<?>> EVENT_FACTORY = new EventFactory<Event<?>>() {
		public Event<?> newInstance() {
			return new Event<Object>();
		}
	};

}
