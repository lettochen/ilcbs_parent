package cn.itcast.test;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

public class SpringMailTest {

	public static void main(String[] args) {
		ApplicationContext ac = new ClassPathXmlApplicationContext("applicationContext-mail.xml");
		JavaMailSender sender = (JavaMailSender) ac.getBean("mailSender");
	
		SimpleMailMessage message = (SimpleMailMessage) ac.getBean("mailMessage");
		message.setTo("17610279727@163.com");
		message.setSubject("spring的javamailsender发送的");
		message.setText("spring发送简单很多");
		
		sender.send(message);
	}
}
