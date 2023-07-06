package com.jnetdata.msp.metadata.pushgroup.model;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.jnetdata.msp.core.model.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import org.thenicesys.data.api.EntityId;
import org.thenicesys.fastjson.serializer.ToStringSerializer;

import java.util.Date;

@Data
@TableName("t_pushgroup")
public class PushgroupModel implements EntityId<Long> {
    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty(value = "id", hidden = true)
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long id;

    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date crtime;

    @ApiModelProperty(value = "新闻id")
    @TableField("xwid")
    private Long xwid;

    @ApiModelProperty(value = "推送组名称")
    @TableField("GROUPNAME")
    private String groupname;
    @ApiModelProperty(value = "推送组已选部门")
    @TableField("PUSHTOGROUP")
    private String pushtogroup;
    @ApiModelProperty(value = "推送组已选人员")
    @TableField("PUSHTOUSER")
    private String pushtouser;
    @ApiModelProperty(value = "推送组已选属性（部门属性、人员属性）")
    @TableField("PUSHTOCONDITION")
    private String pushtocondition;

    @TableField("TUIJIANZHE")
    private String tuijianzhe;

    private String pushtorange;
    private String containsondept;

    @TableField("cruser")
    private String cruser;

    private String tablename;//表名
    private Long tableid;//表id
    private String treeid;//树id
    private Long treename; //树名

    private String pushtotype;
}
