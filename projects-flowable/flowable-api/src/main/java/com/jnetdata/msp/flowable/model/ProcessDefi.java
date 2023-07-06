package com.jnetdata.msp.flowable.model;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.thenicesys.data.api.EntityId;

import java.util.Date;

@Data
@TableName("j_flow_process_defi")
@ApiModel(value="流程定义表")
public class ProcessDefi implements EntityId<String> {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "流程id，主键")
    @TableId(value = "process_id", type = IdType.INPUT)
    private String id;

    @ApiModelProperty(value = "流程名称")
    @TableField(value = "process_name")
    private String processName;

    @ApiModelProperty(value = "流程key")
    @TableField(value = "process_key")
    private String processKey;

    @ApiModelProperty(value = "流程描述")
    @TableField(value = "process_desc")
    private String processDesc;

    @ApiModelProperty(value = "序号")
    @TableField("order_number")
    private Integer orderNumber;

    @ApiModelProperty(value = "图标路径")
    @TableField("icon_path")
    private String iconPath;

    @ApiModelProperty(value = "图标颜色")
    @TableField("icon_color")
    private String iconColor;

    @ApiModelProperty(value = "表单分类id")
    @TableField(value = "form_class_id")
    private String formClassId;

    @ApiModelProperty(value = "表单id")
    @TableField(value = "form_id")
    private String formId;

    @ApiModelProperty(value = "流程分类id")
    @TableField(value = "process_class_id")
    private String processClassId;

    @ApiModelProperty(value = "委托设置：0-不允许委托，1-允许委托")
    @TableField(value = "delegate_flag")
    private String delegateFlag;

    @ApiModelProperty(value = "流程附件：0-关闭，1-开启")
    @TableField(value = "attachment_flag")
    private String attachmentFlag;

    @ApiModelProperty(value = "创建时间")
    @TableField("create_time")
    @JSONField(format = "yyyy-MM-dd hh:mm:ss")
    private Date createTime;

    @ApiModelProperty(value = "修改时间")
    @TableField("update_time")
    @JSONField(format = "yyyy-MM-dd hh:mm:ss")
    private Date updateTime;

    @ApiModelProperty(value = "创建用户id")
    @TableField(value = "create_user_id")
    private String createUserId;

    @ApiModelProperty(value = "修改用户id")
    @TableField(value = "update_user_id")
    private String updateUserId;



}
