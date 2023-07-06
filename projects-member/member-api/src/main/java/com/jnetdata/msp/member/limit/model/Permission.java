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
 * Created by WFJ on 2019/4/2.
 */

@Data
@TableName("J_PERMISSION")
@ApiModel(value = "授权实体类",description = "授权表")
public class Permission extends BaseEntity implements EntityId<Long> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键ID", hidden = true)
    @TableId(value = "ID", type = IdType.ID_WORKER)
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long id;

    @TableField(value = "OWNER_TYPE")
    @ApiModelProperty(value = "对象类型(0: 系统用户,1角色, 2组织, 3：互联网用户)")
    private int ownerType;

    @TableField(value = "OWNER_ID")
    @ApiModelProperty(value = "所属对象编号")
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long ownerId;

    @TableField(value = "ISURL")
    @ApiModelProperty(value = "授权方式是否是url(1:是)")
    private int isurl;

    @TableField(value = "PERMISSION")
    @ApiModelProperty(value = "权限值")
    private String permission;

    @TableField(value = "DESCRIPTION")
    @ApiModelProperty(value = "描述")
    private String description;

}
