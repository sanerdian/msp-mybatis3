package com.jnetdata.msp.message.emailmessage.controller;

import com.jnetdata.msp.constants.VerifyCodeKey;
import com.jnetdata.msp.limit.LimitRequest;
import com.jnetdata.msp.message.emailmessage.service.SendEmailService;
import com.jnetdata.msp.message.emailmessage.vo.EmailVo;
import com.jnetdata.msp.message.emailmessage.vo.SendEmailVo;
import com.jnetdata.msp.message.phonemessage.vo.MobileVo;
import com.jnetdata.msp.verifycodesaver.model.TimeConstant;
import com.jnetdata.msp.verifycodesaver.service.VerifyCodeSaverService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.thenicesys.store.message.sms.model.KindFlag;
import org.thenicesys.web.BaseController;
import org.thenicesys.web.JsonResult;

import java.text.MessageFormat;
import java.util.Random;

/**
 * Created by Admin on 2018/12/7.
 */

@Controller
@RequestMapping("/message/email")
@Api(value = "SendEmailController",description = "邮件发送")
@Slf4j
public class SendEmailController extends BaseController {

    @Autowired
    SendEmailService sendEmailService;
    @Autowired
    private VerifyCodeSaverService verifyCodeSaverService;

    /**
     * @param email  收件邮箱
     * @param subject  邮件主题
     * @param content  邮件内容
     * @author hongshou
     * @date 2020/5/26
     * @return
     */
    @PostMapping("/sendEmail")
    @ResponseBody
    @ApiOperation("发送邮件")
    public JsonResult sendEmail(String email,String subject,String content){
/*        StringBuilder stringHtml = new StringBuilder();
        //追加输入HTML文件内容
        stringHtml.append("<p>程利芳(lfchenga) ，您好：</p>");
        stringHtml.append("<p>账户[具体供应商名称]需冻结，冻结原因:[显示具体冻结原因]</p>");
        stringHtml.append("<p>  账户基本信息：</p>");
        stringHtml.append("<p>[具体供应商名称]</p>");
        stringHtml.append("<p>类别：</p>");
        stringHtml.append("<p> 采购类型：项目</p>");
        email = "15560130692@163.com";
        subject="添加黑名单";
        content = stringHtml.toString();*/
        return  renderSuccess(sendEmailService.sendEmail(email,subject,content));
    }


    /**
     * 发送手机短信验证码
     * @param vo
     * @author hongshou
     * @date 2020/5/26
     * @return
     */
    @PostMapping("/sendEmailVerifyCode")
    @ResponseBody
    @ApiOperation("发送邮件验证码")
    @LimitRequest(limitCounts = 10L)
    public JsonResult<Boolean> sendEmailVerifyCode(@RequestBody EmailVo vo){
        String verifyCode = String.valueOf(new Random().nextInt(899999)+100000);
        Session session = SecurityUtils.getSubject().getSession();
        String msg = MessageFormat.format("您好，您的验证码是{0}, 5分钟内有效。【中科聚网】",verifyCode);
        String key = generateValidateSessionKey(vo.getEmail());
        verifyCodeSaverService.set(session, key, verifyCode, 5* TimeConstant.MINUTE, 5 );
        log.debug("sendEmailVerifyCode({})， verifyCode={}", vo.getEmail(), verifyCode);
        log.info("----------------------------------------------------------------------");
        log.info("cellphone="+vo.getEmail()+",verifyCode="+verifyCode);
        System.out.println("cellphone="+vo.getEmail()+",verifyCode="+verifyCode);
        boolean result = sendEmailService.sendEmail(vo.getEmail(), "验证码", msg);
        return renderSuccess(result);
    }

    private String generateValidateSessionKey(String phonenumber) {
        return VerifyCodeKey.generateValidateSessionKeyByCellPhone(phonenumber);
    }

}
