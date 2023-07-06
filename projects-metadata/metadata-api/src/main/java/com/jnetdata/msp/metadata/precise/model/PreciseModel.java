package com.jnetdata.msp.metadata.precise.model;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.thenicesys.data.api.EntityId;
import org.thenicesys.fastjson.serializer.ToStringSerializer;

@Data
@TableName("t_precise")
@ApiModel(value = "精准推送",description = "精准推送")
public class PreciseModel implements EntityId<Long> {
    @TableId(value = "classid",type = IdType.ID_WORKER )
    @ApiModelProperty(value = "主键id",hidden = true)
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long id;

    private Long userid;

    //用户
    private String username;

    //1为部门 0为用户
    private int picname;

    //
    private int status;

    private String contain;

    private int isdisplay;//是否包含子部门 1包含 0不包含

   //推送id
    private Long classid;

}
