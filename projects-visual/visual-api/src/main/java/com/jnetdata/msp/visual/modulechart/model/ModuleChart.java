package com.jnetdata.msp.visual.modulechart.model;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.jnetdata.msp.visual.moduleobject.model.ModuleObject;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.thenicesys.data.api.EntityId;

import java.util.Date;

@Data
@TableName("j_module_chart")
@ApiModel(value="ModuleChart对象", description="echarts组件表")
public class ModuleChart extends ModuleObject implements EntityId<Long> {

    @ApiModelProperty(value = "id", hidden = true)
    @TableId(value = "chart_id", type = IdType.ID_WORKER)
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long id;

    @ApiModelProperty(value = "可视化模板id")
    @TableField("visual_template_id")
    private Long visualTemplateId;

    @ApiModelProperty(value = "关联的元数据表id")
    @TableField("db_table_id")
    private String dbTableId;

    @ApiModelProperty(value = "最大值")
    @TableField("maximum")
    private Long maximum;

    @ApiModelProperty(value = "平均值")
    @TableField("average_value")
    private Long averageValue;

    @ApiModelProperty(value = "分页设置，是否显示分页，0-否，1-是")
    @TableField("page_setup")
    private String pageSetup;

    @ApiModelProperty(value = "每页显示数据条数")
    @TableField("page_size")
    private Integer pageSize;

    @ApiModelProperty(value = "是否显示表格")//1显示 0不显示
    @TableField("show_table")
    private Integer showTable;

    @ApiModelProperty(value = "是否显示图标")//1显示 0不显示
    @TableField("show_char")
    private Integer showChar;

    @ApiModelProperty(value = "数据区域id")
    @TableField("data_area_id")
    private String dataAreaId;

    @ApiModelProperty(value = "图表X轴单位")
    @TableField("unit_xchart")
    private String unitXchart;

    @ApiModelProperty(value = "图表Y轴单位")
    @TableField("unit_ychart")
    private String unitYchart;

    @ApiModelProperty(value = "图标类型")
    @TableField("view_type")
    private String viewType;

}
