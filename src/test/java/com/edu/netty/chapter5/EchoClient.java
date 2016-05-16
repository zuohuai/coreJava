package com.edu.netty.chapter5;

import java.nio.charset.StandardCharsets;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.ReferenceCountUtil;

/**
 * @author doctor
 *
 * @time 2015年7月6日  
 */
public class EchoClient {

	private final String host;
	private final int port;

	public EchoClient() {
		this("localhost", 8989);
	}

	public EchoClient(String host) {
		this(host, 8989);
	}

	public EchoClient(String host, int port) {
		this.host = host;
		this.port = port;
	}

	/**
	 * @param args
	 * @throws InterruptedException
	 */
	public static void main(String[] args) throws InterruptedException {
		new EchoClient().start();

	}

	public void start() throws InterruptedException {
		NioEventLoopGroup workersGroup = new NioEventLoopGroup(1);

		try {
			Bootstrap bootstrap = new Bootstrap();
			bootstrap.group(workersGroup)
					.channel(NioSocketChannel.class)
					.remoteAddress(host, port)
					.handler(new ChannelInitializer<SocketChannel>() {

						@Override
						protected void initChannel(SocketChannel ch) throws Exception {
							ch.pipeline().addLast(new LineBasedFrameDecoder(2048));
							ch.pipeline().addLast(new StringDecoder(StandardCharsets.UTF_8));
							ch.pipeline().addLast(new StringEncoder(StandardCharsets.UTF_8));
							ch.pipeline().addLast(new EchoClientHandler());
						}
					});

			ChannelFuture channelFuture = bootstrap.connect().sync();
			channelFuture.channel().closeFuture().sync();

		} finally {
			workersGroup.shutdownGracefully();
		}
	}

	private static class EchoClientHandler extends ChannelInboundHandlerAdapter {

		@Override
		public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
			cause.printStackTrace();
			ctx.close();
		}

		@Override
		public void channelActive(ChannelHandlerContext ctx) throws Exception {
			for(int i=0; i<50; i++){
				ctx.writeAndFlush(NettyUtil.appenEndOfLine("Hello LineBasedFrameDecoder"));
			}
		}

		@Override
		public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
			try {
				System.out.println(msg);
			} finally {
				// 读完消息记得释放，那写消息为什么不这样操作呢，因为写完消息netty自动释放。
				// 其操作见：DefaultChannelHandlerInvoker L331-332,不过有这个注释-> promise cancelled
				// 是不少netty5正式发布的时候会取消呢。
				// 我们可以使用SimpleChannelInboundHandler作为父类，因为释放操作已实现。
				ReferenceCountUtil.release(msg);
			}

		}
	}
}