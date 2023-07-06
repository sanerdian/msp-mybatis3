package com.jnetdata.msp.visual.relationmodulefield.model;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.util.ObjectUtils;
import org.thenicesys.data.api.EntityId;

import java.util.Date;

@Data
@TableName("j_relation_module_field")
@ApiModel(value="RelationModuleField对象", description="组件与字段关联表")
public class RelationModuleField implements EntityId<Long>, Comparable<RelationModuleField> {

    @ApiModelProperty(value = "id", hidden = true)
    @TableId(value = "relation_id", type = IdType.ID_WORKER)
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long id;

    @ApiModelProperty(value = "组件id")
    @TableField("module_id")
    private Long moduleId;

    @ApiModelProperty(value = "组件类型code")
    @TableField("module_type")
    private String moduleType;

    @ApiModelProperty(value = "元数据字段表id")
    @TableField("field_id")
    private String fieldId;

    @ApiModelProperty(value = "字段排序(用于关联多个字段的组件，比如表格组件)")
    @TableField("field_order")
    private Integer fieldOrder;

    @ApiModelProperty(value = "字段显示宽度（用于表格组件等）")
    @TableField("show_width")
    private String showWidth;

    @ApiModelProperty(value = "字段英文名")
    @TableField("field_name")
    private String fieldname;

    @ApiModelProperty(value = "字段中文名")
    @TableField(exist = false)
    private String anothername;

    @ApiModelProperty(value = "创建时间")
    @TableField("create_time")
    private Date createTime;

    @ApiModelProperty(value = "字段是否关联样式,0-否，1-是")
    @TableField("style_flag")
    private String styleFlag;

    @ApiModelProperty(value = "是否必须传参,0否，1是")
    @TableField("is_pass_ginseng")
    private String isPassGinseng;

    @ApiModelProperty(value = "字段值")
    @TableField("field_value")
    private String fieldValue;

    @ApiModelProperty(value = "系列类型")
    @TableField("series_type")
    private String seriesType;

    @ApiModelProperty(value = "图表显示颜色")
    @TableField("field_color")
    private String fieldColor;

    @ApiModelProperty(value = "字段关联的事件")
    @TableField("event_type")
    private String eventType;


    //------------------------表单元素冗余字段start----------------------------
    @ApiModelProperty(value = "文本类型")
    @TableField(exist = false)
    private String fieldType;

    @ApiModelProperty(value = "上传组件id")
    @TableField(exist = false)
    private String dataAreaId;

    @ApiModelProperty(value = "上传文件扩展名")
    @TableField(exist = false)
    private String fileExtension;

    @ApiModelProperty(value = "日期类型扩展名")
    @TableField(exist = false)
    private String dateType;

    @ApiModelProperty(value = "是否必填项,0否，1是")
    @TableField(exist = false)
    private String isChoose;
    //------------------------表单元素冗余字段end----------------------------


    @Override
    public int compareTo(RelationModuleField field){
        if(ObjectUtils.isEmpty(this.getFieldOrder())){
            this.setFieldOrder(0);
        }
        if(ObjectUtils.isEmpty(field.getFieldOrder())){
            field.setFieldOrder(0);
        }
        return this.getFieldOrder().compareTo(field.getFieldOrder());
    }

}
