package com.edu.netty.conf;

import com.edu.utils.NamedThreadFactory;

import io.netty.util.concurrent.DefaultEventExecutorGroup;
import io.netty.util.concurrent.EventExecutorGroup;

/**
 * 执行配置管理
 * @author Administrator
 *
 */
public class ExecutorConfig {

	private int min;
	
	private int max;
	
	private long idle;

	public static ExecutorConfig valueOf(int min, int max, long idle){
		ExecutorConfig executorConfig = new ExecutorConfig();
		executorConfig.min = min;
		executorConfig.max = max;
		executorConfig.idle = idle;
		return executorConfig;
	}
	
	/**
	 * 创建一个{@link EventExecutorGroup}
	 * @param groupName
	 * @param threadName
	 * @return
	 */
	public EventExecutorGroup build(String groupName, String threadName){
		ThreadGroup threadGroup = new ThreadGroup(groupName);
		NamedThreadFactory threadFactory = new NamedThreadFactory(threadGroup, threadName);
		int size = Math.max(min, max);
		EventExecutorGroup eventExecutor = new DefaultEventExecutorGroup(size, threadFactory);
		return eventExecutor;
	}
	
	public int getMin() {
		return min;
	}

	public int getMax() {
		return max;
	}

	public long getIdle() {
		return idle;
	}
	
}
