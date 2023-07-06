package com.jnetdata.msp.media.model;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.thenicesys.fastjson.serializer.ToStringSerializer;

import java.util.Date;

@Data
@TableName("jmeta_performance_index")
@ApiModel(description = "绩效指数")
public class PerformanceIndex {
    @ApiModelProperty(value = "id",hidden = true)
    @TableId(value = "id",type = com.baomidou.mybatisplus.enums.IdType.ID_WORKER)
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long id;

    @TableField(value = "stat_time")
    @ApiModelProperty(value = "统计时间（按月）")
    private Date statTime;

    @TableField(value = "performance_value")
    @ApiModelProperty(value = "绩效指数值(0~100)")
    private Integer performanceValue;

    @TableField(value = "site_cannot_access")
    @ApiModelProperty(value = "网站无法访问（0或1，1表示单项否决）")
    private Integer siteCannotAccess;

    @TableField(value = "website_not_update")
    @ApiModelProperty(value = "网站不更新（0或1，1表示单项否决）")
    private Integer websiteNotUpdate;

    @TableField(value = "section_not_update")
    @ApiModelProperty(value = "栏目不更新（0或1，1表示单项否决）")
    private Integer sectionNotUpdate;

    @TableField(value = "serious_mistake")
    @ApiModelProperty(value = "严重错误（0或1，1表示单项否决）")
    private Integer seriousMistake;

    @TableField(value = "interact_poor")
    @ApiModelProperty(value = "互动回应差（0或1，1表示单项否决）")
    private Integer interactPoor;

    @TableField(value = "create_time")
    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @TableField(value = "update_time")
    @ApiModelProperty(value = "修改时间")
    private Date updateTime;
}
