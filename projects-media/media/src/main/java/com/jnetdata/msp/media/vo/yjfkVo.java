package com.jnetdata.msp.media.vo;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import org.thenicesys.fastjson.serializer.ToStringSerializer;

import java.util.Date;

/**
 * Created by 19912 on 2020/8/15.
 */
@Data
public class yjfkVo {

    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long id; //意见反馈id

    private String yijianneirong;//意见内容

    private String yjphone;//提交人

    @JSONField(format="yyyy-MM-dd")
    private Date createTime;//创建时间

    private String userid;//提交人

    private String replyuserid;//答复人

    @JSONField(format="yyyy-MM-dd")
    private Date replydate;//答复时间

    @JSONField(format="yyyy-MM-dd")
    private Date modifyTime;//回复时间

    @JSONField(format="yyyy-MM-dd")
    private Date timingdate;//定时关闭时间

    private Date[] datetime;//时间

    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long state;//状态

    private int jlcount;//交流数量

    private String photo;//意见反馈中的图片

    private String fen;//得分

    private String opinion;//评价

    //排序规则
    private String ordertype;

}
