package com.jnetdata.msp.media.vo;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import org.thenicesys.fastjson.serializer.ToStringSerializer;

import java.util.List;

/**
 * Created by 19912 on 2020/11/11.
 */
@Data
public class CaidanVo {

    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long id;

    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long parentId;

    private String name;

    private List<CaidanVo> children;

    private String followtype;//是否关注，1为已关注

    private String openflag;//是否公开，1公开，其他未公开
}
