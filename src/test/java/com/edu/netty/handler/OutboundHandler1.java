package com.edu.netty.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;

@Sharable
public class OutboundHandler1 extends ChannelOutboundHandlerAdapter {
	@Override
	// 向client发送消息
	public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
		System.out.println("OutboundHandler1.write");
		String response = "I am ok!";
		ByteBuf encoded = ctx.alloc().buffer(4 * response.length());
		encoded.writeBytes(response.getBytes());
		ctx.write(encoded);
		ctx.flush();
	}

}