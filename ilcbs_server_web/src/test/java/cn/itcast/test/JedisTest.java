package cn.itcast.test;

import java.util.Set;

import org.junit.Test;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class JedisTest {

	@Test
	public void testJedis(){
		Jedis jedis = new Jedis("127.0.0.1",6379);
		jedis.set("itcast297", "hello");
		System.out.println(jedis.get("itcast297"));
	}
	
	@Test
	public void testJedisPool(){
		JedisPoolConfig config = new JedisPoolConfig();
		config.setMaxIdle(10);
		config.setMaxTotal(30);
		JedisPool jedisPool = new JedisPool(config, "127.0.0.1");
		Jedis jedis = jedisPool.getResource();
		Set<String> keys = jedis.keys("*");
		for (String string : keys) {
			System.out.println(string);
		}
	}
}
