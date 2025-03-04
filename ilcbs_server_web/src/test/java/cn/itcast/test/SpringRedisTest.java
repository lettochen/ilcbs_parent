package cn.itcast.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext.xml")
public class SpringRedisTest {

	@Autowired
	RedisTemplate<String, String> redisTemplage;
	
	@Test
	public void testRedis(){
		String string = redisTemplage.opsForValue().get("genzTreeNodes_4028a1cd4ee2d9d6014ee2df4c6a0000");
		System.out.println(string);

	}
}
