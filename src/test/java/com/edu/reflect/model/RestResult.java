package com.edu.reflect.model;

public class RestResult<T> {

	private int code;

	private String msg;

	private T data;

	public static <T> RestResult<T> valueOf(int code, String msg, T data) {
		RestResult<T> result = new RestResult<T>();
		result.code = code;
		result.msg = msg;
		result.data = data;
		return result;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

}
