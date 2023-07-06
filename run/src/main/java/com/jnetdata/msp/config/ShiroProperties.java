package com.jnetdata.msp.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

@Data
@ConfigurationProperties(prefix = "jnetdata.shiro")
public class ShiroProperties {

    //登录
    private String loginUrl = "/simple/member/user/login.html";

    private String successUrl = "/index.html";

    private String anauthorizedUrl = "/simple/member/user/login.html";

    private List<String> filterChainDefinitions = new ArrayList<>();

}
