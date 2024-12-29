package org.example.service;


import org.example.entity.SendEmail;
import org.springframework.stereotype.Service;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Random;

@Service
public class SendEmailService {

    private static String generateVerificationCode() {
        // 生成随机的5位数验证码
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 5; i++) {
            sb.append(random.nextInt(10));
        }
        return sb.toString();
    }

    public static String sendVerificationEmail(String userEmail) {
        try {
            // 创建Session会话
            Session session = SendEmail.createSession();
            // 创建邮件对象
            MimeMessage message = new MimeMessage(session);
            message.setSubject("微信系统 注册验证码");// 设置邮件标题
            String verificationCode = generateVerificationCode();// 生成验证码
            // 设置邮件内容
            message.setText("您的验证码是：" + verificationCode, "utf-8");
            message.setFrom(new InternetAddress("lzm_0203@163.com"));// 设置发件人
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(userEmail));
            Transport.send(message);
            return verificationCode;
        } catch (MessagingException e) {
            e.printStackTrace();
            return "发送失败";
        }
    }


//    public static void main(String[] args) {
//        System.out.println(sendVerificationEmail("1638102720@qq.com"));
//    }
}