package com.jnetdata.msp.message.phonemessage.controller;


import com.baidubce.auth.DefaultBceCredentials;
import com.baidubce.services.sms.SmsClient;
import com.baidubce.services.sms.SmsClientConfiguration;
import com.baidubce.services.sms.model.SendMessageV3Request;
import com.baidubce.services.sms.model.SendMessageV3Response;
import com.jnetdata.msp.constants.VerifyCodeKey;
import com.jnetdata.msp.limit.LimitRequest;
import com.jnetdata.msp.message.phonemessage.service.PhoneMessageService;
import com.jnetdata.msp.message.phonemessage.vo.MobileVo;
import com.jnetdata.msp.message.phonemessage.vo.PhoneMessageProperties;
import com.jnetdata.msp.message.phonemessage.vo.SendMessageVo;
import com.jnetdata.msp.message.phonemessage.vo.ValidateVerifyCodeVo;
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
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.ResourceBundle;

/**
 *
 * @author Admin
 * @date 2018/12/3
 */
@Api(value = "PhoneMessageController", description = "手机短信")
@Controller
@RequestMapping("/message/phoneMessage")
@Slf4j
public class PhoneMessageController extends BaseController {

    @Autowired
    private PhoneMessageProperties phoneMessageProperties;
    @Autowired
    private PhoneMessageService phoneMessageService;
    @Autowired
    private VerifyCodeSaverService verifyCodeSaverService;

    /**
     * 获取短信验证码
     * kindFlg参数（注册：REGISTER，收货地址：ADDRESS，找回密码：FINDBACK，预览：PREVIEW）
     * @return
     */
    @PostMapping("/sendMessage")
    @ResponseBody
    @ApiOperation("发送手机短信")
    public JsonResult<Boolean> sendMessage(@RequestBody SendMessageVo vo) {
        log.debug("sendMessage({})， msg={}, kindFlg={}", vo.getMobile(), vo.getMessage(), vo.getKindFlg());
        Boolean result = phoneMessageService.sendMsg(vo.getMobile(), vo.getMessage(), vo.getKindFlg());
        return renderSuccess(result);

    }

    /**
     * 发送手机短信验证码
     * @param vo
     * @return
     */
    @PostMapping("/sendPhoneVerifyCode")
    @ResponseBody
    @ApiOperation("发送手机短信验证码")
    @LimitRequest(limitCounts = 10L)
    public JsonResult<Boolean> sendPhoneVerifyCode(@RequestBody MobileVo vo){
        String verifyCode = String.valueOf(new Random().nextInt(899999)+100000);
        Session session = SecurityUtils.getSubject().getSession();
        String result = "短信配置错误";
        if(phoneMessageProperties.getType() == 1){
            result = sendByZhuTong(verifyCode,vo.getMobile());
        }else if(phoneMessageProperties.getType() == 2){
            result = sendByBaidu(verifyCode,vo.getMobile());
        }

        String key = generateValidateSessionKey(vo.getMobile());
        verifyCodeSaverService.set(session, key, verifyCode, 5* TimeConstant.MINUTE, 5 );

        if(result == null) return renderSuccess();
        return renderError(result);
    }

    public String sendByZhuTong(String verifyCode,String mobile){
        String msg = MessageFormat.format(phoneMessageProperties.getModelMessage(),verifyCode,phoneMessageProperties.getUnitName());
        Boolean result = phoneMessageService.sendMsg(mobile, msg, KindFlag.REGISTER);
        if(result) return null;
        return "发送失败";
    }

    public String sendByBaidu(String verifyCode,String mobile){
        String ENDPOINT = phoneMessageProperties.getEndpoint();
        SmsClientConfiguration config = new SmsClientConfiguration();
        config.setCredentials(new DefaultBceCredentials(phoneMessageProperties.getAccessKeyId(), phoneMessageProperties.getSecretAccessKey()));
        config.setEndpoint(ENDPOINT);
        SmsClient smsClient = new SmsClient(config);

        SendMessageV3Request request = new SendMessageV3Request();
        request.setMobile(mobile);
        request.setSignatureId(phoneMessageProperties.getSignatureId());
        request.setTemplate(phoneMessageProperties.getTemplateId());
        Map<String, String> contentVar = new HashMap<>();
        contentVar.put(phoneMessageProperties.getCodeName(), verifyCode);
        request.setContentVar(contentVar);
        SendMessageV3Response response = smsClient.sendMessage(request);
        // 解析请求响应 response.isSuccess()为true 表示成功
        System.out.println(response.getMessage());
        if(response!=null && response.isSuccess()) return null;
        return response.getMessage();
    }

    /**
     * 获取手机验证码（改接口只用于测试）
     * @param vo
     * @return
     */
    //@PostMapping("/gendPhoneVerifyCode")
    @ResponseBody
    @ApiOperation("获取session中保存的手机验证码（改接口只用于测试），不要在代码中使用!")
    public JsonResult<String> getPhoneVerifyCode(@RequestBody MobileVo vo) {
        Session session = SecurityUtils.getSubject().getSession();
        String verifyCode = (String)verifyCodeSaverService.get(session, generateValidateSessionKey(vo.getMobile()));
        return renderSuccess(verifyCode);
    }

    /**
     * 验证码是否正确
     * @param vo
     * @return
     */
    @ApiOperation("查看验证码是否正确")
    //@PostMapping("/validateVerifyCode")
    @ResponseBody
    public JsonResult<Boolean> validateVerifyCode(@RequestBody ValidateVerifyCodeVo vo){
        Boolean result = doValidateVerifyCode(vo.getMobile(), vo.getVerifyCode());
        return renderSuccess(result);
    }
    /**
     * 验证短信验证码是否正确
     * @param cellphone
     * @param verifyCode
     * @return
     */
    private Boolean doValidateVerifyCode(String cellphone, String verifyCode) {
        Session session = SecurityUtils.getSubject().getSession();
        String verifyCode1 = (String)verifyCodeSaverService.get(session, generateValidateSessionKey(cellphone));
        return verifyCode.equals(verifyCode1);
    }

    private String generateValidateSessionKey(String phonenumber) {
        return VerifyCodeKey.generateValidateSessionKeyByCellPhone(phonenumber);
    }

}
