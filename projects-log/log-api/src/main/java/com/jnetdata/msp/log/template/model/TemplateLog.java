package com.jnetdata.msp.log.template.model;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.jnetdata.msp.core.model.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.thenicesys.data.api.EntityId;
import org.thenicesys.fastjson.serializer.ToStringSerializer;

import javax.validation.constraints.NotEmpty;
import java.util.Date;

/**
 * Created by TF on 2019/3/13.
 */
@Data
@TableName("t_template_log")
@ApiModel(value = "模板日志实体类",description = "模板管理日志")
public class TemplateLog extends BaseEntity implements EntityId<Long> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键id", hidden = true)
    @TableId(value = "id", type = IdType.ID_WORKER)
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long id;

    @TableField(value = "column_name")
    @NotEmpty(message = "栏目名称")
    @ApiModelProperty(value = "栏目名称")
    private String columnName;

    @TableField(value = "template_type")
    @NotEmpty(message = "模板类型")
    @ApiModelProperty(value = "模板类型")
    private String templateType;

    @TableField(value = "template_name")
    @NotEmpty(message = "模板名称")
    @ApiModelProperty(value = "模板名称")
    private String templateName;

    @TableField(value = "handle_type")
    @NotEmpty(message = "操作类型")
    @ApiModelProperty(value = "操作类型")
    private String handleType;

    @TableField(value = "handle_result")
    @NotEmpty(message = "操作结果")
    @ApiModelProperty(value = "操作结果")
    private Long handleResult;

    @TableField(value = "detail")
    @NotEmpty(message = "操作明细")
    @ApiModelProperty(value = "操作明细")
    private String detail;

    @TableField(value = "CRUSER")
    @NotEmpty(message = "创建人")
    @ApiModelProperty(value = "创建人")
    private String crUser;

    @TableField(value = "ADDRESS")
    @NotEmpty(message = "IP地址")
    @ApiModelProperty(value = "IP地址")
    private String address;

    @TableField(value = "CRTIME")
    @NotEmpty(message = "创建时间")
    @ApiModelProperty(value = "创建时间")
    @JSONField(format = "yyyy-MM-dd  HH:mm:ss")
    private Date createTime;

    @TableField(exist = false)
    private Date endTime;
}
