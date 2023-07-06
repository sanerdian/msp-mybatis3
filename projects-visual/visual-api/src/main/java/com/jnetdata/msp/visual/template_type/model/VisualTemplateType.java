package com.jnetdata.msp.visual.template_type.model;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.jnetdata.msp.core.model.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.thenicesys.data.api.EntityId;
import org.thenicesys.fastjson.serializer.ToStringSerializer;

/**
 * <p>
 * 组件类型
 * </p>
 *模板类型实体类
 * @author 王树彬
 * @since 2022-08-08
 */
@Data
@TableName("j_visual_template_type")
@ApiModel(value = "模板类型实体类", description = "模板类型")
public class VisualTemplateType extends BaseEntity implements EntityId<Long>  {

    private static final long serialVersionUID=1L;

    @ExcelProperty(value = "类型名称")
    @ApiModelProperty(value = "类型名称")
    @TableField("TITLE")
    private String title;
    @TableField("DOCSTATUS")
    private Integer docstatus;

    @ExcelProperty(value = "主键id")
    @ApiModelProperty(value = "主键id")
    @JSONField(serializeUsing = ToStringSerializer.class)
    @TableId(value = "ID", type = IdType.AUTO)
    private Long id;

}
