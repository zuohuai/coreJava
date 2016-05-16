package com.edu.netty.handler;

import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

@Sharable
public class InboundHandler1 extends ChannelInboundHandlerAdapter {

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		System.out.println("InboundHandler1.channelRead: ctx :" + ctx);
		// 通知执行下一个InboundHandler 也可以使用 ctx.fireChannelRead(msg);
		super.channelRead(ctx, msg);
	}

	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		System.out.println("InboundHandler1.channelReadComplete");
		ctx.flush();
		super.channelReadComplete(ctx);
	}
}