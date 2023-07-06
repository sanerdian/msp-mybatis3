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
@TableName("jmeta_problem_stat")
@ApiModel(description = "问题统计")
public class ProblemStat {
    @ApiModelProperty(value = "id",hidden = true)
    @TableId(value = "id",type = com.baomidou.mybatisplus.enums.IdType.ID_WORKER)
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long id;

    @TableField(value = "stat_time")
    @ApiModelProperty(value = "统计时间（按天）")
    private Date statTime;

    @TableField(value = "problem_count")
    @ApiModelProperty(value = "问题数量")
    private Integer problemCount;

    @TableField(value = "warning_count")
    @ApiModelProperty(value = "预警数量")
    private Integer warningCount;

    @TableField(value = "create_time")
    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @TableField(value = "update_time")
    @ApiModelProperty(value = "修改时间")
    private Date updateTime;
}
