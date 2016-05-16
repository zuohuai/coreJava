package com.edu.codis.redis;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.edu.codis.JedisResourcePoolFactory;

import redis.clients.jedis.Jedis;

@Component
public class RedisHelper {
	@Autowired
	private JedisResourcePoolFactory jedisFactory;

	private static final Logger LOGGER = LoggerFactory.getLogger(RedisHelper.class);

	private ExecutorService executor = Executors.newSingleThreadExecutor();

	public <T> T execute(RedisCallback<T> callback, Object... args) throws Exception {
		Jedis jedis = jedisFactory.getJedis();
		try {
			FutureTask<T> future = new FutureTask<T>(new Callable<T>() {// 使用Callable接口作为构造参数
				public T call() {
					try {
						// 真正的任务在这里执行，这里的返回值类型为String，可以为任意类型
						T result = callback.doInRedis(jedis, args);
						return result;
					} catch (Exception e) {
						LOGGER.error("执行redis任务异常:", new Object[] { e });
						return null;
					}
				}
			});
			executor.execute(future);
			return future.get(1000, TimeUnit.MILLISECONDS);
		} catch (Exception e) {
			throw e;
		} finally {
			jedisFactory.close(jedis);
		}
	}

	/**
	 * @param callback
	 * @param args
	 * @return
	 * @throws Exception
	 */
	public <T> T execute2(RedisCallback<T> callback, Object... args) throws Exception {
		try (Jedis jedis = jedisFactory.getJedis()) {
			FutureTask<T> future = new FutureTask<T>(new Callable<T>() {// 使用Callable接口作为构造参数
				public T call() {
					try {
						// 真正的任务在这里执行，这里的返回值类型为String，可以为任意类型
						T result = callback.doInRedis(jedis, args);
						return result;
					} catch (Exception e) {
						LOGGER.error("执行redis任务异常:", new Object[] { e });
						return null;
					}
				}
			});
			executor.execute(future);
			return future.get(1000, TimeUnit.MILLISECONDS);
		}
	}

}
