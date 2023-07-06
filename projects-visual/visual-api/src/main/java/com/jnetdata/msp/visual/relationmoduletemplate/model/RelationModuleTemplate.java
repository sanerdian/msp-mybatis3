package com.jnetdata.msp.visual.relationmoduletemplate.model;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.thenicesys.data.api.EntityId;

import java.util.Date;

@Data
@TableName("j_relation_module_template")
@ApiModel(value="RelationModuleTemplate对象", description="组件与可视化模板关联表")
public class RelationModuleTemplate implements EntityId<Long> {

    @ApiModelProperty(value = "id", hidden = true)
    @TableId(value = "relation_id", type = IdType.ID_WORKER)
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long id;

    @ApiModelProperty(value = "组件id")
    @TableField("module_id")
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long moduleId;

    @ApiModelProperty(value = "组件类型code")
    @TableField("module_type")
    private String moduleType;

    @ApiModelProperty(value = "可视化模板id")
    @TableField("visual_template_id")
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long visualTemplateId;

    @ApiModelProperty(value = "创建时间")
    @TableField("create_time")
    private Date createTime;

    @ApiModelProperty(value = "关联的元数据表id")
    @TableField(exist = false)
    private String dbTableId;

    @ApiModelProperty(value = "是否第一个该类型组件")
    @TableField(exist = false)
    private boolean first = true;

    @ApiModelProperty(value = "序号，用于生成对应js函数后缀")
    @TableField(exist = false)
    private Integer order;

}
