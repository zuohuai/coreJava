package com.edu.redis.pub;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class Program {

    public static final String CHANNEL_NAME = "commonChannel1";

    private static Logger logger = LoggerFactory.getLogger(Program.class);

    public static void main(String[] args) throws Exception {

        JedisPoolConfig poolConfig = new JedisPoolConfig();

        JedisPool jedisPool = new JedisPool(poolConfig, "192.168.60.56", 6380, 0);

        final Jedis subscriberJedis = jedisPool.getResource();
        subscriberJedis.auth("test_password");

        final Subscriber subscriber = new Subscriber();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    logger.info("Subscribing to \"commonChannel\". This thread will be blocked.");
                   // subscriberJedis.quit();
                   subscriberJedis.subscribe(subscriber, CHANNEL_NAME);
                    logger.info("Subscription ended.");
                } catch (Exception e) {
                    logger.error("Subscribing failed.", e);
                }
            }
        }).start();

        Jedis publisherJedis = jedisPool.getResource();
        publisherJedis.auth("test_password");

        new Publisher(publisherJedis, CHANNEL_NAME).start();

        subscriber.unsubscribe();
        jedisPool.returnResource(subscriberJedis);
        jedisPool.returnResource(publisherJedis);
    }
}