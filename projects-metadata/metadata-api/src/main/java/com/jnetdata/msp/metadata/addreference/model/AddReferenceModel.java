package com.jnetdata.msp.metadata.addreference.model;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.thenicesys.data.api.EntityId;
import org.thenicesys.fastjson.serializer.ToStringSerializer;

import java.util.Date;

@Data
@TableName("t_addreference")
public class AddReferenceModel implements EntityId<Long> {
    @TableId(value = "id", type = IdType.ID_WORKER)
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long id;


    private Long nameid;

    @TableField("name")
    private String name;

    @TableField("cruser")
    private String cruser;

    @TableField("type")
    private String type;

    @TableField("tablename")
    private String tablename;

    @TableField("tableid")
    private Long tableid;

    @TableField("xwname")
    private String xwname;

    @TableField("xwid")
    private Long xwid;
    @TableField("crtime")
    @JSONField(format = "yyyy-MM-dd hh:mm:ss")
    private Date crtime;
    @TableField("pushid")
    private Long pushid;
    @TableField("pushname")
    private String pushname;
}
