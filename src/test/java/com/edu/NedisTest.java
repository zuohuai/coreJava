package com.edu;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.edu.codis.NedisResourcePoolFactory;
import com.edu.codis.redis.RedisConfig;
import com.wandoulabs.nedis.NedisClient;
import com.wandoulabs.nedis.protocol.SetParams;
import com.wandoulabs.nedis.util.NedisUtils;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(value={"CodisTest-context.xml"})
public class NedisTest {
	@Autowired
	private RedisConfig redisConfig;
	@Autowired
	private NedisResourcePoolFactory nedisFactory;

	@Before
	public void before() {
		
	}
	
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
	 * 异步，设置超时时间
	 */
	@Test
	public void test_nedis_select_setParams() throws Exception{
		NedisClient nedis = nedisFactory.getNedis();
		//切换数据库
		nedis.select(0);
		
		SetParams setParams = new SetParams();
		setParams.setNx();
		setParams.setEx(10);
		for(int i=0; i<1000; i++){
			byte[] key = NedisUtils.toBytes("nedis_key_"+i);
			byte[] value = NedisUtils.toBytes("nedis_value_"+i);
			nedis.set(key, value, setParams);
		}
		nedis.close().sync();
	}
	
	@After
	public void destory() {
		nedisFactory.destroy();
	}
}
