package com.jnetdata.msp.member.group.model;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.jnetdata.msp.core.model.BaseEntity;
import com.jnetdata.msp.member.user.model.User;
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
@TableName("GRP_USER")
@ApiModel(value = "组织用户关联实体类",description = "组织用户关联")
public class GrpUser extends BaseEntity implements EntityId<Long> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键ID", hidden = true)
    @TableId(value = "GRPUSERID", type = IdType.ID_WORKER)
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long id;

    @TableField(value = "GROUPID")
    @NotEmpty(message = "组织编号")
    @ApiModelProperty(value = "组织编号")
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long groupId;

    @TableField(value = "USERID")
    @NotEmpty(message = "用户id")
    @ApiModelProperty(value = "用户id")
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long userId;

    @TableField(value = "JOB")
    @ApiModelProperty(value = "职位")
    private String job;

    @TableField(value = "ISLEADER")
    @NotEmpty(message = "是否组长")
    @ApiModelProperty(value = "是否组长")
    private int isLeader;

    @TableField(value = "USERORDER")
    @NotEmpty(message = "排序字段")
    @ApiModelProperty(value = "排序字段")
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long userOrder;

    @TableField(exist = false)
    private User user;



}
