package com.jnetdata.msp.manage.publish.core.common.vo;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * @author penghe.li
 * @version 1.0
 * @description: TODO
 * @date 2022/8/29 8:53
 */
@ApiModel(value = "站点栏目对象")
@Accessors(chain = true)
@Getter
@Setter
@ToString
public class ChannelSiteVO {
    private Long siteId;
    private Long channelId;
}
