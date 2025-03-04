package cn.itcast.test;

import javax.jms.JMSException;
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
public class SpringQueueTest {

	@Autowired
	@Qualifier(value="jmsQueueTemplate")
	private JmsTemplate queueTemplate;
	
	@Test
	public void testQueueSend(){
		queueTemplate.send("itcast297_queue", new MessageCreator() {
			
			@Override
			public Message createMessage(Session session) throws JMSException {
				// TODO Auto-generated method stub
				TextMessage message = session.createTextMessage("spring发送的消息");
				return message;
			}
		});
	}
	
	@Test
	public void testQueueReceive() throws JMSException{
		TextMessage message = (TextMessage) queueTemplate.receive("itcast297_queue");
		System.out.println(message.getText());
	}
}
