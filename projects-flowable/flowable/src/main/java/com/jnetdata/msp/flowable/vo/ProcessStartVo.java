package com.jnetdata.msp.flowable.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 启动流程参数
 */
@Data
public class ProcessStartVo {

    @ApiModelProperty(value = "元数据表名")
    private String metadataTable;

    @ApiModelProperty(value = "元数据id")
    private String metadataId;

    @ApiModelProperty(value = "流程定义id")
    private String processDefinitionId;

    @ApiModelProperty(value = "流程定义Key")
    private String processDefinitionKey;

    @ApiModelProperty(value = "元数据标题")
    private String metadataTitle;

    @ApiModelProperty(value = "元数据链接")
    private String metadataUrl;

    @ApiModelProperty(value = "表单id")
    private String formId;

    @ApiModelProperty(value = "审核用户id")
    private String auditUserId;

}
