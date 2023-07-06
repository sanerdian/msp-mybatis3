package com.jnetdata.msp.message.emailmessage.service.impl;


import com.jnetdata.msp.message.emailmessage.service.SendEmailService;
import com.jnetdata.msp.util.TryDothingUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thenicesys.store.message.email.service.EmailService;

/**
 *
 * @author Admin
 * @date 2018/12/7
 */
@Service
@Slf4j
public class SendEmailServiceImpl implements SendEmailService {

    @Autowired
    private EmailService emailService;

    /**
     * 发送邮件
     * @param email 邮箱地址
     * @param subject 主题
     * @param content 内容
     * @author hongshou
     * @date 2020/5/26
     * @return
     */
    @Override
    public boolean sendEmail(String email, String subject, String content) {
        log.debug("sendEmail({},{},{})...", email, subject, content);
        String[] str = new String[1];
        str[0] = email;
        TryDothingUtils.tryDothing(3, () -> {
            emailService.send(str, subject, content);
            }, 1000);
        log.debug("sendEmail complete.");
        return true;
    }

    /**
     * 群发并抄送
     * @param toEmailArr 收件人邮箱地址数组
     * @param copyTo 抄送邮箱人地址数组
     * @param subject 邮件标题
     * @param content 邮件内容
     * @author hongshou
     * @date 2020/5/26
     */
    @Override
    public boolean sendGroupAndCopy(String[] toEmailArr, String[] copyTo, String subject, String content) {
        emailService.sendGroupAndCopy(toEmailArr,copyTo,subject,content);
        return true;
    }
}
