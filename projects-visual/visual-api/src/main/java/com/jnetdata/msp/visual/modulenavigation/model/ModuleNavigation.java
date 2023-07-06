package com.jnetdata.msp.visual.modulenavigation.model;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.jnetdata.msp.visual.moduleobject.model.ModuleObject;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.thenicesys.data.api.EntityId;

@Data
@TableName("j_module_navigation")
@ApiModel(value="菜单组件实体类", description="菜单组件")
public class ModuleNavigation  extends ModuleObject implements EntityId<Long> {

    @ApiModelProperty(value = "主键id", hidden = true)
    @TableId(value = "navigation_id", type = IdType.ID_WORKER)
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long id;

}
