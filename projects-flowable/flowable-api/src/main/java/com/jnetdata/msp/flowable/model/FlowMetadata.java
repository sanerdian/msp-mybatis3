package com.jnetdata.msp.flowable.model;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.thenicesys.data.api.EntityId;
import org.thenicesys.fastjson.serializer.ToStringSerializer;

import java.util.Date;

@Data
@TableName("j_flow_metadata")
@ApiModel(value="流程实例与元数据的关联信息")
public class FlowMetadata implements EntityId<String> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "流程实例id，主键")
    @TableId(value = "proc_inst_id", type = IdType.INPUT)
    private String id;

    @ApiModelProperty(value = "元数据表名")
    @TableField("metadata_table")
    private String metadataTable;

    @ApiModelProperty(value = "元数据id")
    @TableField("metadata_id")
    private String metadataId;

    @ApiModelProperty(value = "元数据标题（工作名称）")
    @TableField("metadata_title")
    private String metadataTitle;

    @ApiModelProperty(value = "元数据url")
    @TableField("metadata_url")
    private String metadataUrl;

    @ApiModelProperty(value = "流程当前处理人")
    @TableField("task_handler")
    private String taskTandler;

    @ApiModelProperty(value = "创建时间")
    @TableField("create_time")
    @JSONField(format = "yyyy-MM-dd hh:mm:ss")
    private Date createTime;

    @ApiModelProperty(value = "创建人id")
    @TableField("creator_id")
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long creatorId;

    @ApiModelProperty(value = "创建人名称")
    @TableField("creator_name")
    private String creatorName;

    @ApiModelProperty(value = "审核状态：1-审核中，2-已终止，3-已撤回，4-已完成，5-已驳回")
    @TableField("audit_status")
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Integer auditStatus;

    @ApiModelProperty(value = "流程定义id")
    @TableField("proc_defi_id")
    private String procDefiId;

    @ApiModelProperty(value = "流程定义key")
    @TableField("proc_defi_key")
    private String procDefiKey;

    @ApiModelProperty(value = "流水号")
    @TableField("serial_number")
    private Integer serialNumber;

}
