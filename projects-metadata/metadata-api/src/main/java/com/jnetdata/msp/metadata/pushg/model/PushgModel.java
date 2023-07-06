package com.jnetdata.msp.metadata.pushg.model;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import org.thenicesys.data.api.EntityId;
import org.thenicesys.fastjson.serializer.ToStringSerializer;

import java.util.Date;

@Data
@TableName("t_pushg")
@ApiModel(value = "推送保存实体",description = "推送保存实体")
public class PushgModel implements EntityId<Long> {
    @TableId(value = "id",type = IdType.ID_WORKER )
    @ApiModelProperty(value = "主键id",hidden = true)
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long id;
    @TableField("MODIFY_TIME")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date modifyTime;
    @TableField("GROUPNAME")
    private String groupname;
    @TableField("PUSHTOGROUPS")
    private String pushtogroups;
    @TableField("PUSHTOUSERS")
    private String pushtousers;
    @TableField("PUSHTOCONDITIONS")
    private String pushtoconditions;
    @TableField("FLOW_ID")
    private String flowId;
    @TableField("FLOW_USER")
    private String flowUser;
    @TableField("DWBSM")
    private String dwbsm;


    @TableField(exist = false)
    private String andOr;
}
