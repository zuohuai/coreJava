
package com.edu.codis.redis;

import redis.clients.jedis.Jedis;

/**
 * Redis访问回调函数
 * @author Administrator
 *
 * @param <T>
 */
public interface RedisCallback<T> {

	T doInRedis(Jedis jedis, Object... args) throws Exception;
}
