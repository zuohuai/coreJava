package com.edu.zookeeper;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

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
	private static final int REPETITIONS = QTY * 10;
	private static final String PATH = "/examples/locks";

	/**
	 * error the @annotation pointcut expression is only supported at Java 5
	 * compliance level or above 兼容性，这个问题如何处理了？！
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

}
