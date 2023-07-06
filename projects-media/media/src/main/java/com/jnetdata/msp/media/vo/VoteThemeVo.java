package com.jnetdata.msp.media.vo;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import org.thenicesys.fastjson.serializer.ToStringSerializer;

import java.util.Date;

@Data
public class VoteThemeVo {

    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long id;//id

    private String themename;//投票主题

    private String crUser;//创建人

    @JSONField(format = "yyyy-MM-dd")
    private Date createTime;//创建时间

    @JSONField(format = "yyyy-MM-dd")
    private Date startdate;//创建时间

    @JSONField(format = "yyyy-MM-dd")
    private Date enddate;//创建时间

    private Long pubstatus;//发布状态

    private String photo;

    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long siteid;
}
