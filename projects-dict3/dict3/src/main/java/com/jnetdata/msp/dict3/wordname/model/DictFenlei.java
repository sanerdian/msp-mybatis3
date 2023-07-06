package com.jnetdata.msp.dict3.wordname.model;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import org.thenicesys.fastjson.serializer.ToStringSerializer;

import java.math.BigInteger;
import java.util.Date;

/**
 * Created by Administrator on 21/08/09.
 */
@Data
@TableName(value = "dict_fenlei")
public class DictFenlei {
    @JSONField(serializeUsing = ToStringSerializer.class)
    @TableId(value = "fenleiid", type = IdType.INPUT)
//    @TableField(value = "JMETAFANYICIID")
    private BigInteger fenfeiid;

    @TableField(value = "CRUSER")
    private String cruser;

    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(value = "CRTIME")
    private Date crtime;

    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(value = "modify_time")
    private Date modifyTime;

    @TableField(value = "MODIFY_USER")
    private String modifyUser;

    @TableField(value = "fenlei_name")
    private String fenleiName;

    @TableField(value = "module_id")
    private String moduleId;

    @TableField(value = "module_name")
    private String moduleName;

    @TableField(value = "dict_type")
    private String dictType;

    /**
     * -1停用 0不生效 1生效
     */
    @TableField(value = "STATUS")
    private Integer status;
    /**
     * 字符串id的拼接字符串，用逗号隔开
     */
    private String field1;
}
