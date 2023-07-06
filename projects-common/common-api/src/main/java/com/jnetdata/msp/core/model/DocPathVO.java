package com.jnetdata.msp.core.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.springframework.stereotype.Component;

/**
 * @author penghe.li
 * @version 1.0
 * @description: TODO
 * @date 2022/9/2 1:11
 */
@ApiModel(value = "文档路径缓存")
@Accessors(chain = true)
@Getter
@Setter
@ToString
@Component
public class DocPathVO {
    private String docPubUrl;

    private String docWebDir;

    private String docChannelUrl;

    @ApiModelProperty(value = "文档物理磁盘文件路径")
    private String filePath;

    private String ext;

    private String docFileName;
}
