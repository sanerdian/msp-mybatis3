package com.jnetdata.msp.visual.relationmodulefield.vo;

import com.jnetdata.msp.visual.relationmodulefield.model.RelationModuleField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 组件关联的元数据信息
 */
@Data
public class ModuleRelation {

    @ApiModelProperty(value = "微服务名称")
    private String serviceName;

    @ApiModelProperty(value = "元数据实体名称")
    private String entityName;

    @ApiModelProperty(value = "字段信息")
    private List<RelationModuleField> fieldList;
}
