package com.jnetdata.msp.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotEmpty;

@Validated
@Data
@ConfigurationProperties(prefix = "app")
@Component
public class AppConfigProperties {
//    @NotEmpty(message = "配置文件配置必须要配置[app.id]属性")
    private String key;

    @Bean
    public static ConfigPropertiesValidator configurationPropertiesValidator() {
        return new ConfigPropertiesValidator();
    }
}