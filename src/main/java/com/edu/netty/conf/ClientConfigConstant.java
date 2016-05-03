package com.edu.netty.conf;

public interface ClientConfigConstant {
	/**默认服务器地址与端口的配置*/
	String KEY_DEAULT_ADDRESS= "client.default.address";
	/**设置读缓存大小*/
	String KEY_BUFFER_READ = "client.socket.buffer.read";
	/**设置写缓存大小*/
	String KEY_BUFFER_WRITE = "client.socket.buffer.write";
	/**连接超时设置(单位:ms)*/
	String KEY_TIMEOUT = "client.socket.timeout";
	/**设置相应超时配置(单位:ms)*/
	String KEY_RESPONSE_TIMEOUT = "client.socket.response.timeout";
	/**非活跃客户端移除延时(单位:s)*/
	String KEY_REMOVE_TIME = "client.remove.times";
	
	/**线程池设置*/
	String KEY_POOL_MIN = "server.socket.pool.min";
	String KEY_POOL_MAX = "server.socket.pool.max";
	String KEY_POOL_IDLE = "server.socket.pool.idle";
}
