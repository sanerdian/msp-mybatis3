package com.jnetdata.msp.log.publish.model;

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
@TableName("t_publish_log")
@ApiModel(value = "发布信息日志实体类",description = "发布日志信息")
public class PublishLog extends BaseEntity implements EntityId<Long> {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键id", hidden = true)
    @TableId(value = "id", type = IdType.ID_WORKER)
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long id;

    @TableField(value = "PATH")
    @NotEmpty(message = "发布路径")
    @ApiModelProperty(value = "发布路径")
    private String path;

    @TableField(value = "COLUMNNAME")
    @NotEmpty(message = "栏目")
    @ApiModelProperty(value = "栏目")
    private String columnname;


    @TableField(value = "PATHNAME")
    @NotEmpty(message = "发布名")
    @ApiModelProperty(value = "发布名")
    private String pathName;

    @TableField(value = "CRUser")
    @NotEmpty(message = "创建人")
    @ApiModelProperty(value = "创建人")
    private String crUser;

    @TableField(value = "CRTIME")
    @NotEmpty(message = "创建时间")
    @ApiModelProperty(value = "创建时间")
    @JSONField(format = "yyyy-MM-dd  HH:mm:ss")
    private Date createTime;

    @TableField(value = "ADDRESS")
    @NotEmpty(message = "IP地址")
    @ApiModelProperty(value = "IP地址")
    private String address;

    @TableField(exist = false)
    private Date endTime;
}
