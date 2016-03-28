package com.edu.disruptor;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.lmax.disruptor.EventHandler;

/**
 * 用来中转事件
 * 
 * @author Administrator
 *
 */
@Component
public class ValueEventHandler implements EventHandler<Event<?>> {
	private static final Logger logger = LoggerFactory.getLogger(ValueEventHandler.class);
	/**用来缓存事件处理器*/
	private ConcurrentHashMap<String, CopyOnWriteArraySet<Receiver<?>>> receivers = new ConcurrentHashMap<String, CopyOnWriteArraySet<Receiver<?>>>();

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void onEvent(Event<?> event, long sequence, boolean endOfBatch) throws Exception {
		String name = event.getName();
		if (!receivers.containsKey(name)) {
			logger.warn("事件'{}'没有对应的接收器", name);
			return;
		}
		// 将事件分发到处理器中
		for (Receiver receiver : receivers.get(name)) {
			try {
				receiver.onEvent(event);
			} catch (Exception e) {
				logger.error("事件处理[{}]异常[{}]", new Object[] { name, e });
			}

		}
	}

	public void register(String name, Receiver<?> receiver) {
		if (name == null || receiver == null) {
			throw new IllegalArgumentException("事件名和接收者均不能为空");
		}

		CopyOnWriteArraySet<Receiver<?>> set = receivers.get(name);
		if (set == null) {
			set = new CopyOnWriteArraySet<Receiver<?>>();
			CopyOnWriteArraySet<Receiver<?>> prev = receivers.putIfAbsent(name, set);
			set = prev != null ? prev : set;
		}

		set.add(receiver);
	}

	public void unregister(String name, Receiver<?> receiver) {
		if (name == null || receiver == null) {
			throw new IllegalArgumentException("事件名和接收者均不能为空");
		}

		CopyOnWriteArraySet<Receiver<?>> set = receivers.get(name);
		if (set != null) {
			set.remove(receiver);
		}
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void syncPost(Event<?> event) {
		String name = event.getName();
		if (!receivers.containsKey(name)) {
			logger.warn("事件'{}'没有对应的接收器", name);
			return;
		}
		for (Receiver receiver : receivers.get(name)) {
			try {
				receiver.onEvent(event);
			} catch (Exception e) {
				logger.error("事件[" + event.getName() + "]处理时发生异常", e);
			}
		}
	}

}
