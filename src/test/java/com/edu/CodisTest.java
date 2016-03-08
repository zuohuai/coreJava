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
import com.edu.codis.NedisResourcePoolFactory;
import com.edu.common.RedisConfig;
import com.edu.common.RedisConstant;
import com.edu.jackson.JsonUtils;
import com.edu.javaBean.Person;
import com.edu.utils.RandomUtils;
import com.wandoulabs.nedis.NedisClient;
import com.wandoulabs.nedis.util.NedisUtils;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class CodisTest {
	@Autowired
	private RedisConfig redisConfig;
	@Autowired
	private JedisResourcePoolFactory jedisFactory;
	@Autowired
	private NedisResourcePoolFactory nedisFactory;
	private String sortSetRankName;
	private int min, max;

	@Before
	public void before() {
		sortSetRankName = "test_sort_set_rank";
		min = 1;
		max = 100;
	}

	private static final Logger LOGGER = LoggerFactory.getLogger(CodisTest.class);

	/**
	 * 测试nedis
	 * @throws Exception
	 */
	@Test
	public void test_nedis_connect() throws Exception {
		NedisClient nedis = nedisFactory.getNedis();
		nedis.set(NedisUtils.toBytes("foo"), NedisUtils.toBytes("bar")).sync();
		byte[] value = nedis.get(NedisUtils.toBytes("foo")).sync().getNow();
		System.out.println(NedisUtils.bytesToString(value));
		nedis.close().sync();
	}

	
	/**
	 * 环境搭建测试
	 */
	@Test
	public void test_redis_config() {
		assertThat(redisConfig.getId(), is(1));
	}

	/**
	 * 测试codis的远程连接 get 和set命令
	 * @throws Exception
	 */
	@Test
	public void test_codis_connect() throws Exception {
		Jedis jedis = jedisFactory.getJedis();
		String data =  jedis.get("town_rank:3161783124623363");
		System.out.println(data);
		
	}

	/**
	 * 测试sortSet的add操作
	 * @throws Exception
	 */
	@Test
	public void test_sort_set_add() throws Exception {
		Jedis jedis = jedisFactory.getJedis();
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
		Jedis jedis = jedisFactory.getJedis();
		Set<String> scoreDatas = jedis.zrangeByScore(sortSetRankName, min, max);
		long size = jedis.zcard(sortSetRankName);
		assertEquals(size, scoreDatas.size());
	}

	@Test
	public void test_proxy_connect() throws Exception {
		for (int i = 0; i < 100000; i++) {
			// 连接到LVS的VIP
			Jedis jedis = new Jedis("192.198.56.130", 19000);
			jedis.auth("zzh1234");
			String key = "key" + i;
			jedis.set(key, "value_" + key);
			System.out.println(jedis.get(key));
			jedis.close();
		}

	}

	@Test
	public void test_sort_revrange() throws Exception {
		Jedis jedis = jedisFactory.getJedis();
		String key = "page_rank";
		int start = 0;
		int end = 100;
		Set<String> result = jedis.zrevrange(key, start, end);
		for (String value : result) {
			System.out.println(value);
		}
	}

	/**
	 * 选择数据库的测试
	 * @throws Exception
	 */
	@Test
	public void test_select_db() throws Exception{
		Jedis jedis = jedisFactory.getJedis();
		jedis.select(0);
		for(int i = 0; i < 100 ; i++){
			jedis.set("select_db_key"+i, "select_db_value"+i, RedisConstant.NX_NOT_EXIST, RedisConstant.EX_S, 60);
		}
		jedis.close();
	}
	
	@After
	public void destory() {
		jedisFactory.destroy();
	}
}
