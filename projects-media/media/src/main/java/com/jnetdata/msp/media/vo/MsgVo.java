package com.jnetdata.msp.media.vo;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import org.thenicesys.fastjson.serializer.ToStringSerializer;

@Data
public class MsgVo {
    @JSONField(serializeUsing = ToStringSerializer.class)
    Long configId;
    String content;
    String title;
}
