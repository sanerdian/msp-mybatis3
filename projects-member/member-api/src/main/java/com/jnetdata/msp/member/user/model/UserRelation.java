package com.jnetdata.msp.member.user.model;

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

/**
 * @Author: YUEHAO
 * @Date: 2019/12/3
 */
@Data
@TableName("user_relation")
@ApiModel(value = "虚线上报人实体类",description = "虚线上报人表")
public class UserRelation extends BaseEntity implements EntityId<Long> {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "USERRELATIONID", hidden = true)
    @TableId(value = "USERRELATIONID", type = IdType.ID_WORKER)
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long id;

    @ApiModelProperty(value = "用户id")
    @TableField("USERID")
    private Long userId;

    @ApiModelProperty(value = "虚拟上报人id")
    @TableField("VIRLEADERID")
    private Long virLeaderId;
}
