package com.jnetdata.msp.docs.document.model;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author Administrator
 */
@Data
@ConfigurationProperties(prefix = "jnetdata.upload")
public class DocumentUploadProperties {

    /**
     * 文件上传路径
     */
    private String uploadRoot = "/data/JMETADATA/";

    /**
     * 图片访问路径映射
     */
    private String rootMappingUrl = "/JMETADATA/";

}
