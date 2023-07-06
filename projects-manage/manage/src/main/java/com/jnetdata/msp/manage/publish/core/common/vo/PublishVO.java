package com.jnetdata.msp.manage.publish.core.common.vo;

import com.jnetdata.msp.manage.publish.core.common.groups.ChannelGroup;
import com.jnetdata.msp.manage.publish.core.common.groups.DocumentGroup;
import com.jnetdata.msp.manage.publish.core.common.groups.MetadataGroup;
import com.jnetdata.msp.manage.publish.core.common.groups.WebsiteGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * @author penghe.li
 * @version 1.0
 * @description: TODO
 * @date 2022/8/7 19:08
 */
@ApiModel(value = "发布对象")
@Accessors(chain = true)
@Getter
@Setter
@ToString
public class PublishVO {
    //文档id参数
    @Size(min = 1, groups = {DocumentGroup.class})
    private List<Long> chnlDocIdList;

    //文档id参数
    @Size(min = 1, groups = {MetadataGroup.class})
    private List<Long> metadataIdList;

    //栏目id
    @NotNull(groups = {ChannelGroup.class, MetadataGroup.class, DocumentGroup.class})
    private Long chnlId;

    //站点id
    @NotNull(groups = {WebsiteGroup.class})
    private Long siteId;

    //调用的服务
    @NotBlank
    private String service;

    public PublishVO() {

    }

    @ApiModelProperty(value = "待发布的栏目列表,不缓存")
    //@JsonProperty(access =JsonProperty.Access.READ_ONLY )
    //@JSONField(serialize = true, deserialize = false)
    private List<Long> channelList;

    @ApiModelProperty(value = "站点列表,不缓存")
    private List<Long> siteList;

    @ApiModelProperty(value = "监控id")
    private Long monitorId;

    private PublishCountVO publishCountVO;

}
