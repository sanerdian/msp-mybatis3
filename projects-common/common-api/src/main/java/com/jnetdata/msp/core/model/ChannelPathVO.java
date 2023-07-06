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
 * @date 2022/8/30 23:50
 */
@ApiModel(value = "栏目路径缓存")
@Accessors(chain = true)
@Getter
@Setter
@ToString
@Component
public class ChannelPathVO {

    @ApiModelProperty(value = "栏目自身的 chnlDataPath 属性")
    private String chnlDataPath;

    @ApiModelProperty(value = "全栏目路径,当前与chnlDataPath一致")
    private String chnlPath;

    @ApiModelProperty(value = "栏目物理磁盘文件路径")
    private String filePath;

    @ApiModelProperty(value = "栏目web访问路径,包括站点前缀")
    private String webPath;

    @ApiModelProperty(value = "栏目web访问路径,包括站点域名")
    private String fullWebPath;

    @ApiModelProperty(value = "栏目发布文件名")
    private String fileName;
}
