package com.jnetdata.msp.visual.moduledetail.model;

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
@TableName("j_module_detail")
@ApiModel(value="详情组件实体类", description="详情组件")
public class ModuleDetail extends ModuleObject implements EntityId<Long> {

    @ApiModelProperty(value = "主键id", hidden = true)
    @TableId(value = "detail_id", type = IdType.ID_WORKER)
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long id;

    @ApiModelProperty(value = "关联的元数据表id")
    @TableField("db_table_id")
    private String dbTableId;

}
