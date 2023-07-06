package com.jnetdata.msp.metadata.viewlog.model;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.thenicesys.fastjson.serializer.ToStringSerializer;

import java.util.Date;

/**
 * 功能描述：
 */
@Data
@TableName("t_metadataoperator_log")
@ApiModel(value = "视图字段管理实体类",description = "视图字段管理")
public class MetaDataOperator {
    @ApiModelProperty(value = "id", hidden = true)
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long id;

    @TableField(value = "ip_address")
    private String ipaddress;


    @TableField(value = "user_name")
    private String username;

    @TableField(value = "userid")
    private Long userid;


    private String handletype;

    @ApiModelProperty(value = "创建时间", example = "2019-06-18 16:56:30")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date crtime;

    @ApiModelProperty(value = "创建时间", example = "2019-06-18 16:56:30")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date updatee;


    private  String upname;

    private  Long upid;

    private String title;

    @TableField(exist = false)
    private Date endTime;
}
