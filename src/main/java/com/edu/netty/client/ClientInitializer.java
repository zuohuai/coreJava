package com.edu.netty.client;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;

/**
 * Channel初始化
 * @author Administrator
 *
 */
public class ClientInitializer extends ChannelInitializer<SocketChannel> {

	/**
	 * 每个通道建立都会调用这个InitChannel方法
	 */
	@Override
	protected void initChannel(SocketChannel ch) throws Exception {
		System.out.println("client Init !!!");
	}

}
