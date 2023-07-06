package com.jnetdata.msp.manage.site.vo;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.jnetdata.msp.core.model.BaseEntity;
import com.jnetdata.msp.manage.site.model.Site;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.thenicesys.data.api.EntityId;
import org.thenicesys.fastjson.serializer.ToStringSerializer;

import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * Created by WFJ on 2019/4/28.
 */
@Data
@TableName("GROUPINFO")
@ApiModel(description = "组织信息表")
public class TempOrgan extends BaseEntity implements EntityId<Long> {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "GROUPID", hidden = true)
    @TableId(value = "GROUPID", type = IdType.ID_WORKER)
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long id;

    @TableField(value = "GROUPNAME")
    @NotEmpty(message = "组织名称")
    @ApiModelProperty(value = "组织名称")
    private String name;

    @TableField(value = "GROUPPATH")
    @NotEmpty(message = "组织结构层级")
    @ApiModelProperty(value = "组织结构层级")
    private String groupPath;

    @TableField(value = "GDESC")
    @NotEmpty(message = "组织描述")
    @ApiModelProperty(value = "组织描述")
    private String desc;

    @TableField(value = "PARENTID")
    @NotEmpty(message = "父组织编号")
    @ApiModelProperty(value = "父组织编号")
    private Long parentId;

    @TableField(value = "GROUPORDER")
    @NotEmpty(message = "组织展示顺序")
    @ApiModelProperty(value = "组织展示顺序")
    private Long order;

    @TableField(value = "STATUS")
    @NotEmpty(message = "组织状态")
    @ApiModelProperty(value = "组织状态")
    private Integer status;

    @TableField(value = "ADDRESS")
    @NotEmpty(message = "组织地址")
    @ApiModelProperty(value = "组织地址")
    private String address;

    @TableField(value = "MOBILE")
    @NotEmpty(message = "手机号码")
    @ApiModelProperty(value = "手机号码")
    private String mobile;

    @TableField(value = "phone")
    @NotEmpty(message = "固定电话")
    @ApiModelProperty(value = "固定电话")
    private String phone;

    @TableField(value = "QQ")
    @NotEmpty(message = "qq好")
    @ApiModelProperty(value = "qq号")
    private String qq;

    @TableField(value = "EMAIL")
    @NotEmpty(message = "组织邮件")
    @ApiModelProperty(value = "组织邮件")
    private String email;

    @TableField(value = "GRPCODE")
    @NotEmpty(message = "组织编码")
    @ApiModelProperty(value = "组织编码")
    private String code;

    @TableField(exist = false)
    @ApiModelProperty(value = "子节点集合")
    private List<Site> children;
}
