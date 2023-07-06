package com.jnetdata.msp.config.systemmsg.model;

import com.alibaba.fastjson.annotation.JSONField;
import com.jnetdata.msp.core.model.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.thenicesys.data.api.EntityId;
import org.thenicesys.fastjson.serializer.ToStringSerializer;

/**
 * Created by TF on 2019/3/27.
 */

@Data
@ApiModel(value = "数据库信息实体类",description = "数据库信息")
public class DatabaseMsg extends BaseEntity implements EntityId<Long> {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键id", hidden = true)
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long id;

    @ApiModelProperty(value = "数据库类型")
    private String type;

    @ApiModelProperty(value = "数据库版本")
    private String version;

    @ApiModelProperty(value = "数据库转义符")
    private String symbol;

    @ApiModelProperty(value = "数据库驱动版本")
    private String drive;

    @ApiModelProperty(value = "数据库地址")
    private String address;

    @ApiModelProperty(value = "用户名")
    private String userName;

}
