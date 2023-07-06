package com.jnetdata.msp.visual.moduleform.model;

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
@TableName("j_module_form")
@ApiModel(value="ModuleForm", description="表单组件表")
public class ModuleForm extends ModuleObject implements EntityId<Long> {

    @ApiModelProperty(value = "id", hidden = true)
    @TableId(value = "form_id", type = IdType.ID_WORKER)
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long id;

    @ApiModelProperty(value = "关联的元数据表id")
    @TableField("db_table_id")
    private String dbTableId;

    @ApiModelProperty(value = "表单分类id")
    @TableField("form_class_id")
    private Integer formClassId;

    @ApiModelProperty(value = "表单名称")
    @TableField("form_name")
    private String formName;

}
