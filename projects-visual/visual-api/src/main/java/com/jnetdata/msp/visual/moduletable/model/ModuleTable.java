package com.jnetdata.msp.visual.moduletable.model;

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

import java.util.List;
import java.util.Map;

@Data
@TableName("j_module_table")
@ApiModel(value="ModuleTable对象", description="表格组件表")
public class ModuleTable extends ModuleObject implements EntityId<Long>  {

    @ApiModelProperty(value = "id", hidden = true)
    @TableId(value = "table_id", type = IdType.ID_WORKER)
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long id;

    @ApiModelProperty(value = "表格样式，关联分类法")
    @TableField("table_style")
    private String tableStyle;

    @ApiModelProperty(value = "位置X")
    @TableField("position_x")
    private Integer positionX;

    @ApiModelProperty(value = "位置Y")
    @TableField("position_y")
    private Integer positionY;

    @ApiModelProperty(value = "高度")
    @TableField("size_width")
    private Integer sizeWidth;

    @ApiModelProperty(value = "宽度")
    @TableField("size_height")
    private Integer sizeHeight;

    @ApiModelProperty(value = "边距左")
    @TableField("margin_left")
    private Integer marginLeft;

    @ApiModelProperty(value = "边距上")
    @TableField("margin_top")
    private Integer marginTop;

    @ApiModelProperty(value = "边距右")
    @TableField("margin_right")
    private Integer marginRight;

    @ApiModelProperty(value = "边距下")
    @TableField("margin_bottom")
    private Integer marginBottom;

    @ApiModelProperty(value = "排版，1-左对齐，2-居中，3-右对齐")
    @TableField("type_setup")
    private String typeSetup;

    @ApiModelProperty(value = "选择设置，关联分类法")
    @TableField("select_setup")
    private String selectSetup;

    @ApiModelProperty(value = "分页设置，是否显示分页，0-否，1-是")
    @TableField("page_setup")
    private String pageSetup;

    @ApiModelProperty(value = "每页显示数据条数")
    @TableField("page_size")
    private Integer pageSize;

    @ApiModelProperty(value = "关联的元数据表id")
    @TableField("db_table_id")
    private String dbTableId;

    @ApiModelProperty(value = "操作显示设置，是否显示操作，0-否，1-是")
    @TableField("view_setup")
    private String viewSetup;

    @ApiModelProperty(value = "操作显示设置")
    @TableField("set_view")
    private String setView;

    @ApiModelProperty(value = "是否显示复选框")
    @TableField("set_check")
    private String setCheck;


    @ApiModelProperty(value = "是否显示序号")
    @TableField("set_num")
    private String setNum;

    @ApiModelProperty(value = "设置是否跳转或者弹出框")//link // layer
    @TableField("setform_type")
    private String setformType;

    @ApiModelProperty(value = "跳转地址")
    @TableField("form_adress")
    private String formAdress;

    @TableField(exist = false)
    @ApiModelProperty("字段列表")
    private Map<String,String> defindMap;

    @ApiModelProperty(value = "数据区域id")
    @TableField("data_area_id")
    private String dataAreaId;


}
