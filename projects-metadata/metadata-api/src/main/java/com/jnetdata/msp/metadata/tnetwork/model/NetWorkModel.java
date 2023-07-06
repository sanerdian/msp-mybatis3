package com.jnetdata.msp.metadata.tnetwork.model;

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
@TableName("t_network")
public class NetWorkModel implements EntityId<Long> {
    @TableId(value = "id", type = IdType.ID_WORKER)
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long id;
    private Long xwid;
    @TableField("organizationid")
    private Long organizationid;
    @TableField("organizationname")
    private String organizationname;
    private Long userid;
    private String username;
    private String namee;
    private String sex;
    private int age;
    private String conditionn;
    private int ageone;
    private String education;
    private String politicsstatus;
    private Long tableid;
    private String tablename;
    private String treename;
    private Long treeid;
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date crtime;
    private String cruser;
    private Long isdisplay;
}
