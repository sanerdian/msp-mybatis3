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
@ApiModel(value = "站点路径缓存")
@Accessors(chain = true)
@Getter
@Setter
@ToString
@Component
public class SitePathVO {

    @ApiModelProperty(value = "站点的目录")
    private String dataPath;

    @ApiModelProperty(value = "站点的域名")
    private String weburl;

    @ApiModelProperty(value = "站点物理磁盘文件路径")
    private String filePath;

    @ApiModelProperty(value = "站点发布文件名")
    private String fileName;
}
