package com.jnetdata.msp.member.groupbusiness.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.jnetdata.msp.core.model.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import org.thenicesys.data.api.EntityId;
import org.thenicesys.fastjson.serializer.ToStringSerializer;

@Data
@TableName("j_group_business")
@ApiModel(value="企业组织实体类", description="企业组织")
public class GroupBusiness extends BaseEntity implements EntityId<Long>  {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键id")
    @JSONField(serializeUsing = org.thenicesys.fastjson.serializer.ToStringSerializer.class)
    @TableId(value = "ID", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "状态")
    @TableField("STATUS")
    private String status;

    @ApiModelProperty(value = "所属机构id")
    @TableField("COMPANYID")
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long companyid;

    @ApiModelProperty(value = "组织名称")
    @TableField("NAME")
    private String name;

    @TableField(exist = false)
    private String andOr;

}
