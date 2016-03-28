package com.edu.zookeeper.test;

import java.util.concurrent.atomic.AtomicBoolean;

import org.springframework.stereotype.Component;

import com.edu.zookeeper.aop.AutoRemoteLocked;
import com.edu.zookeeper.aop.IsRemoteLock;

/**
 * 真实的业务方法
 * 
 * @author Administrator
 *
 */
@Component
public class Business {

	private final AtomicBoolean inUse = new AtomicBoolean(false);

	@AutoRemoteLocked
	public Object execute(@IsRemoteLock String path, String helloWorld) {
		// 真实环境中我们会在这里访问/维护一个共享的资源
		// 这个例子在使用锁的情况下不会非法并发异常IllegalStateException
		// 但是在无锁的情况由于sleep了一段时间，很容易抛出异常
		if (!inUse.compareAndSet(false, true)) {
			System.err.println("出现了同步异常了 !!!");
			System.exit(-1);
			throw new IllegalStateException("Needs to be used by one client at a time");
			
		}
		try {
			Thread.sleep((long) (3 * Math.random()));
			System.out.println("逻辑处理");
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			inUse.set(false);
		}
		return new String("Hello World");
	}
}
