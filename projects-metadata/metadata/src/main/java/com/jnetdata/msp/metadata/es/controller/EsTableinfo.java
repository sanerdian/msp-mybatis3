package com.jnetdata.msp.metadata.es.controller;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;
import com.jnetdata.msp.core.es.EsFieldInfo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class EsTableinfo {


    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long dbSourceId;

    @ApiModelProperty(value = "索引名", example = "jmeta_xxx")
    private String index;

    private List<EsFieldInfo> fieldInfo;

    public EsTableinfo(){

    }

    public EsTableinfo(String index) {
        this.index = index;
    }

    public EsTableinfo(Long dbSourceId, String index) {
        this.dbSourceId = dbSourceId;
        this.index = index;
    }
}
