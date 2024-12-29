package org.example.entity;


import javax.mail.PasswordAuthentication;
import java.util.Properties;
import javax.mail.Authenticator;
import javax.mail.Session;

public class SendEmail {
    public static Session createSession() {
        String userName = "lzm_0203@163.com";//发送方邮箱账号
        String password = "CCDZACCMLJEIESWB";//账号授权密码
        // SMTP服务器连接信息
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.163.com"); // SMTP主机名
        props.put("mail.smtp.port", "465"); // 主机端口号
        props.put("mail.smtp.auth", "true"); // 是否需要用户认证
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(userName, password);
            }
        });
        return session;
    }
}