package com.jnetdata.msp.message.phonemessage.vo;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "thenicesys.message.sms")
@Data
public class PhoneMessageProperties {
    private Integer type;

    //助通
    private String loginName;
    private String passWord;
    private String corpId = "0";
    private String modelMessage = "%s";
    private String mtUrl;
    private String md5Url;
    private String msgFormat = "2";
    private String mtLevel = "1";
    private String unitName = "phone_unitName";

    //百度
    private String accessKeyId;
    private String secretAccessKey;
    private String endpoint;
    private String signatureId;
    private String templateId;
    private String codeName;
}
