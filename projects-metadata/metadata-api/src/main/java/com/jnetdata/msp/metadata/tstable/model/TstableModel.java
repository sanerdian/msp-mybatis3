package com.jnetdata.msp.metadata.tstable.model;

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
@TableName("t_tstable")
public class TstableModel implements EntityId<Long> {
    @TableId(value = "id", type = IdType.ID_WORKER)
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long id;

    @TableField("tablename")
    private String tablename;
    @TableField("tableid")
    private Long tableid;
    @TableField("pushname")
    private String pushname;
    @TableField("pushid")
    private Long pushid;
    @JSONField(format = " yyyy-MM-dd hh:mm:ss ")
    @TableField("crtime")
    private Date crtime;
    @TableField("crname")
    private String crname;
    @TableField("xwid")
    private Long xwid;
    @TableField("pushtableid")
    private Long pushtableid;
}
