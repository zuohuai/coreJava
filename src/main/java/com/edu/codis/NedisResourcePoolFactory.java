package com.edu.codis;

import javax.annotation.PreDestroy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import com.edu.codis.redis.RedisConfig;
import com.wandoulabs.nedis.NedisClient;
import com.wandoulabs.nedis.NedisClientPoolBuilder;
import com.wandoulabs.nedis.codis.RoundRobinNedisClientPool;
import com.wandoulabs.nedis.util.NedisUtils;

/**
 * nedis连接工厂
 * 
 * @author jy
 * 
 */
@Component
public class NedisResourcePoolFactory implements
		ApplicationListener<ContextRefreshedEvent> {
	@Autowired
	private RedisConfig redisConfig;
	/** redis连接池 */
	private RoundRobinNedisClientPool nedisResourcePool;

	private static final Logger LOGGER = LoggerFactory
			.getLogger(NedisResourcePoolFactory.class);

	private boolean init = false;

	public NedisClient getNedis() {
		if (nedisResourcePool == null) {
			return null;
		}
		//用户名和密码验证
		NedisClient nedisClient =  NedisUtils.newPooledClient(nedisResourcePool);
		return nedisClient;
	}

	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		if (!init) {
			try {
				//设置密码
				NedisClientPoolBuilder poolBuilder = NedisClientPoolBuilder.create();
				poolBuilder.password(redisConfig.getPassword());
				
				nedisResourcePool = RoundRobinNedisClientPool.builder()
						.poolBuilder(poolBuilder.timeoutMs(5000))
				        .curatorClient(redisConfig.getAddress(), 30000)
				        .zkProxyDir(redisConfig.getProxy()).build().sync().getNow();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	@PreDestroy
	public void destroy() {
		if (nedisResourcePool != null) {
			try {
				nedisResourcePool.close();
			} catch (Exception e) { 
				LOGGER.error("关闭redis连接池出现异常", e);
			}
		}
	}
}
