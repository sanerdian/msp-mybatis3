package com.jnetdata.msp.media.vo;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import org.thenicesys.data.api.Pager;
import org.thenicesys.fastjson.serializer.ToStringSerializer;

import java.util.Date;

@Data
public class voteVo {

    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long id;//id

    @JSONField(serializeUsing = ToStringSerializer.class)
    private String votethemeid;//id

    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long voteid;//投票id

    private Long loginid;//登录人id

    private String title;//标题

    @JSONField(format="yyyy-MM-dd")
    private Date startdate;//开始时间

    @JSONField(format="yyyy-MM-dd")
    private Date enddate;//结束时间

    @JSONField(format="yyyy-MM-dd")
    private Date createTime;//创建时间

    private String remarks;//备注

    private int voteCounts;//投递数量

    private int voteUserCounts;//投票人数

    private String docchannelName;//栏目名称

    private String cruser;//发布人

    private String photo;//图片

    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long state;//状态

    private Date[] datetime;//时间

    private String voteBfb;//投票百分比

    private String votetype;//是否投票，1投了，其余没有

    private String orderby;//count，为按照投票次数排序其他是创建时间

    @JSONField(serializeUsing = ToStringSerializer.class)//投票类型
    private Long limittype;

    @JSONField(serializeUsing = ToStringSerializer.class)//投票数量
    private Long limitcount;

    @JSONField(serializeUsing = ToStringSerializer.class)//投票剩余数量
    private Long limitsycount;

    Pager<voteVo> voteConetentLPager;
}
