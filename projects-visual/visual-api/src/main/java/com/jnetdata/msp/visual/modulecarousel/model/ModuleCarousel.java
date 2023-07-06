package com.jnetdata.msp.visual.modulecarousel.model;

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
@TableName("j_module_carousel")
@ApiModel(value="ModuleCarousel", description="轮播组件表")
public class ModuleCarousel extends ModuleObject implements EntityId<Long>  {

    @ApiModelProperty(value = "id", hidden = true)
    @TableId(value = "carousel_id", type = IdType.ID_WORKER)
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long id;

    @ApiModelProperty(value = "每页显示数据条数")
    @TableField("page_size")
    private Integer pageSize;

    @ApiModelProperty(value = "每页显示条数")
    @TableField("page_total")
    private Integer pageTotal;

    @ApiModelProperty(value = "关联的元数据表id")
    @TableField("db_table_id")
    private String dbTableId;

    @ApiModelProperty(value = "数据区域id")
    @TableField("data_area_id")
    private String dataAreaId;
}
