package com.edu.disruptor;

import com.lmax.disruptor.EventFactory;

public class ValueEvent {
	/** 事件名 */
	private String name;
	/** 事件体 */
	private Object body;

	
	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public Object getBody() {
		return body;
	}


	public void setBody(Object body) {
		this.body = body;
	}


	public final static EventFactory<ValueEvent> EVENT_FACTORY = new EventFactory<ValueEvent>() {
		public ValueEvent newInstance() {
			ValueEvent result = new ValueEvent();
			return result;

		}
	};
}