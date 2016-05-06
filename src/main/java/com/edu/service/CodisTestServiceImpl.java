package com.edu.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import redis.clients.jedis.Jedis;

import com.edu.codis.redis.RedisConfig;
import com.edu.manager.PlayerFighter;
import com.edu.orm.Accessor;
import com.edu.orm.Querier;
import com.edu.utils.UTF8Utils;
import com.wandoulabs.jodis.JedisResourcePool;
import com.wandoulabs.jodis.RoundRobinJedisPool;

@Service
public class CodisTestServiceImpl {
	private static final Logger logger = LoggerFactory
			.getLogger(CodisTestServiceImpl.class);
	@Autowired
	private Accessor accessor;
	@Autowired
	private Querier querier;
	@Autowired
	private RedisConfig redisConfig;

	private JedisResourcePool jedisResourcePool;

	public List<String> getAllPlayerFighterIds() {
		List<Object> queryResult = querier.list(PlayerFighter.class,
				Object.class, PlayerFighter.IDS);
		List<String> result = new ArrayList<String>(queryResult.size());
		for (Object obj : queryResult) {
			result.add(UTF8Utils.toUniqueKey(obj.toString()).toLowerCase());
		}
		logger.error("从数据库中加载唯一ID到数量是：[{}]", new Object[] { result.size() });
		return result;
	}

	public PlayerFighter getPlayerFighterById(String id) {
		return accessor.load(PlayerFighter.class, id);
	}

	public void testFirstCodisLoadAndSave() throws Exception {
		// 启动线程池来处理
		JedisResourcePool jedisResourcePool = RoundRobinJedisPool.create()
				.curatorClient(redisConfig.getAddress(), 30000)
				.zkProxyDir(redisConfig.getProxy()).build();
		for (int j = 0; j < 10000; j++) {
			Jedis jedis = jedisResourcePool.getResource();
			for (int i = 0; i < 30000; i++) {
				String key = "foo" + i;
				jedis.set(key, "bar" + i);
				System.out.println(jedis.get(key));
				Thread.sleep(1);
			}
		}

		jedisResourcePool.close();
	}

	public void testSavePlayerFighterToCodis() {
		List<String> result = getAllPlayerFighterIds();
		int i = 1;
		for (String id : result) {
			try {
				PlayerFighter playerFighter = getPlayerFighterById(id);
				JedisResourcePool jedisResourcePool = RoundRobinJedisPool
						.create()
						.curatorClient("localhost.localdomain:2181", 30000)
						.zkProxyDir("/zk/codis/db_test/proxy").build();
				Jedis jedis = jedisResourcePool.getResource();
				byte[] key = playerFighter.getId().getBytes("utf-8");
				byte[] value = playerFighter.getContent();
				long start = System.currentTimeMillis();
				jedis.set(key, value);
				value = jedis.get(key);
				long end = System.currentTimeMillis();
				logger.error("执行第" + i + "次保存到codis和从codis中加载的耗时是:"
						+ (end - start) + "ms");
				i++;
				jedisResourcePool.close();
			} catch (Exception exception) {
				exception.printStackTrace();
			}
		}
	}

	public JedisResourcePool getJedisResourcePool() {
		return jedisResourcePool;
	}
}
