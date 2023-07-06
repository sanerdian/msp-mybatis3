package com.jnetdata.msp.log.userlogin.model;

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
import java.util.Date;

/**
 * Created by TF on 2019/3/13.
 */
@Data
@TableName("LOGININFO")
@ApiModel(value = "用户登录日志实体类",description = "用户登录日志")
public class UserLoginLog extends BaseEntity implements EntityId<Long> {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键id", hidden = true)
    @TableId(value = "LOGINID", type = IdType.ID_WORKER)
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long id;

    @TableField(value="LOGINTIME")
    @ApiModelProperty(value="登录时间")
    @JSONField(format = "yyyy-MM-dd  HH:mm:ss")
    private Date loginTime;

    @TableField(value = "LOGINIP")
    @NotEmpty(message = "登录ip")
    @ApiModelProperty(value = "登录ip")
    private String address;

    @TableField(value="LOGOUTTIME")
    @ApiModelProperty(value="退出时间")
    @JSONField(format = "yyyy-MM-dd  HH:mm:ss")
    private Date logoutTime;

    @TableField(value = "TIMES")
    @NotEmpty(message = "登录次数")
    @ApiModelProperty(value = "登录次数")
    private Long times;

    @TableField(value = "CRUSER")
    @NotEmpty(message = "用户名")
    @ApiModelProperty(value = "用户名")
    private String crUser;

    @TableField(value = "CRTIME")
    @NotEmpty(message = "创建时间")
    @JSONField(format = "yyyy-MM-dd  HH:mm:ss")
    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @TableField(exist = false)
    private Date endTime;

    @TableField(exist = false)
    private Date endTime1;
}
