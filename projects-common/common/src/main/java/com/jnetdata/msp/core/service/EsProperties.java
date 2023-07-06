package com.jnetdata.msp.core.service;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "spring.elasticsearch")
public class EsProperties {

    private String hostname;
    private int port;
    private String scheme;
    private String userName;
    private String password;

}
