package com.jnetdata.msp.config.config.model;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.jnetdata.msp.core.model.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
//import org.hibernate.validator.constraints.NotEmpty;
import org.thenicesys.data.api.EntityId;
import org.thenicesys.fastjson.serializer.ToStringSerializer;

/**
 * 路径配置实体类
 * Created by Admin on 2019/3/11.
 */

@Data
@TableName("SYSCONFIG")
@ApiModel(value = "配置信息实体类",description = "配置信息")
public class ConfigModel extends BaseEntity implements EntityId<Long> {
    private static final long serialVersionUID = 1L;

    public static final String dir_pic = "dir_pic";
    public static final String dir_front = "dir_front";
    public static final String dir_pub = "dir_pub";
    public static final String dir_preview = "dir_preview";
    public static final String dir_file = "dir_file";
    public static final String dir_upload_base = "dir_upload_base";
    public static final String dir_video = "dir_video";
    public static final String dir_audio = "dir_audio";
    public static final String dir_headimg = "dir_headimg";
    public static final String dir_watermaker = "dir_watermaker";
    public static final String dir_thumbnail = "dir_thumbnail";

    @ApiModelProperty(value = "主键id", hidden = true)
    @TableId(value = "CONFIGID", type = IdType.ID_WORKER)
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long id;

    @TableField(value = "CTYPE")
    @ApiModelProperty(value = "模块分类")
    private String ctype;

    @TableField(value = "CKEY")
    @ApiModelProperty(value = "属性名")
    private String name;

    @TableField(value = "MARK")
    @ApiModelProperty(value = "模块标识")
    private String mark ;

    @TableField(value = "CVALUE")
    @ApiModelProperty(value = "属性值")
    private String value;

    @TableField(value = "CDESC")
    @ApiModelProperty(value = "描述")
    private String cdesc;

    @TableField(value = "SITEID")
    @ApiModelProperty(value = "站点信息主键")
    private Long siteId;




}
