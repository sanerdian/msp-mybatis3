package com.jnetdata.msp.member.menu.model;

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
 * Created by TF on 2019/3/19.
 */




@Data
@TableName("MENUMAN")
@ApiModel(description = "菜单管理")
public class TreeMenu extends BaseEntity implements EntityId<Long> {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "id", hidden = true)
    @TableId(value = "MENUID", type = IdType.ID_WORKER)
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long id;


    @TableField(value = "NAME")
    @NotEmpty(message = "标题")
    @ApiModelProperty(value = "标题")
    private String name;

    @TableField(value = "PARENTID")
    @NotEmpty(message = "父级id")
    @ApiModelProperty(value = "父级id")
    private Long parentId;

}
