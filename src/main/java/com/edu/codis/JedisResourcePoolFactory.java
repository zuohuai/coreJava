package com.edu.codis;

import java.io.IOException;

import javax.annotation.PreDestroy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import redis.clients.jedis.Jedis;

import com.edu.common.RedisConfig;
import com.wandoulabs.jodis.JedisResourcePool;
import com.wandoulabs.jodis.RoundRobinJedisPool;

/**
 * Jedis连接工厂
 * 
 * @author jy
 * 
 */
@Component
public class JedisResourcePoolFactory implements
		ApplicationListener<ContextRefreshedEvent> {
	@Autowired
	private RedisConfig redisConfig;
	/** redis连接池 */
	private JedisResourcePool jedisResourcePool;

	private static final Logger LOGGER = LoggerFactory
			.getLogger(JedisResourcePoolFactory.class);

	private boolean init = false;

	public Jedis getJedis() {
		if (jedisResourcePool == null) {
			return null;
		}
		return jedisResourcePool.getResource();
	}

	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		if (!init) {
			jedisResourcePool = RoundRobinJedisPool.create()
					.curatorClient(redisConfig.getAddress(), 30000)
					.zkProxyDir(redisConfig.getProxy()).build();
		}
	}

	public JedisResourcePool getJedisResourcePool() {
		return jedisResourcePool;
	}

	@PreDestroy
	public void destroy() {
		if (jedisResourcePool != null) {
			try {
				jedisResourcePool.close();
			} catch (IOException e) {
				LOGGER.error("关闭redis连接池出现异常", e);
			}
		}
	}
}
