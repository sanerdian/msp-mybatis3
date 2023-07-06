package com.jnetdata.msp.log.content.model;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.jnetdata.msp.core.model.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.thenicesys.data.api.EntityId;
import org.thenicesys.fastjson.serializer.ToStringSerializer;

import javax.validation.constraints.NotEmpty;
import java.util.Date;

/**
 * Created by TF on 2019/3/13.
 */

@Data
@TableName("t_content_log")
@ApiModel(value = "内容日志实体类",description = "内容操作日志")
public class ContentLog extends BaseEntity implements EntityId<Long> {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键id", hidden = true)
    @TableId(value = "id", type = IdType.ID_WORKER)
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long id;

    @TableField(value = "handle_type")
    @NotEmpty(message = "操作类型")
    @ApiModelProperty(value = "操作类型")
    private String handleType;

    @TableField(value = "content_type")
    @NotEmpty(message = "菜单名称")
    @ApiModelProperty(value = "菜单名称")
    private String contentType;

    @TableField(value = "columnid")
    @NotEmpty(message = "栏目id")
    @ApiModelProperty(value = "栏目id")
    private Long columnid;
    @TableField(value = "siteid")
    @NotEmpty(message = "网站id")
    @ApiModelProperty(value = "网站id")
    private Long siteid;

    @TableField(value = "column_name")
    @NotEmpty(message = "栏目")
    @ApiModelProperty(value = "栏目")
    private String columnName;

    @TableField(value = "content")
    @NotEmpty(message = "内容")
    @ApiModelProperty(value = "内容")
    private String content;

    @TableField(value = "c_detail")
    @NotEmpty(message = "操作明细")
    @ApiModelProperty(value = "操作明细")
    private String cDetail;

    @TableField(value = "RESULT")
    @NotEmpty(message = "操作状态")
    @ApiModelProperty(value = "操作状态")
    private String result;

    @TableField(exist = false)
    private Date endTime;
}
