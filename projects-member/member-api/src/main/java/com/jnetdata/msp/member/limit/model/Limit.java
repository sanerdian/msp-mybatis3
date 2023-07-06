package com.jnetdata.msp.member.limit.model;

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
 * Created by WFJ on 2019/4/1.
 */


@Data
@TableName("limit_info")
@ApiModel(value = "权限信息实体类",description = "权限信息")
public class Limit extends BaseEntity implements EntityId<Long> {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键ID", hidden = true)
    @TableId(value = "ID", type = IdType.ID_WORKER)
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long id;

    @NotEmpty(message = "权限名称")
    @ApiModelProperty(value = "权限名称")
    private String name;

    @NotEmpty(message = "权限类型")
    @ApiModelProperty(value = "权限类型")
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long type;

    @NotEmpty(message = "权限类型")
    @ApiModelProperty(value = "权限类型")
    private String ldesc;

    @NotEmpty(message = "权限值")
    @ApiModelProperty(value = "权限值")
    private String value;

    @NotEmpty(message = "是否系统预定义权限")
    @ApiModelProperty(value = "是否系统预定义权限")
    private int system;

    @TableField(exist = false)
    @NotEmpty(message = "权限分类")
    @ApiModelProperty(value = "权限分类")
    private String typename;

    @TableField( value= "STATUS")
    @NotEmpty(message = "转换修改状态")
    @ApiModelProperty(value = "转换修改状态")
    private int status;

}
