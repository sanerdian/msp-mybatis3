package com.jnetdata.msp.visual.modulebutton.model;

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
@TableName("j_module_button")
@ApiModel(value="ModuleButton", description="按钮组件表")
public class ModuleButton extends ModuleObject implements EntityId<Long>  {

    @ApiModelProperty(value = "id", hidden = true)
    @TableId(value = "button_id", type = IdType.ID_WORKER)
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long id;

    @ApiModelProperty(value = "按钮名称")
    @TableField("button_name")
    private String buttonName;


    @ApiModelProperty(value = "按钮英文名")
    @TableField("another_name")
    private String anotherName;

    @ApiModelProperty(value = "关联的事件")
    @TableField("event_type")
    private String eventType;

    @ApiModelProperty(value = "上级组件id")
    @TableField("parent_id")
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long parentId;

}
