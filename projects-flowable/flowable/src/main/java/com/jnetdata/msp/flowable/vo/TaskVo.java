package com.jnetdata.msp.flowable.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 任务
 */
@Data
public class TaskVo {

    @ApiModelProperty(value = "任务id列表")
    private List<String> taskIds;

    @ApiModelProperty(value = "审核意见")
    private String auditOpinion;

    @ApiModelProperty(value = "审核参数，0-不同意，1-同意")
    private String auditParam;

    @ApiModelProperty(value = "用户id")
    private String auditUserId;
}
