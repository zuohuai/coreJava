package com.edu.event;

/**
 * 事件定义
 * @author Administrator
 *
 */
public class Event<T> {
	/**事件名称*/
	private String name;
	/**事件体*/
	private T body;
	
	
	public static <T> Event<T> valueOf(String name , T body){
		return new Event<T>(name, body);
	}
	
	public Event(String name, T body){
		this.name = name;
		this.body = body;
	}
	
	public String getName() {
		return name;
	}
	public T getBody() {
		return body;
	}
	
}
