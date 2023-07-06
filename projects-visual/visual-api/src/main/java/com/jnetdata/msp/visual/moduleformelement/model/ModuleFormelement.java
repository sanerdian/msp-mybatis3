package com.jnetdata.msp.visual.moduleformelement.model;

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

@Data
@TableName("j_module_formelement")
@ApiModel(value="ModuleFormelement", description="表单元素组件表")
public class ModuleFormelement extends ModuleObject implements EntityId<Long>  {

    @ApiModelProperty(value = "id", hidden = true)
    @TableId(value = "element_id", type = IdType.ID_WORKER)
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long id;

    @ApiModelProperty(value = "关联的元数据表id")
    @TableField("db_table_id")
    private String dbTableId;

    @ApiModelProperty(value = "关联的元数据字段id")
    @TableField("db_field_id")
    private String dbFieldId;

    @ApiModelProperty(value = "所属表单id")
    @TableField("form_id")
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long formId;

    @ApiModelProperty(value = "文本类型")
    @TableField("field_type")
    private String fieldType;

    @ApiModelProperty(value = "上传组件id")
    @TableField("data_area_id")
    private String dataAreaId;


    @ApiModelProperty(value = "上传文件扩展名")
    @TableField("file_extension")
    private String fileExtension;

    @ApiModelProperty(value = "日期类型扩展名")
    @TableField("date_type")
    private String dateType;

    @ApiModelProperty(value = "是否必填项,0否，1是")
    @TableField("is_choose")
    private String isChoose;


}
