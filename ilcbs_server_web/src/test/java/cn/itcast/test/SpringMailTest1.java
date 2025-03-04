package cn.itcast.test;

import java.io.File;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

public class SpringMailTest1 {

	public static void main(String[] args) throws MessagingException {
		ApplicationContext ac = new ClassPathXmlApplicationContext("applicationContext-mail.xml");
		JavaMailSender sender = (JavaMailSender) ac.getBean("mailSender");
	
		MimeMessage message = sender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message,true); //true带图片带附件必须设置
		helper.setFrom("18865506187@163.com");
		helper.setTo("18865506187@163.com");
		helper.setSubject("200张图片的附件");
		
		//true代表前面的文本可以当成html标签进行解析
		helper.setText("<html><head></head><body><h1>hello!!spring image html mail</h1><a href=http://www.baidu.com>baidu</a><img src='cid:image' /></body></html>", true);
				
		//添加了一个图片的附件
		helper.addInline("image", new FileSystemResource(new File("E:/test.jpg")));

		//添加附件
		helper.addAttachment("axis.log", new FileSystemResource(new File("E:/axis.log")));
	
		sender.send(message);
	}
}
