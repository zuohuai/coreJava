package com.edu.netty.chapter4;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;

public class TimeClientInitializer extends ChannelInitializer<SocketChannel> {

	@Override
	protected void initChannel(SocketChannel ch) throws Exception {
		ch.pipeline().addLast(new TimeClientHandler());
	}

}
