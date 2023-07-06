package com.jnetdata.msp.flowable.model;

import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.thenicesys.fastjson.serializer.ToStringSerializer;

import java.io.Serializable;
import java.util.Date;

/**
 * 元数据公共字段
 */
@Data
@ApiModel(value="元数据公共信息", description="元数据公共信息")
public class MetaPub implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "元数据id")
    private String metaId;

    @ApiModelProperty(value = "任务id")
    private String taskId;

    @ApiModelProperty(value = "审核状态：1-审核中，2-已终止，3-已撤回，4-已完成，5-已驳回")
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Integer auditStatus;

    @ApiModelProperty(value = "审核时间")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date auditTime;

    @ApiModelProperty(value = "当前处理人id")
    private String handlerId;

    @ApiModelProperty(value = "当前处理人名称")
    private String handlerName;

    @ApiModelProperty(value = "最终审核结果（1-同意、0-不同意、---）")
    private String autidResult;

    @ApiModelProperty(value = "流程实例ID")
    private String procInstId;

    //非数据库字段
    @ApiModelProperty(value = "流程发起人id")
    private String startUserId;

    @ApiModelProperty(value = "流程发起人名称")
    private String startUserName;

    @ApiModelProperty(value = "流程发起时间")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;

    @ApiModelProperty(value = "数据表名称")
    private String tableName;
}
