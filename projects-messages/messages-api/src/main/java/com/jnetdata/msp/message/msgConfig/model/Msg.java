package com.jnetdata.msp.message.msgConfig.model;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.jnetdata.msp.core.model.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.thenicesys.data.api.EntityId;
import org.thenicesys.fastjson.serializer.ToStringSerializer;

import javax.validation.constraints.NotEmpty;


/**
 * 路径配置实体类
 * Created by Admin on 2019/3/11.
 */

@Data
@TableName("SYSCONFIG")
@ApiModel(value = "路径配置实体类",description = "路径配置")
public class Msg extends BaseEntity implements EntityId<Long> {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键id", hidden = true)
    @TableId(value = "CONFIGID", type = IdType.ID_WORKER)
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long id;

    @TableField(value = "CTYPE")
    @NotEmpty(message = "模块标识")
    @ApiModelProperty(value = "模块标识")
    private String mark;

    @TableField(value = "CKEY")
    @NotEmpty(message = "属性名")
    @ApiModelProperty(value = "属性名")
    private String name;

    @TableField(value = "CVALUE")
    @NotEmpty(message = "属性值")
    @ApiModelProperty(value = "属性值")
    private String value;

    @TableField(value = "CDESC")
    @NotEmpty(message = "描述")
    @ApiModelProperty(value = "描述")
    private String cdesc;

    @TableField(value = "SITEID")
    @NotEmpty(message = "站点信息主键")
    @ApiModelProperty(value = "站点信息主键")
    private Long siteId;

    @TableField(value = "MSGTYPE")
    @NotEmpty(message = "消息类型")
    @ApiModelProperty(value = "消息类型")
    @JSONField(serializeUsing = ToStringSerializer.class)
    private String msgType;

    @TableField(value = "IFENCRYPT")
    @NotEmpty(message = "是否加密")
    @ApiModelProperty(value = "是否加密")
    private int ifEncrypt;

    @TableField(value = "MAIL_PORT")
    @NotEmpty(message = "端口号")
    @ApiModelProperty(value = "端口号")
    private int mail_port;

    @TableField(value = "IFSLL")
    @NotEmpty(message = "是否SSL")
    @ApiModelProperty(value = "是否SSL")
    private int ifSLL;

    @TableField(value = "GROUPID")
    @NotEmpty(message = "组织Id")
    @ApiModelProperty(value = "组织Id")
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long groupId;

    @TableField(exist = false)
    @NotEmpty(message = "组织Id")
    @ApiModelProperty(value = "组织Id")
    private String groupName;

    // TODO
/*
    @TableField(exist = false)
    @NotEmpty(message = "组织信息")
    @ApiModelProperty(value = "组织信息")
    private Groupinfo group;
*/
//    邮件账号
    @TableField(value = "MAIL_ACCOUNT")
    @NotEmpty(message = "邮件账号")
    @ApiModelProperty(value = "邮件账号")
    private String mail_account;

//    邮件密码
    @TableField(value = "MAIL_PASSWORD")
    @NotEmpty(message = "邮件密码")
    @ApiModelProperty(value = "邮件账号")
    private String mail_password;

    //    邮件密码
    @TableField(value = "MAIL_SMTP")
    @NotEmpty(message = "邮件smtp")
    @ApiModelProperty(value = "smtp")
    private String mail_smtp;

    @TableField(value = "MAIL_TYPE")
    @NotEmpty(message = "邮件类型")
    @ApiModelProperty(value = "邮件类型")
    private String mail_type;

    @TableField(value = "LOGINNAME")
    @NotEmpty(message = "短信账号用户名")
    @ApiModelProperty(value = "短信账号用户名")
    private String loginName;

    @TableField(value = "PASSWORD")
    @NotEmpty(message = "短信账号密码")
    @ApiModelProperty(value = "短信账号密码")
    private String password;

    @TableField(value = "CORPID")
    @NotEmpty(message = "企业编号")
    @ApiModelProperty(value = "企业编号")
    private String corpId;

    @TableField(value = "accessKeyId")
    @NotEmpty(message = "秘钥id")
    @ApiModelProperty(value = "秘钥id")
    private String accessKeyId;

    @TableField(value = "secretAccessKey")
    @NotEmpty(message = "秘钥")
    @ApiModelProperty(value = "秘钥")
    private String secretAccessKey;

    @TableField(value = "signendpointatureId")
    @NotEmpty(message = "签名id")
    @ApiModelProperty(value = "签名id")
    private String signendpointatureId;

    @TableField(value = "templateId")
    @NotEmpty(message = "模板id")
    @ApiModelProperty(value = "模板id")
    private String templateId;

    @TableField(value = "threepartType")
    @NotEmpty(message = "第三方短信服务")
    @ApiModelProperty(value = "第三方短信服务")
    private String threepartType;

}
