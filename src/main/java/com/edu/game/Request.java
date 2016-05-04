package com.edu.game;

/**
 * 请求对象
 * @author Administrator
 *
 * @param <T>
 */
public class Request<T> {

	private T body;

	public static <T> Request<T> valueOf(T body) {
		Request<T> request = new Request<T>();
		return request;
	}

	public T getBody() {
		return body;
	}
}
