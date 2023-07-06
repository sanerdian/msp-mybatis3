package com.jnetdata.msp.media.vo;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import org.thenicesys.fastjson.serializer.ToStringSerializer;

import java.util.Date;

/**
 * Created by 19912 on 2020/8/15.
 */
@Data
public class voteUserVo {

    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long id;

    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long voteid;

    @JSONField(serializeUsing = ToStringSerializer.class)
    private String votethemeid;

    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long loginid;

    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long votecontentid;//投票内容id

    private String votetitle;//投票标题

    private String votecontenttitle;//投票内容标题

    private String crUser;//投票用户

    private Long createBy;

    @JSONField(format="yyyy-MM-dd")
    private Date createTime;//创建时间

    private String sex;//性别

    private String docchannelName;//栏目名称

    //组织名称
    private String organize;

    //用户头像
    private String photo;

}
