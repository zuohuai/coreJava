package com.edu.netty.handler;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.AttributeKey;

public class HelloServerHandler extends ChannelInboundHandlerAdapter {

	private static AttributeKey<String> SESSION_KEY = AttributeKey.valueOf("SESSION");

	@Override
	public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
		System.out.println("ChannelRegister");
		ctx.fireChannelRegistered();
		// 可以用来做防火墙 TODO
		// Channel channel = ctx.channel();
		// channel.close();
	}

	@Override
	// 读取Client发送的信息，并打印出来
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		ByteBuf buf = ByteBuf.class.cast(msg);
		byte[] req = new byte[buf.readableBytes()];
		buf.readBytes(req);
		String body = new String(req, "UTF-8");
		System.out.println("The time receiver order:" + body);
		String currentTime = "QUERY TIME ORDER".equalsIgnoreCase(body) ? "Hello" : "BAD ORDER";
		currentTime += System.getProperty("line.separator");
		ByteBuf resp = Unpooled.copiedBuffer(currentTime.getBytes());
		ctx.writeAndFlush(resp);
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		Channel channel = ctx.channel();
		channel.attr(SESSION_KEY);
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		System.out.println("ChannelActive!");
	}

	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		ctx.flush();
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		ctx.close();
	}
}
