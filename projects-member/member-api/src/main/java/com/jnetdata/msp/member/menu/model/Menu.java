package com.jnetdata.msp.member.menu.model;

import com.alibaba.excel.annotation.ExcelProperty;
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
 * Created by TF on 2019/3/19.
 */




@Data
@TableName("MENUMAN")
@ApiModel(value = "菜单管理实体类",description = "菜单管理")
public class Menu extends BaseEntity implements EntityId<Long> {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键id", hidden = true)
    @TableId(value = "MENUID", type = IdType.ID_WORKER)
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long id;

    @TableField(value = "type")
    @NotEmpty(message = "是否前台展示")
    @ApiModelProperty(value = "是否前台展示")
    private Integer type;


    @TableField(value = "natural_id")
    @NotEmpty(message = "自然id")
    @ApiModelProperty(value = "自然id")
    private String naturalId;

    @TableField(value = "NAME")
    @NotEmpty(message = "标题")
    @ApiModelProperty(value = "标题")
    private String name;

    @TableField(value = "url")
    @NotEmpty(message = "url")
    @ApiModelProperty(value = "url")
    private String url;

    @TableField(value = "seq")
    @NotEmpty(message = "排序字段")
    @ApiModelProperty(value = "排序字段")
    private Integer seq;

    @TableField(value = "PARENTID")
    @NotEmpty(message = "父级id")
    @ApiModelProperty(value = "父级id")
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long parentId;

    @TableField(value = "STATE")
    @NotEmpty(message = "是否活跃")
    @ApiModelProperty(value = "是否活跃")
    private Integer active;//菜单显示隐藏

    @TableField(value = "description")
    @NotEmpty(message = "描述")
    @ApiModelProperty(value = "描述")
    private String description;

    @TableField(value = "photo_url")
    @NotEmpty(message = "图片链接")
    @ApiModelProperty(value = "图片链接")
    private String photoUrl;

    @TableField(value = "CRUSER")
    @NotEmpty(message = "创建人")
    @ApiModelProperty(value = "创建人")
    private String crUser;

    @TableField(value = "MODSTR")
    @ApiModelProperty(value = "模块名")
    private String modstr;

    @TableField(value = "REALM_NAME")
    @ApiModelProperty(value = "域名")
    private String realmName;

    @TableField(value = "MOD_SUFFIX")
    @ApiModelProperty(value = "后缀")
    private String modSuffix;

    @TableField(exist = false)
    @NotEmpty(message = "父类名称")
    @ApiModelProperty(value = "父类名称")
    private String parentTitle;

    @TableField(exist = false)
    @NotEmpty(message = "子节点集合")
    @ApiModelProperty(value = "子节点集合")
    private List<Menu> children;

    @ApiModelProperty(value = "所属机构id")
    @JSONField(serializeUsing = ToStringSerializer.class)
    @TableField(value = "COMPANYID")
    private Long companyId;

    @TableField(value = "siteid")
    @ApiModelProperty(value = "站点id")
    @JSONField(serializeUsing = ToStringSerializer.class)
    @ExcelProperty("站点id")
    private Long siteId;

    @TableField(exist = false)
    @ApiModelProperty(value = "是否为站点")
    private Integer isSite=2;

    @TableField(exist = false)
    @ApiModelProperty(value = "树节点展开属性")
    private boolean isParent;

    public boolean getIsParent(){
        return isParent;
    }

    public void setIsParent(boolean isParent){
        this.isParent = isParent;
    }

    public String getIconSkin(){
        return parentId==null || parentId.equals(0L)?"menuTreeIcon1": parentId.equals(companyId)? "menuTreeIcon2" : "menuTreeIcon3";
    }
}
