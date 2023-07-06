package com.jnetdata.msp.log.metadata.model;

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
@TableName("t_metadata_log")
@ApiModel(value = "元数据日志实体类",description = "元数据操作日志")
public class MetadataLog extends BaseEntity implements EntityId<Long> {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键id", hidden = true)
    @TableId(value = "id", type = IdType.ID_WORKER)
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long id;

    @TableField(value = "HANDLETYPE")
    @ApiModelProperty(value = "操作类型")
    private String handleType;

    @TableField(value = "NAME")
    @ApiModelProperty(value = "表名称")
    private String name;

    @TableField(value = "TITLE")
    @ApiModelProperty(value = "标题名")
    private String title;

    @TableField(value = "RESULT")
    @ApiModelProperty(value = "操作结果")
    private String result;

    @TableField(value = "DETAIL")
    @ApiModelProperty(value = "操作明细")
    private String detail;

    @TableField(value = "CRUser")
    @ApiModelProperty(value = "创建人")
    private String crUser;

    @TableField(value = "CRTIME")
    @ApiModelProperty(value = "创建时间")
    @JSONField(format = "yyyy-MM-dd  HH:mm:ss")
    private Date createTime;

    @TableField(value = "ADDRESS")
    @ApiModelProperty(value = "IP地址")
    private String address;

    @TableField(exist = false)
    private Date endTime;
}
