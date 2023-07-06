package com.jnetdata.msp.base.vo;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import org.thenicesys.data.api.EntityId;
import org.thenicesys.fastjson.serializer.ToStringSerializer;

import java.io.Serializable;

@Data
public class EntityIdVo<IdType  extends Serializable> implements EntityId<IdType> {

    @JSONField(serializeUsing = ToStringSerializer.class)
    private IdType id;

    public EntityIdVo(IdType id) {
        this.id = id;
    }

}
