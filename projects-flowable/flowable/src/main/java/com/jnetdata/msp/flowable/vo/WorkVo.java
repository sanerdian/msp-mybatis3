package com.jnetdata.msp.flowable.vo;

import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.thenicesys.fastjson.serializer.ToStringSerializer;

import java.util.Date;

@Data
@ApiModel(value="我的工作")
public class WorkVo {

    @ApiModelProperty(value = "工作名称")
    private String workName;

    @ApiModelProperty(value = "流程实例id")
    private String procInstId;

    @ApiModelProperty(value = "流程定义id")
    private String procDefiId;

    @ApiModelProperty(value = "流程定义key")
    private String procDefiKey;

    @ApiModelProperty(value = "流程定义名称")
    private String procDefiName;

    @ApiModelProperty(value = "流程开始时间")
    @JSONField(format = "yyyy-MM-dd hh:mm:ss")
    private Date startTime;

    @ApiModelProperty(value = "发起人id")
    private String startUserId;

    @ApiModelProperty(value = "发起人名称")
    private String startUserName;

    @ApiModelProperty(value = "审核状态：1-审核中，2-已终止，3-已撤回，4-已完成，5-已驳回")
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Integer auditStatus;

    @ApiModelProperty(value = "审核状态名称")
    private String auditStatusStr;

    @ApiModelProperty(value = "流水号")
    private Integer serialNumber;

    @ApiModelProperty(value = "步骤名称")
    private String stepName;

    @ApiModelProperty(value = "当前步骤名称")
    private String currentStepName;

    @ApiModelProperty(value = "接收时间")
    @JSONField(format = "yyyy-MM-dd hh:mm:ss")
    private Date receiveTime;

    @ApiModelProperty(value = "办结时间")
    @JSONField(format = "yyyy-MM-dd hh:mm:ss")
    private Date doneTime;

    @ApiModelProperty(value = "元数据表名")
    private String metadataTable;

    //参数
    @ApiModelProperty(value = "当前登录用户id")
    private String loginUserId;



}
