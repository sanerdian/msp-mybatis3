package com.jnetdata.msp.flowable.vo;

import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.thenicesys.fastjson.serializer.ToStringSerializer;

import java.util.Date;

/**
 * 流程步骤
 */
@Data
public class StepVo {

    @ApiModelProperty(value = "步骤名称")
    private String stepName;

    @ApiModelProperty(value = "流程实例id")
    private String procInstId;

    @ApiModelProperty(value = "步骤状态：已办结、办理中")
    private String stepStatus;

    @ApiModelProperty(value = "用时（单位：毫秒）")
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long duration;

    @ApiModelProperty(value = "操作人名称")
    private String userName;

    @ApiModelProperty(value = "接收时间")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date receiveTime;

    @ApiModelProperty(value = "完成时间")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;
}
