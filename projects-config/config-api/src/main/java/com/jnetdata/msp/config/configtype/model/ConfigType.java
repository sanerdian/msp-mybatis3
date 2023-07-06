package com.jnetdata.msp.config.configtype.model;

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
 * Created by TF on 2019/3/26.
 */


@Data
@TableName("SYSCONFIGTYPE")
@ApiModel(value = "系统配置类型实体类",description = "系统配置类型")
public class ConfigType extends BaseEntity implements EntityId<Long> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键id", hidden = true)
    @TableId(value = "CONFIGID", type = IdType.ID_WORKER)
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long id;

    @TableField(value = "CTYPE")
    @NotEmpty(message = "模块标识")
    @ApiModelProperty(value = "模块标识")
    private int mark;

    @TableField(value = "CKEY")
    @NotEmpty(message = "属性名")
    @ApiModelProperty(value = "属性名")
    private String name;

    @TableField(value = "CVALUE")
    @NotEmpty(message = "属性值")
    @ApiModelProperty(value = "属性值")
    private String value;

    @TableField(value = "CDESC")
    @NotEmpty(message = "描述")
    @ApiModelProperty(value = "描述")
    private String describe;

    @TableField(value = "SITEID")
    @NotEmpty(message = "站点信息主键")
    @ApiModelProperty(value = "站点信息主键")
    private Long siteId;

}
