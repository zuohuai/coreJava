package com.edu.netty.client;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.net.SocketServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.helpers.FormattingTuple;
import org.slf4j.helpers.MessageFormatter;

import com.edu.netty.SocketException;
import com.edu.netty.handler.SnGenerator;
import com.edu.utils.DelayedElement;
import com.edu.utils.NamedThreadFactory;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

public class ClientFactory {

	private static final Logger LOGGER = LoggerFactory.getLogger(ClientFactory.class);

	/** 当前全部可用客户端 */
	private final ConcurrentHashMap<String, SimpleClient> clients = new ConcurrentHashMap<String, SimpleClient>();
	/** 客户端操作锁 */
	private final ConcurrentHashMap<String, Lock> locks = new ConcurrentHashMap<String, Lock>();
	/** 过期客户端移除队列 */
	private final DelayQueue<DelayedElement<SimpleClient>> delays = new DelayQueue<DelayedElement<SimpleClient>>();
	/** SID 生成器 */
	private final SnGenerator sn = new SnGenerator();
	/** 最大重试次数，超过次数未连接则抛弃Client */
	private static final int MAX_RETRY = 10;
	/** 与{@link SocketServer}的连接 */
	private volatile Bootstrap connector = null;

	private EventLoopGroup group = null;
	/** 客户端过期时间(秒) */
	private int remoteTimes = 300;

	/** 回调集合 */
	@SuppressWarnings("rawtypes")
	private final ConcurrentHashMap<Long, SocketFuture> futures = new ConcurrentHashMap<Long, SocketFuture>();

	public void test_create_bean() {
		LOGGER.error("client-factory is created !!!");
	}

	@PostConstruct
	private void initialize() {

		bootstrap0();

		// 创建非活跃客户端清理线程
		Thread removeClientThread = new Thread(new Runnable() {
			@Override
			public void run() {
				while (!Thread.interrupted()) {

					try {
						DelayedElement<SimpleClient> e = delays.take();
						SimpleClient client = e.getContent();
						boolean delay = checkRemove(client);
						if (delay) {
							submitRemove(client);
						}
					} catch (InterruptedException e) {
						LOGGER.error("过期客户端清理线程被中断", e);
					} catch (Exception e) {
						LOGGER.error("过期客户端清理线程出现未知异常", e);
					}
				}
			}
		});
		removeClientThread.setDaemon(true);
		removeClientThread.start();
	}

	private void bootstrap0() {
		connector = new Bootstrap();

		// 设置会话配置

		group = new NioEventLoopGroup(1, new NamedThreadFactory("客户端NIO线程"));
		connector.group(group);
		connector.channel(NioSocketChannel.class);

		connector.handler(new ClientInitializer());
	}

	/**
	 * 提交移除非活跃客户端的移除任务
	 * @param client
	 */
	private void submitRemove(SimpleClient client) {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.SECOND, remoteTimes);

