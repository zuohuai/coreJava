package com.edu.netty.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

@Sharable
public class InboundHandler2 extends ChannelInboundHandlerAdapter {
	@Override
	// 读取Client发送的信息，并打印出来
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		System.out.println("InboundHandler2.channelRead: ctx :" + ctx);
		ByteBuf result = (ByteBuf) msg;
		byte[] result1 = new byte[result.readableBytes()];
		result.readBytes(result1);
		ctx.write(msg);
		super.channelRead(ctx, msg);
	}

	/**
	 * 为什么这个方法不会被调用两次捏?!
	 */
	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		System.out.println("InboundHandler2.channelReadComplete");
		ctx.flush();
		super.channelReadComplete(ctx);
	}

}