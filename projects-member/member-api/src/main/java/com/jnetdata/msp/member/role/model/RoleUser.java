package com.jnetdata.msp.member.role.model;

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
 * Created by WFJ on 2019/4/2.
 */

@Data
@TableName("ROLE_USER")
@ApiModel(value = "用户角色关联实体类",description = "用户角色关联表")
public class RoleUser extends BaseEntity implements EntityId<Long> {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键ID", hidden = true)
    @TableId(value = "ROLEUSERID", type = IdType.ID_WORKER)
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long id;

    @TableField(value = "ROLEID")
    @NotEmpty(message = "角色编号")
    @ApiModelProperty(value = "角色编号")
    private Long roleId;

    @TableField(value = "USERID")
    @NotEmpty(message = "用户编号")
    @ApiModelProperty(value = "用户编号")
    private Long userId;

    @TableField(value = "ISLEADER")
    @NotEmpty(message = "是否为组长")
    @ApiModelProperty(value = "是否为组长")
    private int isLeader;

    @TableField(value = "USERORDER")
    @NotEmpty(message = "用户排序")
    @ApiModelProperty(value = "用户排序")
    private Long userOrder;

    @TableField(value = "JOINTIME")
    @NotEmpty(message = "创建时间")
    @ApiModelProperty(value = "创建时间")
    private Date joinTime;


}
