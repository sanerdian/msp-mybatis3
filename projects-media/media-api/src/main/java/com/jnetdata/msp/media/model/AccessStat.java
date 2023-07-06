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
@TableName("jmeta_access_stat")
@ApiModel(description = "访问统计")
public class AccessStat {
    @ApiModelProperty(value = "id",hidden = true)
    @TableId(value = "id",type = com.baomidou.mybatisplus.enums.IdType.ID_WORKER)
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long id;

    @TableField(value = "stat_time")
    @ApiModelProperty(value = "统计时间（按天）")
    private Date statTime;

    @TableField(value = "success_count")
    @ApiModelProperty(value = "成功次数")
    private Integer successCount;

    @TableField(value = "failure_count")
    @ApiModelProperty(value = "失败次数")
    private Integer failureCount;

    @TableField(value = "create_time")
    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @TableField(value = "update_time")
    @ApiModelProperty(value = "修改时间")
    private Date updateTime;
}
