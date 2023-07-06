package com.jnetdata.msp.media.vo;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import org.thenicesys.fastjson.serializer.ToStringSerializer;

import javax.persistence.Transient;
import java.util.Date;

@Data
public class xinwenPtVo {

    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long id;

    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long xinwenid;


    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long crnumber;

    private String crUser;

    @JSONField(format="yyyy-MM-dd")
    private Date crtime;

    private String title;//新闻标题

    private String commnettitle;//评论标题

    private String logotu;//如果logotu不为空

    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long commentcount;//评论数量

}
