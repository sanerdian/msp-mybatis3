package com.jnetdata.msp.util;

import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;

import java.util.HashMap;

public class SendSms {
    public static void main(String[] args) {
        SendSms sendSms = new SendSms();
    }
    public void send(String phone){

        //连接阿里云
        DefaultProfile profile = DefaultProfile.getProfile("cn-hangzhou", "LTAI5t5kDmKRcREsKWTxExN8", "ClW9nlCVZYZPHBFFrmFUGELwD2hrE9");
        IAcsClient client = new DefaultAcsClient(profile);
        //构建请求
        CommonRequest request = new CommonRequest();
        request.setMethod(MethodType.POST);
        request.setDomain("dysmsapi.aliyuncs.com");
        request.setVersion("2017-05-25");
        request.setAction("SendSms");
        //自定义的参数（手机号，验证码，签名，模板!）
        request.putQueryParameter("PhoneNumbers", phone);
        request.putQueryParameter("SignName", "小明的博客");
        request.putQueryParameter("TemplateCode", "SMS_262445245");
        //构建一个短信验证码
        HashMap<String, Object> map = new HashMap<>();
        map.put("code",1314);
        request.putQueryParameter("TemplateParam", JSONObject.toJSONString(map));
        try {
            CommonResponse response = client.getCommonResponse(request);
            System.out.println(response.getData());
        } catch (ServerException e) {
            e.printStackTrace();
        } catch (ClientException e) {
            e.printStackTrace();
        }
    }
}