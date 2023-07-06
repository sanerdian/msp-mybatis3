package com.jnetdata.msp.member.resource.model;

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

/**
 * 可以被授权的资源
 * 比如: 用户模块(naturalId=user,name='用户模块') ,
 *       角色模块(naturalId=role, name='角色模块')
 * 权限： user:get 表示可以对用户模块做查询操作,
 *        user:update 可以对用户模块做修改操作,
 *        user:get:1 表示可以查看id=1的用户信息,
 *        user:get=user:get:*, user=user:*:*
 */
@Data
@TableName("t_resource")
@ApiModel(description = "授权资源表")
public class Resource extends BaseEntity implements EntityId<Long> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "id", hidden = true)
    @TableId(value = "id", type = IdType.ID_WORKER)
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long id;

    /**
     * 资源编码
     */
    private String naturalId;

    /**
     * 资源名称
     */
    private String name;

    /**
     * 资源描述
     */
    private String description;

    /**
     * 是否禁用
     */
    private Boolean active;

}
