package com.edu.codis;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.edu.utils.json.JsonUtils;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * Jedis 操作的模板类
 * @author Frank
 */
public abstract class JedisTemplate {

	private static final Logger LOGGER = LoggerFactory.getLogger(JedisTemplate.class);

	/** 获取 Jedis 客户端 */
	public static Jedis getJedis(JedisPool pool, int idx) {
		Jedis jedis = null;
		try {
			jedis = pool.getResource();
			jedis.select(idx);
			return jedis;
		} catch (RuntimeException e) {
			LOGGER.error("获取 Redis 客户端连接失败:" + e.getMessage(), e);
			throw e;
		}
	}

	/** 获取 Jedis 客户端 */
	public static Jedis getJedis(JedisPool pool) {
		return getJedis(pool, 0);
	}

	/** 在指定数据库执行操作 */
	public static <R> R execute(JedisPool pool, int idx, Function<Jedis, R> callback) {
		try (Jedis jedis = getJedis(pool, idx)) {
			return callback.apply(jedis);
		}
	}

	/** 在默认数据库执行操作 */
	public static <R> R execute(JedisPool pool, Function<Jedis, R> callback) {
		return execute(pool, 0, callback);
	}
	
	/** 转换为 hash 的存储格式 */
	public static Map<String, String> toSave(Map<String, Object> map) {
		HashMap<String, String> ret = new HashMap<String, String>(map.size());
		for (Entry<String, Object> entry : map.entrySet()) {
			String key =  entry.getKey();
			Object value = entry.getValue();
			if (value == null)
				continue;
			Class<?> clz = value.getClass();
			if (clz.equals(String.class)) {
				ret.put(key, (String) value);
			} else if (clz.equals(LocalDate.class)) {
				ret.put(key, ((LocalDate) value).toString());
			} else {
				ret.put(key, JsonUtils.object2String(value));
			}
		}
		return ret;
	}

	/** 创建 Jedis 连接 */
	public static Jedis connect(String hostPortIndex, int timeout) {
		String[] ss = hostPortIndex.split(":");
		Jedis ret = null;
		switch (ss.length) {
		case 1:
			ret = new Jedis(ss[0], 6379, timeout);
			break;
		case 2:
			ret = new Jedis(ss[0], Integer.valueOf(ss[1]), timeout);
			break;
		case 3:
			ret = new Jedis(ss[0], Integer.valueOf(ss[1]), timeout);
			ret.select(Integer.valueOf(ss[2]));
			break;
		default:
			throw new RuntimeException("无法解析的 Redis 连接信息[" + hostPortIndex + "]");
		}
		return ret;
	}

	/** 创建 Jedis 连接 */
	public static Jedis connect(String hostPortIndex) {
		String[] ss = hostPortIndex.split(":");
		Jedis ret = null;
		switch (ss.length) {
		case 1:
			ret = new Jedis(ss[0]);
			break;
		case 2:
			ret = new Jedis(ss[0], Integer.valueOf(ss[1]));
			break;
		case 3:
			ret = new Jedis(ss[0], Integer.valueOf(ss[1]));
			ret.select(Integer.valueOf(ss[2]));
			break;
		default:
			throw new RuntimeException("无法解析的 Redis 连接信息[" + hostPortIndex + "]");
		}
		return ret;
	}

}
