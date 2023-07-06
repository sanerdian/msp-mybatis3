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

@Data
@TableName("limit_type")
@ApiModel(value = "权限类型实体类",description = "权限类型")
public class LimitType extends BaseEntity implements EntityId<Long> {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键ID", hidden = true)
    @TableId(value = "ID", type = IdType.ID_WORKER)
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long id;

    @NotEmpty(message = "权限类型名称")
    @ApiModelProperty("权限名称")
    private String name;

    @NotEmpty(message = "权限类型描述")
    @ApiModelProperty("权限类型描述")
    private String tdesc;

    @TableField( value= "SYSTEM")
    @NotEmpty(message = "是否系统定义")
    @ApiModelProperty("是否系统定义")
    private int system;

    @TableField( value= "STATUS")
    @NotEmpty(message = "转换修改状态")
    @ApiModelProperty("转换修改状态")
    private int status;

    @NotEmpty(message = "权限类型分类")
    @ApiModelProperty("权限类型分类")
    private String type;

    @NotEmpty(message = "元数据表id")
    @ApiModelProperty("元数据表id")
    @TableField("TABLE_ID")
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long tableId;

    @NotEmpty(message = "元数据表名")
    @ApiModelProperty("元数据表名")
    @TableField("TABLE_NAME")
    private Long tableName;
}
