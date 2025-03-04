package cn.itcast.test;

import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.junit.Test;

public class QueueTest {

	@Test
	public void testQueueSend() throws JMSException{
		
		ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory();
		Connection connection = factory.createConnection();
		// 开启连接
		connection.start();
		//创建session    arg0:是否用事务     第二个参数：应答的方式   常用自动应答，或者手动应答
		Session session = connection.createSession(true, Session.AUTO_ACKNOWLEDGE);
		
		Queue queue = session.createQueue("itcast297_queue");

		MessageProducer producer = session.createProducer(queue);
		// 从session中准备一条文本消息
		for (int i = 0; i < 10; i++) {
			TextMessage message = session.createTextMessage();
			message.setText("hello!!!!!=======" + i);
			// 通过生产者发送该消息
			producer.send(message);
		}

		session.commit();
		// 关闭各种连接
		session.close();
		connection.close();
	}
	
	/**
	 * 手动调用receive方法接收消息
	 * @throws JMSException
	 */
	@Test
	public void testQueueReceiver() throws JMSException{
		ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory();
		Connection connection = factory.createConnection();
		connection.start();
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		Queue queue = session.createQueue("itcast297_queue");
		MessageConsumer consumer = session.createConsumer(queue);
		while(true){
			TextMessage message = (TextMessage) consumer.receive();
			if(message != null){
				String text = message.getText();
				System.out.println(text);
			}else{
				break;
			}
		}
		System.out.println("关闭连接资源");
		session.commit();
		session.close();
		connection.close();
	}

	@Test
	public void testQueueReceiveListener() throws JMSException{
		ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory();
		Connection connection = factory.createConnection();
		connection.start();
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		Queue queue = session.createQueue("itcast297_queue");
		MessageConsumer consumer = session.createConsumer(queue);
		consumer.setMessageListener(new MessageListener() {
			
			@Override
			public void onMessage(Message arg0) {
				// TODO Auto-generated method stub
				try {
					TextMessage message = (TextMessage) arg0;
					System.out.println(message.getText());
				} catch (JMSException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		while(true);
		/*session.commit();
		session.close();
		connection.close();*/
	}
	
}
