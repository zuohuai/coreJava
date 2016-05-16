package com.edu.netty.handler;

import com.edu.utils.NamedThreadFactory;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.util.concurrent.DefaultEventExecutorGroup;
import io.netty.util.concurrent.EventExecutorGroup;

public class HelloServerInitializer extends ChannelInitializer<SocketChannel> {

	@Override
	protected void initChannel(SocketChannel ch) throws Exception {
		// 注册两个OutboundHandler，执行顺序为注册顺序的逆序，所以应该是OutboundHandler2 OutboundHandler1
		/*ch.pipeline().addLast(new OutboundHandler1());
		ch.pipeline().addLast(new OutboundHandler2());

		// 注册两个InboundHandler，执行顺序为注册顺序，所以应该是InboundHandler1 InboundHandler2
		ch.pipeline().addLast(new InboundHandler1());
		ch.pipeline().addLast(new InboundHandler2());*/
		
		
		ch.pipeline().addLast(new LineBasedFrameDecoder(1024));
		//ch.pipeline().addLast(new StringDecoder());
		EventExecutorGroup executorGroup = build("处理线程", "通讯线程");
		ch.pipeline().addLast(executorGroup, "HANDLER", new HelloServerHandler());
		
	}

	private EventExecutorGroup build(String groupName, String threadName) {
		ThreadGroup group = new ThreadGroup(groupName);
		NamedThreadFactory threadFactory = new NamedThreadFactory(group, threadName);
		EventExecutorGroup executor = new DefaultEventExecutorGroup(1, threadFactory);
		return executor;
	}

}
