package com.jnetdata.msp.metadata.tciteadd.model;

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
@TableName("t_citeadd")
public class TciteaddModel implements EntityId<Long> {
    @TableId(value = "id", type = IdType.ID_WORKER)
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long id;

    @TableField("tablename")
    private String tablename;
    @TableField("tableid")
    private Long tableid;
    @TableField("name")
    private String name;
    @TableField("nameid")
    private Long nameid;
    @TableField("crtime")
    @JSONField(format = " yyyy-MM-dd hh:mm:ss ")
    private Date crtime;
    @TableField("cruser")
    private String cruser;
    @TableField("pushid")
    private Long pushid;
    @TableField("pushname")
    private String pushname;
}
