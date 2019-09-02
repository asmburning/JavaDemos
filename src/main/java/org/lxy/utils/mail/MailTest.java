package org.lxy.utils.mail;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.mail.internet.MimeMessage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

@Slf4j
public class MailTest {

    @Test
    public void test1() {
        String host = "smtp.163.com";
        String userName = "xxx@163.com";
        String password = "xxx";
        doSendEmail(host, userName, password);
    }

    @Test
    public void test2() {
        String host = "xx";
        String userName = "x@x.com.cn";
        String password = "xx";
        doSendEmail(host, userName, password);
    }

    public void doSendEmail(String host, String userName, String password) {
        try {

            JavaMailSenderImpl senderImpl = new JavaMailSenderImpl();

            // 设定mail server
            senderImpl.setHost(host);
            senderImpl.setUsername(userName); // 根据自己的情况,设置username
            senderImpl.setPassword(password); // 根据自己的情况, 设置password

            Properties prop = new Properties();
            prop.put("mail.smtp.auth", "true"); // 将这个参数设为true，让服务器进行认证,认证用户名和密码是否正确
            prop.put("mail.smtp.timeout", "25000");
            senderImpl.setJavaMailProperties(prop);

            // 建立邮件消息,发送简单邮件和html邮件的区别
            MimeMessage mailMessage = senderImpl.createMimeMessage();
            MimeMessageHelper messageHelper = new MimeMessageHelper(mailMessage,
                    true, "utf-8");

            FileSystemResource file = new FileSystemResource(
                    new File("D:/sql/run.sql"));
            // 这里的方法调用和插入图片是不同的。
            messageHelper.addAttachment("run.sql", file);


            List<String> toList = new ArrayList<>();
            toList.add("x@qq.com");
            toList.add("x@qq.com");
            // 设置收件人，寄件人
            String[] toArray = new String[toList.size()];
            toList.toArray(toArray);
            messageHelper.setTo(toArray);
            messageHelper.setFrom(userName);
            messageHelper.setSubject("测试带附件HTML邮件！");
            // true 表示启动HTML格式的邮件
            messageHelper.setText("<h1>hello!!<br>spring html Mail</h1>",
                    true);

            // 发送邮件
            senderImpl.send(mailMessage);

            log.info("邮件发送成功..");
        } catch (Exception e) {
            log.error("e", e);
        }
    }
}
