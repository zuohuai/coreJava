package com.edu.netty.client;

/**
 * socket 请求回调
 * @author Administrator
 *
 * @param <T>
 */
public interface SocketCallback<T> {

	/**
	 * 返回成功
	 * @param result
	 */
	void onSuccess(T result);

	/**
	 * 异常捕获
	 * @param e
	 */
	void onError(Exception e);

	/**
	 * 取消请求
	 */
	void onCancel();
}
