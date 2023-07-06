package com.jnetdata.msp.docs.upload.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

//TODO xuning 待修复,现在的仍不对,无法正确注入配置
@Configuration
@Data
public class UploadConfig {

    private String basePath;

}
