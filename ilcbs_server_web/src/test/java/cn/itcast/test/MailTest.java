package cn.itcast.test;

import java.util.Properties;

import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMessage.RecipientType;

import org.junit.Test;

import cn.itcast.utils.MailUtil;

public class MailTest {

	@Test
	public void sendMail() throws Exception{
		Properties props = new Properties();
		props.put("mail.smtp.host", "smtp.163.com");
		props.put("mail.smtp.auth", "true");
		Session session = Session.getInstance(props);
		session.setDebug(true);
		
		//准备邮件
		MimeMessage message = new MimeMessage(session);
		
		//发件人
		InternetAddress address = new InternetAddress("17610279727@163.com");
		message.setFrom(address);
		//收件人
		InternetAddress toAddr = new InternetAddress("itcast250@163.com");
		message.setRecipient(RecipientType.TO, toAddr);

		//主题
		message.setSubject("手动发送123ceshiss ");
		//内容
		message.setText("手写代码不依赖spring累死了123ceshi1234");
		
		Transport transport = session.getTransport("smtp");
		transport.connect("smtp.163.com", "17610279727@163.com", "297ITCAST");
		transport.sendMessage(message, message.getAllRecipients());
		transport.close();
	}
	
	@Test
	public void send(){
		MailUtil mailUtil = new MailUtil();
		try {
			mailUtil.sendMsg("17610279727@163.com", "hao", "test");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
