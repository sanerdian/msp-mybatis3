package com.jnetdata.msp.log.usermenu.model;

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
@TableName("t_uesrmenu_log")
@ApiModel(value = "用户菜单日志实体类",description = "用户菜单操作日志")
public class UserMenuLog extends BaseEntity implements EntityId<Long> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键id", hidden = true)
    @TableId(value = "id", type = IdType.ID_WORKER)
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long id;

    @TableField(value = "remark")
    @NotEmpty(message = "备注")
    @ApiModelProperty(value = "备注")
    private String remark;


    @TableField(value = "address")
    @NotEmpty(message = "ip地址")
    @ApiModelProperty(value = "ip地址")
    private String address;

    @TableField(value = "content")
    @NotEmpty(message = "原始消息")
    @ApiModelProperty(value = "原始消息")
    private String content;

    @TableField(value = "CRUSER")
    @NotEmpty(message = "创建人")
    @ApiModelProperty(value = "创建人")
    private String crUser;

    @TableField(exist = false)
    private Date startTIme;

    @TableField(exist = false)
    private Date endTime;
}
