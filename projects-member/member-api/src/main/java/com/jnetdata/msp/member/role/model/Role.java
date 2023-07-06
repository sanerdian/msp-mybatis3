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
import java.util.List;

/**
 * Created by WFJ on 2019/4/2.
 */


@Data
@TableName("ROLEINFO")
@ApiModel(value = "角色基本信息实体类",description = "角色基本信息")
public class Role extends BaseEntity implements EntityId<Long> {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键ID", hidden = true)
    @TableId(value = "ROLEID", type = IdType.ID_WORKER)
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long id;

    @TableField(value = "ROLENAME")
    @NotEmpty(message = "角色名称")
    @ApiModelProperty(value = "角色名称")
    private String name;

    @TableField(value = "ROLEPATH")
    @NotEmpty(message = "角色结构层级")
    @ApiModelProperty(value = "角色结构层级")
    private String rilePath;

    @TableField(value = "ROLEDESC")
    @NotEmpty(message = "描述说明")
    @ApiModelProperty(value = "描述说明")
    private String roleDesc;

    @TableField(value = "ROLETYPE")
    @NotEmpty(message = "角色类型")
    @ApiModelProperty(value = "角色类型")
    private String roleType;

    @TableField(value = "PARENTID")
    @NotEmpty(message = "父角色编号")
    @ApiModelProperty(value = "父角色编号")
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long parentId;

    @TableField(value = "ROLEORDER")
    @NotEmpty(message = "显示顺序")
    @ApiModelProperty(value = "显示顺序")
    private Long roleOrder;

    @TableField(value = "STATUS")
    @NotEmpty(message = "状态")
    @ApiModelProperty(value = "状态")
    private int status;

    @TableField(value = "ADDRESS")
    @NotEmpty(message = "地址")
    @ApiModelProperty(value = "地址")
    private String address;

    @TableField(value = "MOBILE")
    @NotEmpty(message = "手机")
    @ApiModelProperty(value = "手机")
    private String mobile;

    @TableField(value = "EMAIL")
    @NotEmpty(message = "邮箱")
    @ApiModelProperty(value = "邮箱")
    private String email;

    @TableField(value = "QQ")
    @NotEmpty(message = "qq")
    @ApiModelProperty(value = "qq")
    private String qq;

    @TableField(value = "PHONE")
    @NotEmpty(message = "电话")
    @ApiModelProperty(value = "电话")
    private String phone;

    @TableField(value = "ROLECODE")
    @NotEmpty(message = "编码")
    @ApiModelProperty(value = "编码")
    private String code;

    @TableField(exist = false)
    @ApiModelProperty(value = "子节点集合")
    private List<Role> children;

    @TableField(value = "COMPANYID")
    @NotEmpty(message = "所属机构id")
    @ApiModelProperty(value = "所属机构id")
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long companyId;

    @TableField(exist = false)
    @ApiModelProperty("所属机构名")
    private String companyName;

    @TableField(exist = false)
    @ApiModelProperty(value = "接收者标识")
    private int addressTpye;
}
