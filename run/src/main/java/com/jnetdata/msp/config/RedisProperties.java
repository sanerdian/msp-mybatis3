package com.jnetdata.msp.config;

import com.baomidou.mybatisplus.toolkit.StringUtils;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Data
@ConfigurationProperties(prefix = "spring.redis")
public class RedisProperties {

    private int isCluster;

    private String host;

    private int port;

    private int timeout;

    private String password;

    public String getAllHost(){
        return host + ":" + port;
    }

    public Boolean isUsed(){
        if(StringUtils.isNotEmpty(host)) return true;
        return false;
    }
}