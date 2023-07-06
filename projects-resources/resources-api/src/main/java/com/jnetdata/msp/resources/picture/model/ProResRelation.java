package com.jnetdata.msp.resources.picture.model;

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
 * @Date: 2019/11/29
 */
@Data
@TableName("pro_res_relation")
@ApiModel(description = "项目资源关联表")
public class ProResRelation  extends BaseEntity implements EntityId<Long> {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "id", hidden = true)
    @TableId(value = "ID", type = IdType.ID_WORKER)
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long id;

    @TableField(value = "PROID")
    private Long proid;

    @TableField(value = "RESID")
    private Long resid;


    @Override
    protected Object clone() throws CloneNotSupportedException {

        return super.clone();
    }
}
