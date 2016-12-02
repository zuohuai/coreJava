package com.edu.zookeeper;

import java.nio.charset.Charset;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.ZooKeeper.States;
import org.apache.zookeeper.data.Stat;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.edu.zookeeper.test.Business;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(value = { "ZookeeperTest-context.xml" })
public class ZookeeperTest {
	@Autowired
	private Business business;

	private static final int QTY = 100;

	/**
	 * 实现了一套分布式的锁 spring + CuratorFramework 实现一个分布式锁， 性能在50ms
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

	private static final String ROOT = "/zk";
	private static final String CHILD1 = "/zk/child1";
	private static final String CHILD2 = "/zk/child2";
	// 定义远程连接地址
	public static final String SERVER = "10.199.200.63:2181";
	@Autowired
	private ZkWatcher zkWatcher;

	@Test
	public void test_create_node() throws Exception {
		ZooKeeper zooKeeper = new ZooKeeper(SERVER, 300, zkWatcher);
		if (zooKeeper.getState() != States.CONNECTED) {
			Thread.sleep(3000L);
		}
		if (zooKeeper.exists(ROOT, true) == null) {
			// 1.持久化节点
			zooKeeper.create(ROOT, "root".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
		}
		if (zooKeeper.exists(CHILD1, true) == null) {
			// 2.临时节点，链接中断则消失
			zooKeeper.create(CHILD1, "child1".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
			// Thread.sleep(3000);
		}

		if (zooKeeper.exists(CHILD2, true) == null) {
			// 3.持久化，序列化节点
			zooKeeper.create(CHILD2, "child2".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT_SEQUENTIAL);
			List<String> child = zooKeeper.getChildren(ROOT, true);
			for (String childStr : child) {
				System.out.println(childStr);
			}
		}

		zooKeeper.close();
	}

	private static final String PASSWORD = "rightKey";
	private static final String WRONG = "wrongKey";
	private static final String AUTH_TYPE = "digest";

	private static final String AUTH = "/zk/auth";

	@Test
	public void test_auth() throws Exception {
		ZooKeeper zooKeeper = new ZooKeeper(SERVER, 300, zkWatcher);
		if (zooKeeper.getState() != States.CONNECTED) {
			Thread.sleep(3000L);
		}
		// 1.授权
		zooKeeper.addAuthInfo(AUTH_TYPE, PASSWORD.getBytes());
		if (zooKeeper.exists(AUTH, true) == null) {
			// 2.创建对应的路径
			zooKeeper.create(AUTH, "auth".getBytes(), Ids.CREATOR_ALL_ACL, CreateMode.PERSISTENT);
		}
		zooKeeper.close();

		// 新链接，正确密码来获取数据
		zooKeeper = new ZooKeeper(SERVER, 300, zkWatcher);
		zooKeeper.addAuthInfo(AUTH_TYPE, PASSWORD.getBytes());
		String data = StringUtils.toEncodedString(zooKeeper.getData(AUTH, true, null), Charset.defaultCharset());
		System.out.println(data);
		zooKeeper.close();

		// 新链接，错误密码来获取数据
		try {
			zooKeeper = new ZooKeeper(SERVER, 300, zkWatcher);
			zooKeeper.addAuthInfo(AUTH_TYPE, WRONG.getBytes());
			data = StringUtils.toEncodedString(zooKeeper.getData(AUTH, true, null), Charset.defaultCharset());
			System.out.println(data);
		} finally {
			zooKeeper.close();
		}
	}

	private static final String DELETE = "/zk/delete";

	@Test
	public void test_delete() throws Exception {
		ZooKeeper zooKeeper = new ZooKeeper(SERVER, 300, zkWatcher);
		if (zooKeeper.getState() != States.CONNECTED) {
			Thread.sleep(3000L);
		}
		Stat stat = zooKeeper.exists(DELETE, true);
		if (stat == null) {
			System.out.println("执行创建节点");
			zooKeeper.create(DELETE, "delete".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
		}
		if (stat != null) {
			System.out.println("执行删除节点");
			zooKeeper.delete(DELETE, -1);
		}
		zooKeeper.close();
	}

	private static final String UPDATE = "/zk/update";

	@Test
	public void test_update_data() throws Exception {
		ZooKeeper zooKeeper = new ZooKeeper(SERVER, 300, zkWatcher);
		if (zooKeeper.getState() != States.CONNECTED) {
			Thread.sleep(3000L);
		}
		Stat stat = zooKeeper.exists(UPDATE, true);
		if (stat == null) {
			zooKeeper.create(UPDATE, "update1".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
		}

		zooKeeper.setData(UPDATE, "udpate2".getBytes(), -1);
		zooKeeper.close();
	}

	private static final String WATCH = "/zk/watch";
	
	/**
	 *
	 * test_watch:watch 如果如何保证数据不丢失？！ 如果连接中断了，watch 怎么处理 ？！<br/>  
	 *  
	 * @author hison.zhang  
	 * @throws Exception
	 */
	@Test
	public void test_watch() throws Exception {
		ZooKeeper zooKeeper = new ZooKeeper(SERVER, 300, zkWatcher);
		if (zooKeeper.getState() != States.CONNECTED) {
			Thread.sleep(3000L);
		}
		
		Stat stat = zooKeeper.exists(WATCH, true);
		if(stat == null){
			zooKeeper.create(WATCH, "watch".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
		}else{
			zooKeeper.setData(WATCH, "watch".getBytes(), -1);
		}
		
		//1.如果设置watch: fase; 后面的NodeDataChanged事件将不会被监听到
		byte[] data = zooKeeper.getData(WATCH, false, null);
		System.out.println(StringUtils.toEncodedString(data, Charset.defaultCharset()));
		zooKeeper.setData(WATCH, "watch2".getBytes(), -1);
		
		data = zooKeeper.getData(WATCH, true, null);
		System.out.println(StringUtils.toEncodedString(data, Charset.defaultCharset()));
	}
}
