package com.jnetdata.msp.visual.modulesearch.model;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.jnetdata.msp.visual.moduleobject.model.ModuleObject;
import com.jnetdata.msp.visual.relationmodulefield.model.RelationModuleField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.thenicesys.data.api.EntityId;

import java.util.List;

@Data
@TableName("j_module_search")
@ApiModel(value="搜索组件实体类", description="搜索组件")
public class ModuleSearch extends ModuleObject implements EntityId<Long> {

    @ApiModelProperty(value = "主键id", hidden = true)
    @TableId(value = "search_id", type = IdType.ID_WORKER)
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long id;

    @ApiModelProperty(value = "关联的元数据表id")
    @TableField("db_table_id")
    private String dbTableId;

    @ApiModelProperty(value = "搜索关系，关联分类法")
    @TableField("search_relation")
    private String searchRelation;

    @ApiModelProperty(value = "数据区域id")
    @TableField("data_area_id")
    private String dataAreaId;
}
