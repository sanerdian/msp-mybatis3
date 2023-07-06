package com.jnetdata.msp.visual.log.model;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 功能描述：
 */
@Data
@TableName("t_element_log")
@ApiModel(value="组件操作日志" , description="")
public class ElementModel {
    @ApiModelProperty(value = "主键id")
    @JSONField(serializeUsing = org.thenicesys.fastjson.serializer.ToStringSerializer.class)
    @TableId(value = "ID", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("创建人")
    private String cruser;


    @ApiModelProperty("创建人id")
    private Long cruserid;

    @ApiModelProperty("模板类型")
    private String templateType;

    @ApiModelProperty("模板名称")
    private String templateName;

    @ApiModelProperty("操作")
    private String operation;


    @TableField("crtime")
    @ApiModelProperty(value = "创建时间搜索" , example = "2019-01-22 17:32:24")
    @JSONField(format = "yyyy-MM-dd hh:mm:ss")
    private Date crtime;

    private String ipadress;

    @TableField(exist = false)
    private Date endTime;
}
