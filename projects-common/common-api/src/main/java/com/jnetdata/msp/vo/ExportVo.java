package com.jnetdata.msp.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.thenicesys.data.api.Pager;

import java.util.List;

@Data
public class ExportVo<T> {
    @ApiModelProperty(value = "类型(excel,dat,txt),可不传,默认excle")
    private String ext;
    @ApiModelProperty(value = "范围可不传,默认excle")
    private Integer area;
    @ApiModelProperty(value = "可不传")
    private Long tableId;
    @ApiModelProperty(value = "可不传,是否导出标题(1:是,2:否)")
    private Integer includeTitle;
    private T entity;
    @ApiModelProperty(value = "可不传,每页条数")
    private Integer pageSize;
    @ApiModelProperty(value = "可不传,开始页数")
    private Integer pageStartNo;
    @ApiModelProperty(value = "可不传,结束页数")
    private Integer pageEndNo;
    @ApiModelProperty(value = "导出字段")
    private List<String> fields;

    public String getExt(){
        if(ext == null) return "excel";
        return ext;
    }

    public Integer getArea(){
        if(area == null) return 1;
        return area;
    }

    public Integer getIncludeTitle(){
        if(includeTitle == null) return 1;
        return includeTitle;
    }
}
