package com.jnetdata.msp.media.vo;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import org.thenicesys.fastjson.serializer.ToStringSerializer;

import java.util.Date;

/**
 * Created by 19912 on 2020/8/15.
 * 互动中心，评论点赞，app评论列表实体类，查询和返回实体类
 */
@Data
public class CommentVo {

    public CommentVo(){}

    public CommentVo(Long docid) {
        this.docid = docid;
    }

    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long id; //评论id

    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long userid;//用户id

    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long docid;//新闻id

    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long createBy;//创建人id

    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long commentsid;//评论id，用于根据评论id查询下面的评论

    private String xinwentitle;//新闻名称

    private String usercomment;//评论内容

    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long commentstatus;//评论状态0/1

    private String commentstatusStr;//评论状态 ，正常/已屏蔽

    @JSONField(format="yyyy-MM-dd")
    private Date statusdate;//审核日期

    @JSONField(format="yyyy-MM-dd")
    private Date modifyTime;//修改时间

    private String crUser;//评论用户/点赞用户

    private String crUserheadUrl;//评论用户/点赞用户头像

    private String crTrueName;//评论用户真实名称/点赞用户真实名称

    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long dfdid;//评论用户/点赞用户

    private String dfdTrueName;//评论用户真实名称/点赞用户真实名称

    @JSONField(format="yyyy-MM-dd")
    private Date createTime;//评论时间//点赞时间

    @JSONField(format="yyyy-MM-dd")
    private Date createTime2;//评论结束//点赞时间

    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date createTime3;//评论时间//点赞时间

    private String sex;//性别

    private int djcount;//点击数量

    private int commentcount;//评论数量

    private int anonymousflag; //是否匿名

    //组织名称
    private String organize;

    private String evaluatetype;//是否点赞，1点赞否则都是没点
}
