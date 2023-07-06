package com.jnetdata.msp.metadata.push.model;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.thenicesys.data.api.EntityId;
import org.thenicesys.fastjson.serializer.ToStringSerializer;

import java.util.Date;

@Data
@TableName("t_push")
@ApiModel(value = "元数据管理实体类",description = "元数据管理")
public class PushModel implements EntityId<Long> {
    @TableId(value = "id", type = IdType.ID_WORKER)
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long id;

    private String name;

    private Long classid;

    private String classname;

    private int status;
    @ApiModelProperty(value = "创建时间",example = "")
    @JSONField(format = " yyyy-MM-dd hh:mm:ss ")
    private Date crtime;

    private Long pushid;

    private Long tableid;

    private String tablename;

    private String cruser;
}
