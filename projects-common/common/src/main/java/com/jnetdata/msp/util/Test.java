package com.jnetdata.msp.util;

import com.sun.mail.util.MailSSLSocketFactory;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.security.GeneralSecurityException;
import java.util.Date;
import java.util.Properties;

public class Test {


    public static void main(String[] args) throws Exception {
        System.out.println(System.getProperty("catalina.home"));
//        new Test().test("smtp.exmail.qq.com","li.zike@jnetdata.com","Lizike1234","li.zike@jnetdata.com","hello","hello");
    }
    public void test(String stmp,String username,String password,String recmail,String title,String concent, Integer ifSLL, Integer port){
        Properties prop = new Properties();
        //协议
        prop.setProperty("mail.transport.protocol", "smtp");
        //服务器
        prop.setProperty("mail.smtp.host", stmp);
        //端口
        prop.setProperty("mail.smtp.port", port.toString());
        //使用smtp身份验证
        prop.setProperty("mail.smtp.auth", "true");
        if(ifSLL==1){
            //使用SSL，企业邮箱必需！
            //开启安全协议
            MailSSLSocketFactory sf = null;
            try {
                sf = new MailSSLSocketFactory();
                sf.setTrustAllHosts(true);
            } catch (GeneralSecurityException e1) {
                e1.printStackTrace();
            }
            prop.put("mail.smtp.ssl.enable", "true");
            prop.put("mail.smtp.ssl.socketFactory", sf);
        }
        //获取Session对象
        Session s = Session.getInstance(prop,new Authenticator() {
            //此访求返回用户和密码的对象
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                PasswordAuthentication pa = new PasswordAuthentication(username, password);
                return pa;
            }
        });
        //设置session的调试模式，发布时取消
        s.setDebug(true);
        MimeMessage mimeMessage = new MimeMessage(s);
        try {
            mimeMessage.setFrom(new InternetAddress(username,username));
            mimeMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(recmail));
            //设置主题
            mimeMessage.setSubject(title);
            mimeMessage.setSentDate(new Date());
            //设置内容
            mimeMessage.setText(concent);
            mimeMessage.saveChanges();
            //发送
            Transport.send(mimeMessage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
