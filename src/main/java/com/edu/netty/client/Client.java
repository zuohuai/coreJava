package com.edu.netty.client;

public interface Client {

	/**
	 * 关闭服务器连接
	 */
	void close();
	
	/**
	 * 连接到服务器
	 */
	void connect();
	
	/**
	 * 是否保持连接
	 * @return
	 */
	boolean isKeepAlive();
	
	/**
	 * 获取客户端的最后操作时间戳
	 * @return
	 */
	long getTimestamp();
	
	/**
	 * 检查会话是否处于连接状态
	 * @return
	 */
	boolean isConnect();
	
	/**
	 * 连接是否有效
	 * @return
	 */
	boolean isDisposed();
	
	/**
	 * 是否空闲
	 * @return
	 */
	boolean isIdle();
	
	/**
	 * 取消保持连接状态
	 */
	void disableKeepAlive();
	
	/**
	 * 获取连接的地址
	 * @return
	 */
	String getAddress();
}
