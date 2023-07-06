package com.jnetdata.msp.media.vo;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotations.TableField;
import lombok.Data;
import org.thenicesys.fastjson.serializer.ToStringSerializer;

import java.util.Date;

/**
 * Created by 19912 on 2020/8/15.
 */
@Data
public class CommentCenterVo {

    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long id; //新闻id

    private String title;//标题

    private String docchannelName;//栏目名称

    @JSONField(format="yyyy-MM-dd")
    private Date crtime;//发布时间

    private String cruser;//发布人

    private int commentCount;//评论数

    private int tread;//踩

    private int djcounts;//点击数量

    private String tjtype;//推荐类型1推荐，其他不推荐

    private Long frozentype;//新闻是否冻结

    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long siteid;

}
