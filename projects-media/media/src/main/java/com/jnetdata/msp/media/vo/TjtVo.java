package com.jnetdata.msp.media.vo;

import com.alibaba.fastjson.annotation.JSONField;
import com.jnetdata.msp.media.model.JobApi;
import lombok.Data;
import org.thenicesys.fastjson.serializer.ToStringSerializer;
import org.thenicesys.web.vo.PageVo1;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Data
public class TjtVo {
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long id;//新闻id

    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long dateType;//0今天，1本周，2本月，3本年

    private String voteType;// content 则是，新闻内容，其他则是新闻标题

    private int min;

    private int max;

    private Date statrdate;

    private Date enddate;

    private Date statrdatez;

    private Date enddatez;

    private Date statrdatey;

    private Date enddatey;

    private List<Object> listname;

    private List<Object> listvalue;

    private List<Object> listvalue2;

    private List<Map<String,Object>> listmap;

    private Map<String,Object> map;
}
