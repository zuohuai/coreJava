package com.edu.netty.conf;

import java.util.HashMap;
import java.util.Map;

import io.netty.channel.ChannelOption;
import io.netty.util.concurrent.EventExecutorGroup;

public class SessionConfig {
	/** 默认最大连接数 */
	public static final int DEFAULT_BACKLOG = 10000;
	/** 是否是TCP无延迟 */
	public static final boolean TCP_NODELAY = true;
	/** 是否允许重用地址 */
	public static final boolean REUSE_ADDR = true;
	/** 是否允许半开连接 */
	public static final boolean ALLOW_HALF_CLOSURE = false;

	/** 读缓冲 */
	private Integer readBuffSize;
	/** 写缓冲 */
	private Integer writeBuffSize;
	/** 连接超时 */
	private Integer timeout;
	/** 最大连接数 */
	private Integer backlog;

	public static SessionConfig valueOfServer(int readBuffSize, int writeBuffSize, int timeout, int backlog) {
		SessionConfig sessionConfig = new SessionConfig();
		sessionConfig.readBuffSize = readBuffSize;
		sessionConfig.writeBuffSize = writeBuffSize;
		sessionConfig.timeout = timeout;
		sessionConfig.backlog = backlog;
		return sessionConfig;
	}

	public static SessionConfig valueOfClient(int readBuffSize, int writeBuffSize, int timeout) {
		SessionConfig sessionConfig = new SessionConfig();
		sessionConfig.readBuffSize = readBuffSize;
		sessionConfig.writeBuffSize = writeBuffSize;
		sessionConfig.timeout = timeout;
		return sessionConfig;
	}

	/**
	 * 创建 {@link EventExecutorGroup} 并返回
	 * @return
	 */
	public Map<ChannelOption<?>, ?> buildClient() {
		Map<ChannelOption<?>, Object> values = new HashMap<>();
		values.put(ChannelOption.SO_RCVBUF, readBuffSize);
		values.put(ChannelOption.SO_SNDBUF, writeBuffSize);

		// 客户端默认配置
		values.put(ChannelOption.TCP_NODELAY, TCP_NODELAY);
		values.put(ChannelOption.ALLOW_HALF_CLOSURE, ALLOW_HALF_CLOSURE);
		return values;
	}

	/**
	 * 创建 {@link EventExecutorGroup} 并返回
	 * @return
	 */
	public Map<ChannelOption<?>, ?> buildServer() {
		Map<ChannelOption<?>, Object> values = new HashMap<>();
		values.put(ChannelOption.SO_RCVBUF, readBuffSize);
		values.put(ChannelOption.SO_SNDBUF, writeBuffSize);

		// 服务端默认配置
		values.put(ChannelOption.TCP_NODELAY, TCP_NODELAY);
		values.put(ChannelOption.ALLOW_HALF_CLOSURE, ALLOW_HALF_CLOSURE);
		values.put(ChannelOption.SO_REUSEADDR, REUSE_ADDR);
		return values;
	}

	public Integer getReadBuffSize() {
		return readBuffSize;
	}

	public Integer getWriteBuffSize() {
		return writeBuffSize;
	}

	public Integer getTimeout() {
		return timeout;
	}

	public Integer getBacklog() {
		return backlog;
	}

}
