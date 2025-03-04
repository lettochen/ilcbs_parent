package cn.itcast.test;

import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.Topic;

import org.apache.activemq.spring.ActiveMQConnectionFactory;
import org.junit.Test;

public class TopicTest {

	@Test
	public void testTopicSend() throws JMSException{
		ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory();
		Connection connection = factory.createConnection();
		connection.start();
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		Topic topic = session.createTopic("itcast297_topic");
		MessageProducer producer = session.createProducer(topic);
		for (int i = 0; i < 10; i++) {
			MapMessage message = session.createMapMessage();
			message.setString("username", "cgx");
			message.setString("password", "123456");
			producer.send(message);
		}
		session.close();
		connection.close();
	}
	
	@Test
	public void testTopicReceiver() throws JMSException{
		ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory();
		Connection connection = factory.createConnection();
		connection.start();
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		Topic topic = session.createTopic("itcast297_topic");
		MessageConsumer consumer = session.createConsumer(topic);
		MapMessage message = (MapMessage) consumer.receive();
		System.out.println(message.getString("username"));
		session.close();
		connection.close();
	}
	
	@Test
	public void testTopicReceiverListener1() throws JMSException{
		ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory();
		Connection connection = factory.createConnection();
		connection.start();
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		Topic topic = session.createTopic("itcast297_topic");
		MessageConsumer consumer = session.createConsumer(topic);
		consumer.setMessageListener(new MessageListener() {
			
			@Override
			public void onMessage(Message arg0) {
				// TODO Auto-generated method stub
				try {
					MapMessage message = (MapMessage) arg0;
					System.out.println("Listener1=====用户名是："+message.getString("username"));
				} catch (JMSException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		
		while(true);
	}
	
	@Test
	public void testTopicReceiverListener2() throws JMSException{
		ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory();
		Connection connection = factory.createConnection();
		connection.start();
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		Topic topic = session.createTopic("itcast297_topic");
		MessageConsumer consumer = session.createConsumer(topic);
		consumer.setMessageListener(new MessageListener() {
			
			@Override
			public void onMessage(Message arg0) {
				// TODO Auto-generated method stub
				try {
					MapMessage message = (MapMessage) arg0;
					System.out.println("Listener2=====用户名是："+message.getString("username"));
				} catch (JMSException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		
		while(true);
	}
}
