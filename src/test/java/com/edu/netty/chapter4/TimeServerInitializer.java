package com.edu.netty.chapter4;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;

public class TimeServerInitializer extends ChannelInitializer<SocketChannel> {

	@Override
	protected void initChannel(SocketChannel ch) throws Exception {
		ch.pipeline().addLast(new TimeServerHandler());
	}
}