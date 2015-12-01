package com.edu.orm;

public interface CursorCallback<T> {

	public void call(T entity);
	
}
