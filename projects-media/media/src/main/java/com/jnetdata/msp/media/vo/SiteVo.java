package com.jnetdata.msp.media.vo;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import org.thenicesys.fastjson.serializer.ToStringSerializer;

import java.util.List;

/**
 * Created by 19912 on 2020/11/9.
 */
@Data
public class SiteVo {

    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long id;

    private String name;

    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long parentid;

    private List<SiteVo> sites;

}
