package cn.itcast.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext-mq-consumer.xml")
public class SpringActiveMQListener {

	@Test
	public void testRun(){
		while(true);
	}
}
