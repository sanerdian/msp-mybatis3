package com.jnetdata.msp.metadata.subscribe.model;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.jnetdata.msp.core.model.BaseEntity;
import lombok.Data;
import org.thenicesys.data.api.EntityId;
import org.thenicesys.fastjson.serializer.ToStringSerializer;

import java.util.Date;

@Data
@TableName("subscribeclass")
public class Subscribeinfo  {

    @TableId(value = "id", type = IdType.ID_WORKER)
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long id;

    @TableField("type")
    private int type;

    @TableField("userid")
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long userid;

    @TableField("classid")
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long classid;

    @TableField("subscribetime")
    @JSONField(format = "yyyy-MM-dd hh:mm:ss")
    private Date subscribetime;

    @TableField("classname")
    private String classname;

    @TableField("username")
    private String username;

    @TableField("classtreeid")
    private Long classtreeid;

    @TableField("cruser")
    private String cruser;
}