		DelayedElement<SimpleClient> element = DelayedElement.valueOf(client, calendar.getTime());
		delays.put(element);
	}

	private boolean checkRemove(SimpleClient client) {
		if (client == null || client.channel == null) {
			return false; // 客户端已经销毁
		}
		if (client.isKeepAlive()) {
			return false; // 长连接不需要销毁
		}
		if (!client.isIdle()) {
			// 有消息则等待回应,延时进行检查
			return true;
		}

		String address = client.getAddress();
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(client.getTimestamp());
		calendar.add(Calendar.SECOND, remoteTimes);
		Date now = new Date();
		Date check = calendar.getTime();
		if (check.after(now)) {
			// 客户端还处于活跃的状态,则延时在检查
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("客户端获取失效时间[{}]未到,等到下一次检查", check);
			}
			return true;
		}
		if (LOGGER.isWarnEnabled()) {
			LOGGER.warn("关于空闲超时客户端连接-{}", client.getAddress());
		}
		Lock lock = loadClientLock(address);
		try {
			// 根据地址来移除
			clients.remove(address, client);
		} finally {
			lock.unlock();
		}
		// 关闭连接，释放资源
		client.close();
		return false;
	}

	/**
	 * 获取指定地址对应的的通信客户端
	 * @param address
	 * @param keepAlive
	 * @return
	 */
	public Client getClient(String address, boolean keepAlive) {
		Lock lock = loadClientLock(address);
		lock.lock();
		try {
			SimpleClient client = clients.get(address);
			if (client != null) {
				client.timestamp = System.currentTimeMillis();
				// TODO 这句话有没有一点多余
				if (client.keepAlive != keepAlive) {
					client.keepAlive = keepAlive;
				}
			} else {
				client = createClient(address);

				SimpleClient prev = clients.putIfAbsent(address, client);
				if (prev != null && prev != client) {
					client = prev;
				} else {
					client.keepAlive = keepAlive;
				}
			}
			return client;
		} catch (Exception e) {
			// 移除客戶端锁
			removeClientLock(address);
			FormattingTuple message = MessageFormatter.format("无法获取指定服务器地址的客户端对象[{}]", address, e);
			LOGGER.error(message.getMessage());
			throw new RuntimeException(e.getMessage(), e);
		} finally {
			lock.unlock();
		}
	}

	private Lock loadClientLock(String address) {
		Lock result = locks.get(address);
		if (result != null) {
			return result;
		}

		result = new ReentrantLock();
		Lock prev = locks.putIfAbsent(address, result);
		return result != null ? result : prev;
	}

	/**
	 * 移除客户端锁
	 * @param address
	 */
	private void removeClientLock(String address) {
		locks.remove(address);
	}

	public SimpleClient createClient(String address) {
		InetSocketAddress socketAddress = toInetSocketAddress(address);
		return new SimpleClient(socketAddress);
	}

	private String fromInetSocketAddress(InetSocketAddress address) {
		InetAddress inetAddress = address.getAddress();
		return inetAddress == null ? "UNKONW_ADDRESS" : inetAddress.getHostAddress() + ":" + address.getPort();
	}

	private InetSocketAddress toInetSocketAddress(String text) {
		if (StringUtils.isEmpty(text)) {
			throw new IllegalArgumentException("无效的字符串地址" + text);
		}

		// TODO 地址格式检查，目前只是单一检查
		int col = text.indexOf(":");
		String host = text.substring(0, col);
		int port = Integer.parseInt(text.substring(col + 1));
		return new InetSocketAddress(host, port);
	}

	class SimpleClient implements Client {
		/** 连接地址 */
		private final InetSocketAddress address;
		/** 与当前服务器连接的会话 */
		private volatile Channel channel;
		/** 会话ID */
		private volatile long sessionId = -1;

		// private SendProxyFactory sendProxy; TODO 发送代理

		/** 重试次数 */
		private volatile int retry;
		/** 是否保持连接 */
		private boolean keepAlive = false;
		/** 最后操作的时间戳 */
		private volatile long timestamp = System.currentTimeMillis();

		public SimpleClient(InetSocketAddress address) {
			this.address = address;
		}

		@Override
		public synchronized void close() {
			if (channel == null || !channel.isActive()) {
				if (LOGGER.isDebugEnabled()) {
					LOGGER.debug("已经关闭与服务器[{}的连接]", address);
				}
				return;
			}

			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("准备关闭与服务器[{}]的连接", address);
			}

			// 关闭连接和释放对象资源
			ChannelFuture future = channel.close();
			future.awaitUninterruptibly();
			channel = null;
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("已经关闭与服务器[{}]的连接", address);
			}
		}

		@Override
		public synchronized void connect() {
			if (channel != null && channel.isActive()) {
				return;
			}

			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("开始连接服务器[{}]", address);
			}

			retry++;
			try {
				ChannelFuture future = connector.connect(address);
				future.sync();
				Channel channel = future.channel();
				sessionId = sn.next();
				this.channel = channel;
				if (LOGGER.isDebugEnabled()) {
					LOGGER.debug("与服务器[{}]连接成功,本地连接", address, channel.localAddress());
				}
				retry = 0;
				submitRemove(this);;
			} catch (Throwable e) {
				if (retry > MAX_RETRY) {
					close();
				}
				e.printStackTrace();
				throw new SocketException(e);
			}

		}

		@Override
		public boolean isKeepAlive() {
			return keepAlive;
		}

		@Override
		public long getTimestamp() {
			if (isKeepAlive()) {
				return System.currentTimeMillis();
			}
			return this.timestamp;
		}

		@Override
		public boolean isConnect() {
			if (channel == null) {
				return false;
			}
			if (channel.isActive()) {
				return true;
			}
			return false;
		}

		@Override
		public boolean isDisposed() {
			return channel == null;
		}

		@Override
		public boolean isIdle() {
			return futures == null || futures.isEmpty();
		}

		@Override
		public void disableKeepAlive() {
			keepAlive = false;
			submitRemove(this);
		}

		@Override
		public String getAddress() {
			return fromInetSocketAddress(address);
		}

		// 重写hashcode 和 equals方法
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + getOuterType().hashCode();
			result = prime * result + ((address == null) ? 0 : address.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			SimpleClient other = (SimpleClient) obj;
			if (!getOuterType().equals(other.getOuterType()))
				return false;
			if (address == null) {
				if (other.address != null)
					return false;
			} else if (!address.equals(other.address))
				return false;
			return true;
		}

		private ClientFactory getOuterType() {
			return ClientFactory.this;
		}

	}

}
