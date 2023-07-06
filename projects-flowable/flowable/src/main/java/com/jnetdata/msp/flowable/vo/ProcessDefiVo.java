package com.jnetdata.msp.flowable.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 流程定义
 */
@Data
public class ProcessDefiVo {

    @ApiModelProperty(value = "流程定义id")
    private String id;

    @ApiModelProperty(value = "流程定义名称")
    private String name;

    @ApiModelProperty(value = "流程定义key")
    private String key;
}
