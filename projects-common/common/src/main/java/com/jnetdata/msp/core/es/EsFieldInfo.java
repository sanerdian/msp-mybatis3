package com.jnetdata.msp.core.es;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class EsFieldInfo {


    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long dbSourceId;

    @ApiModelProperty(value = "索引名")
    private String index;

    @ApiModelProperty(value = "字段名")
    private String key;

    @ApiModelProperty(value = "类型")
    private String type;
}
