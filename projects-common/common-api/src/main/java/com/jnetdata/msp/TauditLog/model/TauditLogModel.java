package com.jnetdata.msp.TauditLog.model;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 功能描述：
 */
@Data
@TableName("t_audit_log")
public class TauditLogModel {

    //主键id
    @ApiModelProperty(value = "主键id")
    @TableId(value = "id",type = IdType.ASSIGN_ID)
    @JSONField(serializeUsing = org.thenicesys.fastjson.serializer.ToStringSerializer.class)
    private Long id;

    @TableField("crtime")
    @ApiModelProperty(value = "创建时间搜索" , example = "2019-01-22 17:32:24")
    @JSONField(format = "yyyy-MM-dd hh:mm:ss")
    private Date crtime;

    @ApiModelProperty(value = "审核状态（1:撰稿状态，2：审稿状态）")
    private Long status;

    @ApiModelProperty(value = "创建人")
    private String cruser;
    @ApiModelProperty(value = "创建人id")
    private Long cruserid;

    @ApiModelProperty(value = "创建时间搜索" , example = "2019-01-22 17:32:24")
    @JSONField(format = "yyyy-MM-dd hh:mm:ss")
    private Date uptime;

    @ApiModelProperty(value = "更新人")
    private String upuser;

    @ApiModelProperty(value = "更新人id")
    private Long upuserid;

    @ApiModelProperty(value = "栏目名称")
    @TableField("column_Name")
    private String columnName;

    @ApiModelProperty(value = "栏目id")
    @TableField("column_id")
    private Long columnId;

    @ApiModelProperty(value = "操作状态")
    @TableField("operation_Status")
    private String operationStatus;
}
