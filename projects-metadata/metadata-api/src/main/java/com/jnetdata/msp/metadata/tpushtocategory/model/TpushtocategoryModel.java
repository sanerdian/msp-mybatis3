package com.jnetdata.msp.metadata.tpushtocategory.model;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import org.thenicesys.data.api.EntityId;
import org.thenicesys.fastjson.serializer.ToStringSerializer;

import java.util.Date;

@Data
@TableName("t_pushtocategory")
public class TpushtocategoryModel implements EntityId<Long> {
    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty(value = "id", hidden = true)
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long id;
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date crtime;
    @ApiModelProperty(value = "推送的id")
    @TableField("xwid")
    private Long xwid;
    @TableField("classid")
    private Long classid;
    @TableField("classname")
    private String classname;
    @TableField("tableid")
    private Long tableid;
    @TableField("tablename")
    private String tablename;
    @TableField("cruser")
    private String cruser;
    @TableField("pushid")
    private Long pushid;
    @TableField("pushname")
    private String pushname;
}
