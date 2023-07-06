package com.jnetdata.msp.media.vo;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotations.TableField;
import lombok.Data;
import org.thenicesys.fastjson.serializer.ToStringSerializer;

import javax.persistence.Transient;
import java.util.Date;

@Data
public class xinwenVo {

    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long id;

    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long columnid;

    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long userid;

    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long crnumber;

    private String whereType;//查询条件，如果是collect则查询收藏

    private String title;

    private String logotu;//如果logotu不为空

    private String tuijianzhe;

    private String dianpingshu;

    @JSONField(format="yyyy-MM-dd")
    private Date ptime;

    @Transient
    private Long readcount;//读的数量，用来判断是否已读

    private boolean readbool;

    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long commentcount;//评论数量

    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long docchannelid;

    private Integer commentallowed;

    private Integer commentchecked;

    private String cdntype;

    private String tjtype;

    private String pushsql;

    private String pushtouser;

    /**查询统计部分使用*/

    private String cruser;

    @JSONField(format="yyyy-MM-dd")
    private Date crtime;

    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long evaluatecount;//点赞数量

    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long datetype;//查询时间，0今天，1本周，2本月

    private String evaluatetype;//是否点赞，1点赞否则都是没点

    @Transient
    private Long collectcount;//收藏数量

    @Transient
    private boolean collectbool;

    public boolean isReadbool() {
        return this.readcount!=null&&this.readcount>0?true:false;
    }

    public boolean coolectbool() {
        return this.collectcount!=null&&this.collectcount>0?true:false;
    }

    /*新闻详情需要字段start*/

    private String zhengwen;

    private String summary;

    private String hometitle;

    private String subtitle;

    private Long frozentype;//是否冻结

    /*** 新闻详情需要字段end*/


}
