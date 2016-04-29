package com.edu.netty.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 通信消息对象
 * @author Administrator
 * TODO 这个可以定义对应的消息通信格式 
 */
public class Message {
	private static final Logger LOGGER = LoggerFactory.getLogger(Message.class);
	/**消息体*/
	private byte[] body = new byte[0];
}
