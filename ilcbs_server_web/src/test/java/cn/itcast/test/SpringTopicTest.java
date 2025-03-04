package cn.itcast.test;

import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext-mq.xml")
public class SpringTopicTest {

	@Autowired
	@Qualifier(value="jmsTopicTemplate")
	private JmsTemplate topicTemplate;
	
	@Test
	public void testTopicSend(){
		topicTemplate.send("itcast297_topic", new MessageCreator() {
			
			@Override
			public Message createMessage(Session session) throws JMSException {
				// TODO Auto-generated method stub
				//TextMessage message = session.createTextMessage("spring发送的消息");
				MapMessage map = session.createMapMessage();
				map.setString("username", "cgx");
				return map;
			}
		});
	}
	
	/**
	 * 手动调用的方式
	 * @throws JMSException
	 */
	@Test
	public void testTopicReceive() throws JMSException{
		MapMessage message = (MapMessage) topicTemplate.receive("itcast297_topic");
		System.out.println(message.getString("username"));
		System.out.println("执行结束");
	}
	
	
}
