package com.edu.zookeeper;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.nodes.PersistentEphemeralNode;
import org.apache.curator.framework.recipes.nodes.PersistentEphemeralNode.Mode;
import org.apache.curator.framework.state.ConnectionState;
import org.apache.curator.framework.state.ConnectionStateListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.test.KillSession;
import org.apache.curator.utils.CloseableUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.edu.codis.redis.RedisConfig;
import com.edu.zookeeper.test.Business;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(value = { "ZookeeperTest-context.xml" })
public class ZookeeperTest {
	@Autowired
	private Business business;
	//@Autowired
	//private RedisConfig redisConfig;

	private static final int QTY = 100;
	private static final int REPETITIONS = QTY * 10;
	private static final String PATH = "/examples/locks";
	

	/**
	 * error the @annotation pointcut expression is only supported at Java 5
	 * compliance level or above 兼容性，这个问题如何处理了？！ 实现了一套分布式的锁 spring +
	 * CuratorFramework 实现一个分布式锁， 性能在50ms
	 * 
	 * @throws Exception
	 */
	@Test
	public void test_remote_lock() throws Exception {
		ExecutorService service = Executors.newFixedThreadPool(QTY);
		try {
			for (int i = 0; i < QTY; ++i) {
				Callable<Void> task = new Callable<Void>() {
					@Override
					public Void call() throws Exception {
						String path = "/examples/locks";
						business.execute(path, "Hello World");
						return null;
					}
				};
				service.submit(task);
			}
			service.shutdown();
			service.awaitTermination(10, TimeUnit.MINUTES);
		} finally {

		}
	}

	private static final String PATH1 = "/example/ephemeralNode";
	private static final String PATH2 = "/example/node";
	// 定义远程连接地址
	private static final String server = ",192.168.60.81:2181";
	
	/**
	 * 临时节点测试
	 * 
	 * @throws Exception
	 */
	@Test
	public void test_PersistentEphemeralNode() throws Exception {
		CuratorFramework client = null;
		PersistentEphemeralNode node = null;
		try {
			client = CuratorFrameworkFactory.newClient(server, new ExponentialBackoffRetry(1000, 3));
			client.getConnectionStateListenable().addListener(new ConnectionStateListener() {

				@Override
				public void stateChanged(CuratorFramework client, ConnectionState newState) {
					System.out.println("client 的连接状态e:" + newState.name());

				}
			});
			client.start();

			// http://zookeeper.apache.org/doc/r3.2.2/api/org/apache/zookeeper/CreateMode.html
			node = new PersistentEphemeralNode(client, Mode.EPHEMERAL, PATH1, "test".getBytes());
			node.start();
			node.waitForInitialCreate(3, TimeUnit.SECONDS);
			String actualPath = node.getActualPath();
			System.out.println("node " + actualPath + " value: " + new String(client.getData().forPath(actualPath)));

			client.create().forPath(PATH2, "persistent node".getBytes());
			System.out.println("node " + PATH2 + " value: " + new String(client.getData().forPath(PATH2)));
			KillSession.kill(client.getZookeeperClient().getZooKeeper());
			System.out.println(
					"node " + actualPath + " doesn't exist: " + (client.checkExists().forPath(actualPath) == null));
			System.out.println("node " + PATH2 + " value: " + new String(client.getData().forPath(PATH2)));

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			CloseableUtils.closeQuietly(node);
			CloseableUtils.closeQuietly(client);
		}
	}

}
