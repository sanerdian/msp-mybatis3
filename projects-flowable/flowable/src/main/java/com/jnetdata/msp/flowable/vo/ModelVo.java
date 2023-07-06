package com.jnetdata.msp.flowable.vo;

import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 工作流模型
 */
@Data
public class ModelVo {

    @ApiModelProperty(value = "模型id")
    private String id;

    @ApiModelProperty(value = "模型名称")
    private String name;

    @ApiModelProperty(value = "创建时间")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @ApiModelProperty(value = "修改时间")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    @ApiModelProperty(value = "模型key")
    private String key;
}
