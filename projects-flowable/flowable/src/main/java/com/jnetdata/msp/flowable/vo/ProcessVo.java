package com.jnetdata.msp.flowable.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 流程
 */
@Data
public class ProcessVo {

    @ApiModelProperty(value = "模型id")
    private String modelId;

    @ApiModelProperty(value = "模型名称")
    private String modelName;

    @ApiModelProperty(value = "流程定义id")
    private String procDefiId;

    @ApiModelProperty(value = "流程定义名称")
    private String procDefiName;

    @ApiModelProperty(value = "流程key")
    private String procKey;

    @ApiModelProperty(value = "流程实例id")
    private String procInstId;

    @ApiModelProperty(value = "删除原因")
    private String deleteReason;
}
