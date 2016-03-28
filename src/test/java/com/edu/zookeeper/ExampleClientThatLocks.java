package com.edu.zookeeper;
import java.util.concurrent.TimeUnit;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.framework.state.ConnectionState;
import org.apache.curator.framework.state.ConnectionStateListener;

public class ExampleClientThatLocks implements ConnectionStateListener{
    private final InterProcessMutex lock;
    
    private final FakeLimitedResource resource;
    
    private final String clientName;

    public ExampleClientThatLocks(CuratorFramework client, String lockPath, FakeLimitedResource resource, String clientName) {
        this.resource = resource;
        this.clientName = clientName;
        lock = new InterProcessMutex(client, lockPath);
    }

    public void doWork(long time, TimeUnit unit) throws Exception {
    	long start = System.currentTimeMillis();
        if (!lock.acquire(time, unit)) {
            throw new IllegalStateException(clientName + " could not acquire the lock");
        }
        try {
            System.out.println(clientName + " has the lock");
            resource.use(); //access resource exclusively
        } finally {
            System.out.println(clientName + " releasing the lock");
            lock.release(); // always release the lock in a finally block
        }
        long end = System.currentTimeMillis();
        System.out.println("每次请求, 获取锁和释放处理的时间是:" + (end - start)+"ms");
    }

	@Override
	public void stateChanged(CuratorFramework client, ConnectionState newState){
		if (newState != ConnectionState.CONNECTED) {
			if(lock != null){
				try {
					System.out.println("连接被中断了,释放锁资源");
					lock.release(); // always release the lock in a finally block
				} catch (Exception e) {
					e.printStackTrace();
				}
				
			}
		}
	}
}