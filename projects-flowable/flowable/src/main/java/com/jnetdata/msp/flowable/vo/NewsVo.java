package com.jnetdata.msp.flowable.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 新闻
 */
@Data
public class NewsVo {


    @ApiModelProperty(value = "新闻状态")
    private String newsStatus;

    @ApiModelProperty(value = "栏目ID")
    private String columnId;
}
