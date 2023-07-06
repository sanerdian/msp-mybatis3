package com.jnetdata.msp.core.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * Created by xujian on 2017/10/26.
 * 发布对象属性
 */
@Data
@Builder
public class PublishObj {
    /**
     * 发布任务id
     */
    private long monitorId;
    /**
     * 当前站点id
     */
    private long siteId;

    /**
     * 当前栏目id(如果有）
     */
    private long channelId;

    /**
     * 当前文档id(如果有）
     */
    private Long metadataId;

    private DocPathVO docPathVO;

    private ChannelPathVO channelPathVO;

    private SitePathVO sitePathVO;

    @ApiModelProperty(value = "可发布的文档的状态")
    private String pubStatus;

    @ApiModelProperty(value = "获取文档的数量")
    private Integer num;

    private Integer from;

    private String order;

    @ApiModelProperty(value = "当前发布类型")
    private PublishType publishType;

    public enum PublishType {
        document,
        channel,
        site
    }
}
