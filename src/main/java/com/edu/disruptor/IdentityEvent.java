package com.edu.disruptor;

public interface IdentityEvent extends NameEvent{
	/**
	 * 获取发生事件的用户身份标识
	 * @return
	 */
	long getOwner();
}
