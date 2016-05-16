package com.edu.netty.chapter5;

import java.nio.charset.StandardCharsets;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

/**
 * @author doctor
 *
 * @time 2015年7月6日  
 */
public class EchoServer {
	private final int port;

	public EchoServer() {
		this(8989);
	}

	public EchoServer(int port) {
		this.port = port;
	}

	/**
	 * 
	 * @param args
	 * @throws InterruptedException
	 */
	public static void main(String[] args) throws InterruptedException {
		new EchoServer().start();

	}

	public void start() throws InterruptedException {
		ServerBootstrap bootstrap = new ServerBootstrap();
		NioEventLoopGroup bossGroup = new NioEventLoopGroup(1);
		NioEventLoopGroup workerGroup = new NioEventLoopGroup();

		try {
			bootstrap.group(bossGroup, workerGroup)
					.channel(NioServerSocketChannel.class)
					.localAddress(port)
					.childHandler(new ChannelInitializer<SocketChannel>() {

						@Override
						protected void initChannel(SocketChannel ch) throws Exception {
							ch.pipeline().addLast(new LineBasedFrameDecoder(2048));
							ch.pipeline().addLast(new StringDecoder(StandardCharsets.UTF_8));
							ch.pipeline().addLast(new StringEncoder(StandardCharsets.UTF_8));
							ch.pipeline().addLast(new EchoServerHandler());
						}
					});

			ChannelFuture channelFuture = bootstrap.bind().sync();
			System.out.println(EchoServer.class.getName() + " started and listen on port:" + channelFuture.channel().localAddress());

			channelFuture.channel().closeFuture().sync();
		} finally {
			workerGroup.shutdownGracefully();
			bossGroup.shutdownGracefully();
		}
	}

	private static class EchoServerHandler extends ChannelInboundHandlerAdapter {

		@Override
		public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
			cause.printStackTrace();
			ctx.close();
		}

		@Override
		public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
			System.out.println(msg);
			ctx.write(NettyUtil.appenEndOfLine("服务器返回：", (String) msg));

		}

		@Override
		public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
			ctx.flush();
		}
	}

}