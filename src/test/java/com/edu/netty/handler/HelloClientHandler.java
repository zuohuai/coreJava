package com.edu.netty.handler;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class HelloClientHandler extends ChannelInboundHandlerAdapter {

	private int counter;

	private byte[] req;

	public HelloClientHandler() {
		this.req = ("QUERY TIME ORDER" + System.getProperty("line.separator")).getBytes() ;
	}

	@Override
	// 读取服务端的信息
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		ByteBuf buf = (ByteBuf)msg;
		byte[] req = new byte[buf.readableBytes()];
		buf.readByte();
		String body = new String(req, "UTF-8");
		counter++;
		System.out.println("Now is :" + body + ";the counter is:" + counter);
	}

	@Override
	// 当连接建立的时候向服务端发送消息 ，channelActive 事件当连接建立的时候会触发
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		ByteBuf message = null;
		for (int i = 0; i < 20; i++) {
			message = Unpooled.buffer(req.length);
			message.writeBytes(req);
			ctx.writeAndFlush(message);
		}
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause){
		ctx.close();
		cause.printStackTrace();
	}
}