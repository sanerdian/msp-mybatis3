package com.jnetdata.msp.config.systemmsg.model;

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

/**
 * Created by Administrator on 2019/9/12.
 */
@Data
@TableName("sysAppInfo")
@ApiModel(value = "系统应用信息实体类",description = "系统应用信息")
public class AppInfoMsg extends BaseEntity implements EntityId<Long> {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键ID", hidden = true)
    @TableId(value = "ID", type = IdType.ID_WORKER)
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long id;

    @TableField(value = "APPNAME")
    @NotEmpty(message = "应用名")
    @ApiModelProperty(value = "应用名")
    private String name;

    @TableField(value = "APPVERSION")
    @NotEmpty(message = "应用版本")
    @ApiModelProperty(value = "应用版本")
    private String  version;

    @TableField(value = "APPUSERS")
    @NotEmpty(message = "使用对象")
    @ApiModelProperty(value = "使用对象")
    private String  appUsers;

    @TableField(value = "APPFRAME")
    @NotEmpty(message = "使用对象")
    @ApiModelProperty(value = "使用对象")
    private String  appFrame;

    @TableField(value = "MANUAL")
    @NotEmpty(message = "手册")
    @ApiModelProperty(value = "手册")
    private String  manual;

    @TableField(value = "DESCRIPTION")
    @NotEmpty(message = "描述")
    @ApiModelProperty(value = "描述")
    private String  description;

    @TableField(value = "COPYRIGHT")
    @NotEmpty(message = "版本")
    @ApiModelProperty(value = "版本")
    private String  copyRight;

    @TableField(value = "FRONTURL")
    @ApiModelProperty(value = "前台地址")
    private String frontUrl;

}
