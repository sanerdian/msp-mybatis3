package com.jnetdata.msp.visual.relationmodulefield.vo;

import com.baomidou.mybatisplus.annotations.TableField;
import com.jnetdata.msp.metadata.fieldinfo.model.Fieldinfo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class FieldinfoVo extends Fieldinfo {

    @ApiModelProperty(value = "是否已选择，0-未选择，1-已选择")
    @TableField(exist = false)
    private String selectFlag;

    @ApiModelProperty(value = "关联的元数据表id")
    @TableField(exist = false)
    private String dbTableId;

    @ApiModelProperty(value = "组件id")
    @TableField(exist = false)
    private Long moduleId;

    @ApiModelProperty(value = "组件类型code")
    @TableField(exist = false)
    private String moduleType;

    @ApiModelProperty(value = "字段关联的事件")
    @TableField(exist = false)
    private String eventType;

    @ApiModelProperty(value = "字段是否关联样式,0-否，1-是")
    @TableField(exist = false)
    private String styleFlag;

    @ApiModelProperty(value = "字段显示宽度（用于表格组件等）")
    @TableField(exist = false)
    private String showWidth;
}
