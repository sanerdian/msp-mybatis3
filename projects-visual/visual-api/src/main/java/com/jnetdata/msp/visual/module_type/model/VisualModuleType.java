package com.jnetdata.msp.visual.module_type.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.jnetdata.msp.core.model.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import org.thenicesys.data.api.EntityId;
import org.thenicesys.fastjson.serializer.ToStringSerializer;
import com.alibaba.excel.annotation.ExcelProperty;

/**
 * <p>
 * 组件类型
 * </p>
 *
 * @author zyj
 * @since 2022-08-08
 */
@Data
@TableName("j_visual_module_type")
@ApiModel(value = "组件类型实体类", description = "组件类型")
public class VisualModuleType extends BaseEntity implements EntityId<Long>  {

    private static final long serialVersionUID=1L;

    @ExcelProperty(value = "类型名称")
    @ApiModelProperty(value = "类型名称")
    @TableField("TITLE")
    private String title;

    @ApiModelProperty(value = "类型编码")
    @TableField("TYPE_CODE")
    private String typeCode;

    @ExcelProperty(value = "图标")
    @ApiModelProperty(value = "图标")
    @TableField("ICON")
    private String icon;
    @ExcelProperty(value = "父级ID")
    @ApiModelProperty(value = "父级ID")
    @TableField("PARENT_ID")
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long parentId;
    @ExcelProperty(value = "父级名称")
    @ApiModelProperty(value = "父级名称")
    @TableField("PARENT_TITLE")
    private String parentTitle;
    @TableField("DOCSTATUS")
    private Integer docstatus;

    @ExcelProperty(value = "主键id")
    @ApiModelProperty(value = "主键id")
    @JSONField(serializeUsing = org.thenicesys.fastjson.serializer.ToStringSerializer.class)
    @TableId(value = "ID", type = IdType.AUTO)
    private Long id;

}
