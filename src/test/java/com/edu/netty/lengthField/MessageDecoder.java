package com.edu.netty.lengthField;

import java.net.SocketAddress;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

public class MessageDecoder extends LengthFieldBasedFrameDecoder {

	public MessageDecoder(int maxLen) {
		super(maxLen, 4, 4, 0, 0);
	}

	@Override
	protected ByteBuf extractFrame(ChannelHandlerContext ctx, ByteBuf buffer, int index, int length) {
		return buffer.slice(index, length);
	}

	/**
	 * 解码成对象
	 */
	@Override
	protected Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
		if (in.readableBytes() == 0) {
			//BUFF为空
			return null;
		}
		Channel channel = ctx.channel();
		SocketAddress romoteAddress = channel.remoteAddress();
		System.out.println("连接:" + romoteAddress+"准备解码BUFF:[" + in.readerIndex()+","+ in.readableBytes() +"]");
		
		ByteBuf frame = (ByteBuf)super.decode(ctx, in);
		try{
			
		}catch(Exception e){
			//解码错误，强制关闭连接
			ctx.close();
		}
	}
}
