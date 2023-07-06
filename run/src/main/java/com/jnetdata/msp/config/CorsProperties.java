package com.jnetdata.msp.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Arrays;
import java.util.List;

@ConfigurationProperties(prefix = "jnetdata.cros")
@Data
public class CorsProperties {

    private List<String> allowedOrigins;

    private List<String> allowedMethods = Arrays.asList("*");

    private List<String> allowedHeaders = Arrays.asList("*");

    private Boolean allowCredentials = true;

    private Long maxAge = 3600L;

}
