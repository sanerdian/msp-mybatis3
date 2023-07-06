package com.jnetdata.msp.log.j_log.model;

import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.jnetdata.msp.core.model.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.IdType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import com.baomidou.mybatisplus.annotation.*;
import org.thenicesys.data.api.EntityId;
import org.thenicesys.data.api.Pager;
import org.thenicesys.fastjson.serializer.ToStringSerializer;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;
import java.util.List;

/**
 * <p>
 * 日志
 * </p>
 *
 * @author zyj
 * @since 2021-02-02
 */
@Data
@TableName("j_log")
@ApiModel(value = "JLog对象", description = "日志")
public class JLog extends BaseEntity implements EntityId<Long>  {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "主键id")
    @JSONField(serializeUsing = ToStringSerializer.class)
    @TableId(value = "ID", type = IdType.ASSIGN_ID)
    private Long id;
    @ApiModelProperty(value = "操作标题")
    @TableField("HANDLETITLE")
    private String handletitle;
    @ApiModelProperty(value = "操作类型（增加/修改/删除/浏览/授权/登录、登出/锁定/解锁等）")
    @TableField("HANDLETYPE")
    private String handletype;
    @ApiModelProperty(value = "用户id")
    @TableField("USERID")
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long userid;
    @ApiModelProperty(value = "用户名")
    @TableField("USERNAME")
    private String username;
    @ApiModelProperty(value = "姓名")
    @TableField("TRUENAME")
    private String truename;
    @ApiModelProperty(value = "手机号")
    @TableField("MOBILE")
    private String mobile;
    @ApiModelProperty(value = "ip")
    @TableField("IPSTR")
    private String ipstr;
    @ApiModelProperty(value = "操作对象id")
    @TableField("TARGETID")
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long targetid;
    @ApiModelProperty(value = "操作对象标题")
    @TableField("TARGETTITLE")
    private String targettitle;
    @ApiModelProperty(value = "操作对象类型")
    @TableField("TARGETTYPE")
    private String targettype;
    @ApiModelProperty(value = "源服务器时间")
    @TableField("HANDLEDATE")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date handledate;
    @TableField(exist = false)
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date handledateBT2;
    @ApiModelProperty(value = "写服务器时间")
    @TableField("TARGETDATE")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date targetdate;
    @TableField(exist = false)
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date targetdateBT2;
    @ApiModelProperty(value = "结果")
    @TableField("RESULT")
    private String result;
    @ApiModelProperty(value = "操作后对象信息")
    @TableField("OBJJSON")
    private String objjson;
    @ApiModelProperty(value = "日志摘要")
    @TableField("ZHAIYAO")
    private String zhaiyao;
    @ApiModelProperty(value = "备注")
    @TableField("REMARKS")
    private String remarks;

    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date quitdate;


    @TableField(exist = false)
    private List<Long> pubIds;
    @TableField(exist = false)
    private String andOr;



}
