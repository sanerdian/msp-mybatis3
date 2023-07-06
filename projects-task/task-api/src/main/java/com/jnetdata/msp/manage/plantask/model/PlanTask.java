package com.jnetdata.msp.manage.plantask.model;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.jnetdata.msp.core.model.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.thenicesys.data.api.EntityId;
import org.thenicesys.fastjson.serializer.ToStringSerializer;

import javax.validation.constraints.NotEmpty;
import java.util.Date;

@Data
@TableName("plantask")
@ApiModel(description = "计划任务调度")
public class PlanTask extends BaseEntity implements EntityId<Long> {
    @ApiModelProperty(value = "id",hidden = true)
    @TableId(value = "id",type = IdType.ID_WORKER)
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long id;

    @TableField(value = "name")
    @NotEmpty(message = "名称")
    @ApiModelProperty(value = "名称")
    private String name;

    @TableField(value = "describe")
    @NotEmpty(message = "描述")
    @ApiModelProperty(value = "描述")
    private String describe;

    @TableField(value = "pattern")
    @NotEmpty(message = "运行模式")
    @ApiModelProperty(value = "运行模式")
    private int pattern;

    @TableField(value = "fileType")
    @NotEmpty(message = "文件类型")
    @ApiModelProperty(value = "文件类型")
    private String fileType;

    @TableField(value = "opeparam")
    @NotEmpty(message = "操作参数")
    @ApiModelProperty(value = "操作参数")
    private String opeparam;

    @TableField(value = "tasktype")
    @NotEmpty(message = "任务类型")
    @ApiModelProperty(value = "任务类型")
    private String tasktype;

    @TableField(value = "startTime")
    @NotEmpty(message = "执行时间")
    @ApiModelProperty(value = "执行时间")
    private Date startTime;

    @TableField(value = "endTime")
    @NotEmpty(message = "结束时间")
    @ApiModelProperty(value = "结束时间")
    private Date endTime;

    @TableField(value = "interval")
    @NotEmpty(message = "间隔时间")
    @ApiModelProperty(value = "间隔时间")
    private String interval;

    @TableField(value = "deldate")
    @NotEmpty(message = "'删除几天前的文件'")
    @ApiModelProperty(value = "'删除几天前的文件'")
    private Long deldate;
}
