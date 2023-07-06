package com.jnetdata.msp.metadata.encoding.model;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.thenicesys.fastjson.serializer.ToStringSerializer;

import java.util.Date;

@Data
@TableName("encoding")
public class Encodingmodel {
    @TableId(value = "id", type = IdType.ID_WORKER)
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long id;
    @TableField("bmname")
    private String bmname;
    @TableField("bmstyle")
    private String bmstyle;
    @TableField("stutas")
    private int stutas;

    @TableField("crtime")
    @ApiModelProperty(value = "创建时间搜索" , example = "2019-01-22 17:32:24")
    @JSONField(format = "yyyy-MM-dd hh:mm:ss")
    private Date crtime;

    @TableField("cruser")
    private String cruser;
    @TableField("typee")
    private int typee;
    @TableField("cumulative")
    private int cumulative;
    @TableField("digit")
    private int digit;
    @TableField("nummber")
    private int nummber;
    @TableField("prefix")
    private String prefix;

}
