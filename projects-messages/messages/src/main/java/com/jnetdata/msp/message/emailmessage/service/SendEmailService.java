package com.jnetdata.msp.message.emailmessage.service;

/**
 * Created by Admin on 2018/12/7.
 */
public interface SendEmailService{

    /**
     * 发送邮件
     * @param email 邮箱地址
     * @param subject 主题
     * @param content 内容
     * @author hongshou
     * @date 2020/5/26
     * @return
     */
    boolean sendEmail(String email, String subject, String content);

    /**
     * 群发并抄送
     * @param toEmailArr 收件人邮箱地址数组
     * @param copyTo 抄送邮箱人地址数组
     * @param subject 邮件标题
     * @param content 邮件内容
     * @author hongshou
     * @date 2020/5/26
     */
    boolean sendGroupAndCopy(String[] toEmailArr, String[] copyTo, String subject, String content);

}
