package com.edu.disruptor;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.annotation.PreDestroy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import com.edu.utils.NamedThreadFactory;
import com.lmax.disruptor.BatchEventProcessor;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.SequenceBarrier;
import com.lmax.disruptor.YieldingWaitStrategy;

/**
 * 事件总线接口的实现类
 * 
 * @author Frank
 */
@Component("EventBusImpl2")
public class EventBusImpl implements EventBus, EventBusImplMBean, ApplicationListener<ContextRefreshedEvent> {

	private static final Logger logger = LoggerFactory.getLogger(EventBusImpl.class);

	/** 注册的事件接收者 */
	@Autowired(required = false)
	@Qualifier("event_queue_size")
	private Integer queueSize = 8192 * 2;
	@Autowired(required = false)
	@Qualifier("event_pool_size")
	private Integer poolSize = 5;
	@Autowired(required = false)
	@Qualifier("event_pool_max_size")
	private Integer poolMaxSize = 10;
	@Autowired(required = false)
	@Qualifier("event_pool_alive_time")
	private Integer poolKeepAlive = 60;
	@Autowired(required = false)
	@Qualifier("event_pool_await_time")
	private Integer poolAwaitTime = 60;

	private ExecutorService pool;

	@Autowired
	private ValueEventHandler valueEventHandler;
	/** 避免启动重复加载的情况 */
	private volatile boolean init = false;

	// 创建一个RingBuffer
	private RingBuffer<Event<Object>> ringBuffer = null;

	// 创建线程池
	private ExecutorService executors = null;

	private SequenceBarrier barrier = null;
	/** 创建事件处理器 */
	private BatchEventProcessor<ValueEvent> eventProcessor = null;

	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		if (!init) {
			ThreadGroup threadGroup = new ThreadGroup("事件模块");
			NamedThreadFactory threadFactory = new NamedThreadFactory(threadGroup, "事件处理");
			executors = new ThreadPoolExecutor(poolSize, poolMaxSize, poolKeepAlive, TimeUnit.SECONDS,
					new LinkedBlockingQueue<Runnable>(queueSize), threadFactory);
			ringBuffer = RingBuffer.createMultiProducer(Event.EVENT_FACTORY, queueSize, new YieldingWaitStrategy());
			barrier = ringBuffer.newBarrier();

			BatchEventProcessor<Event<Object>> eventProcessor = new BatchEventProcessor<Event<Object>>(ringBuffer,
					barrier, valueEventHandler);
			ringBuffer.addGatingSequences(eventProcessor.getSequence());
			executors.submit(eventProcessor);

			init = true;
		}
	}

	/** 销毁方法 */
	@PreDestroy
	public void destory() {
		shutdown();
	}

	/**
	 * 关闭事件总线，阻塞方法会等待总线中的全部事件都发送完后再返回
	 */
	public void shutdown() {
		// 等待线程池关闭
		eventProcessor.halt();
		// 通知事件(或者说消息)处理器 可以结束了（并不是马上结束!!!）
		pool.shutdown();

		logger.error("开始关闭事件总线线程池");
		try {
			if (!pool.awaitTermination(poolAwaitTime, TimeUnit.SECONDS)) {
				logger.error("无法在预计时间内完成事件总线线程池关闭,尝试强行关闭");
				pool.shutdownNow();
				if (!pool.awaitTermination(poolAwaitTime, TimeUnit.SECONDS)) {
					logger.error("事件总线线程池无法完成关闭");
				}
			}
		} catch (InterruptedException e) {
			logger.error("事件总线线程池关闭时线程被打断,强制关闭事件总线线程池");
			pool.shutdownNow();
		}
	}

	@Override
	public void post(Event<?> event) {
		if (!init) {
			logger.error("事件处理模块未能正常初始化，请稍后提交");
			return;
		}
		if (event == null) {
			throw new IllegalArgumentException("事件对象不能为空");
		}
		// 检查剩余RingBuffer容量,用来规避超过容量被覆盖的问题
		if (ringBuffer.remainingCapacity() > queueSize * 0.1) {
			// TODO 存在对象冗余，怎么办?
			long seq = ringBuffer.next();// 占个坑 --ringBuffer一个可用区块
			Event<Object> result = ringBuffer.get(seq);
			result.setBody(event.getBody());// 给这个区块放入
											// 数据,如果此处不理解，想想RingBuffer的结构图
			result.setName(event.getName());
			ringBuffer.publish(seq);// 发布这个区块的数据使handler(consumer)可见
		} else {
			logger.error("事件的大小已经超过了RingSize{[]}的90%, 将直接调用", new Object[] { queueSize });
			syncPost(event);
		}
	}

	@Override
	public void syncPost(Event<?> event) {
		valueEventHandler.syncPost(event);
	}

	// JMX管理接口的实现方法
	@Override
	public int getEventQueueSize() {
		return ringBuffer.getBufferSize();
	}

	@Override
	public int getPoolActiveCount() {
		return ((ThreadPoolExecutor) pool).getActiveCount();
	}

	@Override
	public int getPollQueueSize() {
		return ((ThreadPoolExecutor) pool).getQueue().size();
	}

	@Override
	public List<String> getEvents() {
		// TODO
		ArrayList<String> result = new ArrayList<String>();
		return result;
	}
}
