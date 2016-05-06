package com.edu.codis.redis;

import org.apache.poi.ss.formula.functions.T;
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

	public <T> T  execute(RedisCallback<T> callback, Object... args) throws Exception {
		Jedis jedis = jedisFactory.getJedis();
		try {
			T result = callback.doInRedis(jedis, args);
			return result;
		} catch (Exception e) {
			throw e;
		} finally {
			jedisFactory.close(jedis);
		}
	}
}
