package com.edu;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import redis.clients.jedis.Jedis;

import com.edu.codis.JedisResourcePoolFactory;
import com.edu.common.RedisConfig;
import com.edu.javaBean.Person;
import com.edu.utils.JsonUtils;
import com.edu.utils.RandomUtils;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class CodisTest {
	@Autowired
	private RedisConfig redisConfig;
	@Autowired
	private JedisResourcePoolFactory factory;
	private String sortSetRankName;
	private int min, max;

	@Before
	public void before() {
		sortSetRankName = "test_sort_set_rank";
		min = 1;
		max = 100;
	}

	private static final Logger LOGGER = LoggerFactory
			.getLogger(CodisTest.class);

	/**
	 * 环境搭建测试
	 */
	@Test
	public void test_redis_config() {
		assertThat(redisConfig.getId(), is(1));
	}

	/**
	 * 测试codis的远程连接 get 和set命令
	 * 
	 * @throws Exception
	 */
	@Test
	public void test_codis_connect() throws Exception {
		for (int j = 0; j < 10000; j++) {
			Jedis jedis = factory.getJedis();
			for (int i = 0; i < 30000; i++) {
				String key = "foo" + i;
				jedis.set(key, "bar" + i);
				System.out.println(jedis.get(key));
				Thread.sleep(1);
			}
		}
	}

	/**
	 * 测试sortSet的add操作
	 * 
	 * @throws Exception
	 */
	@Test
	public void test_sort_set_add() throws Exception {
		Jedis jedis = factory.getJedis();
		int min = 1, max = 100;

		for (int i = 0; i < 1000; i++) {
			int score = RandomUtils.betweenInt(min, max, true);
			Person person = Person.valueOf(i, "test_" + i, score);
			jedis.zadd(sortSetRankName, score, JsonUtils.object2String(person));
		}
		LOGGER.error("保存完成");
	}

	/**
	 * 测试sortSet的get操作
	 */
	@Test
	public void test_sort_set_get() {
		Jedis jedis = factory.getJedis();
		Set<String> scoreDatas = jedis.zrangeByScore(sortSetRankName, min, max);
		long size = jedis.zcard(sortSetRankName);
		assertEquals(size, scoreDatas.size());
	}

	@After
	public void destory() {
		factory.destroy();
	}
}
