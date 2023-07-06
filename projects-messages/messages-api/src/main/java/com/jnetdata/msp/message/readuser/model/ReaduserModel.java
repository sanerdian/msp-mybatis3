package com.jnetdata.msp.message.readuser.model;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.thenicesys.data.api.EntityId;
import org.thenicesys.fastjson.serializer.ToStringSerializer;

import javax.validation.constraints.NotEmpty;

@Data
@TableName("t_readuser")
public class ReaduserModel implements EntityId<Long> {
    @ApiModelProperty(value = "主键id", hidden = true)
    @TableId(value = "id", type = IdType.ID_WORKER)
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long id;
    @TableField(value = "userid")
    @NotEmpty(message = "用户id")
    @ApiModelProperty(value = "用户id")
    private Long userid;
    @TableField(value = "readid")
    @NotEmpty(message = "readid")
    @ApiModelProperty(value = "readid")
    private Long readid;
    @TableField(value = "status")
    @NotEmpty(message = "status")
    @ApiModelProperty(value = "status")
    private int status;
}
