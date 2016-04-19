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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.helpers.FormattingTuple;
import org.slf4j.helpers.MessageFormatter;

import com.edu.utils.DelayedElement;

import io.netty.channel.Channel;

public class ClientFactory {

	private static final Logger LOGGER = LoggerFactory.getLogger(ClientFactory.class);

	/** 当前全部可用客户端 */
	private final ConcurrentHashMap<String, SimpleClient> clients = new ConcurrentHashMap<String, SimpleClient>();
	/** 客户端操作锁 */
	private final ConcurrentHashMap<String, Lock> locks = new ConcurrentHashMap<String, Lock>();
	/** 过期客户端移除队列 */
	private final DelayQueue<DelayedElement<SimpleClient>> delays = new DelayQueue<DelayedElement<SimpleClient>>();

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
						if(delay){
							submintRemove(client);
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

	}

	/**
	 * 提交移除非活跃客户端的移除任务
	 * @param client
	 */
	private void submintRemove(SimpleClient client) {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.SECOND, remoteTimes);

		DelayedElement<SimpleClient> element = DelayedElement.valueOf(client, calendar.getTime());
		delays.put(element);
		;
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
	 * 
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
		} catch (Exception e) {
			// 移除客戶端锁
			removeClientLock(address);
			FormattingTuple message = MessageFormatter.format("无法获取指定服务器地址的客户端对象[{}]", address, e);
			LOGGER.error(message.getMessage());
			throw new RuntimeException(e.getMessage(), e);
		} finally {
			lock.unlock();
		}
		return null;
	}

	private Lock loadClientLock(String address) {
		Lock result = locks.get(address);
		if (result != null) {
			return result;
		}

		result = new ReentrantLock();
		Lock prev = locks.putIfAbsent(address, result);
		return result == null ? result : prev;
	}

	/**
	 * 移除客户端锁
	 * 
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
		public void close() {
			// TODO Auto-generated method stub

		}

		@Override
		public synchronized void connect() {
			// TODO Auto-generated method stub

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

			// submitRemove(this); TODO 提交延迟销毁
		}

		@Override
		public String getAddress() {
			return fromInetSocketAddress(address);
		}
	}

}
