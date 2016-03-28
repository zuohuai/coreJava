package com.edu.zookeeper.aop;

import java.util.concurrent.TimeUnit;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.framework.state.ConnectionState;
import org.apache.curator.framework.state.ConnectionStateListener;
import org.aspectj.lang.ProceedingJoinPoint;

/**
 * 定义一把远程锁
 * 
 * @author Administrator
 *
 */
public class ClientLock implements ConnectionStateListener {
	/** 可重入锁 */
	private final InterProcessMutex lock;
	/** 定义方法调用的名字 */
	private final String clientName;

	public ClientLock(CuratorFramework client, String lockPath, String clientName) {
		this.clientName = clientName;
		this.lock = new InterProcessMutex(client, lockPath);
	}

	public Object doWork(ProceedingJoinPoint pjp, long time, TimeUnit unit) throws Throwable {
		long start = System.currentTimeMillis();
		if (!lock.acquire(time, unit)) {
			throw new IllegalStateException(clientName + " could not acquire the lock");
		}
		Object object = null;
		try {
			object = pjp.proceed(pjp.getArgs());
		} finally {
			lock.release();
		}
		long end = System.currentTimeMillis();
		System.out.println("每次请求, 获取锁和释放处理的时间是:" + (end - start) + "ms");

		return object;
	}

	@Override
	public void stateChanged(CuratorFramework client, ConnectionState newState) {
		System.err.println("远程连接发生变更了");
		// 连接状态发生变更,则需要强制释放锁
		if (newState != ConnectionState.CONNECTED) {
			if (lock != null) {
				try {
					System.out.println("连接被中断了,释放锁资源");
					lock.release(); // always release the lock in a finally
									// block
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		}
	}
}