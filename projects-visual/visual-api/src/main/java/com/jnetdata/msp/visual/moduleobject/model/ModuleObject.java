package com.jnetdata.msp.visual.moduleobject.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.jnetdata.msp.visual.relationmodulefield.model.RelationModuleField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 组件对象
 * 各种组件的公共部分
 */
@Data
public class ModuleObject {

    @ApiModelProperty(value = "可视化模板id")
    @TableField("visual_template_id")
    private Long visualTemplateId;

    @ApiModelProperty(value = "创建时间")
    @TableField("create_time")
    private Date createTime;

    @ApiModelProperty(value = "更新时间")
    @TableField("update_time")
    private Date updateTime;

    @ApiModelProperty(value = "字体")
    @TableField("font_family")
    private String fontFamily;

    @ApiModelProperty(value = "字号")
    @TableField("font_size")
    private Integer fontSize;

    @ApiModelProperty(value = "字体颜色")
    @TableField("font_color")
    private String fontColor;

    @ApiModelProperty(value = "组件样式名称")
    @TableField("style_name")
    private String styleName;

    @ApiModelProperty(value = "关联字段列表")
    @TableField(exist = false)
    private List<RelationModuleField> fieldList;

    @ApiModelProperty(value = "组件类型code")
    @TableField(exist = false)
    private String moduleType;
}
