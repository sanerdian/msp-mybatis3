package com.jnetdata.msp.manage.publish.core.common.vo;

import com.jnetdata.msp.column.model.Channel;
import com.jnetdata.msp.core.model.ChannelPathVO;
import com.jnetdata.msp.core.model.SitePathVO;
import com.jnetdata.msp.manage.publish.core.common.utils.AssertUtil;
import com.jnetdata.msp.manage.site.model.Site;
import com.jnetdata.msp.manage.template.model.Template;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.springframework.boot.autoconfigure.jms.JmsProperties;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author penghe.li
 * @version 1.0
 * @description: TODO
 * @date 2022/8/14 16:59
 */
@ApiModel(value = "发布上下文缓存")
@Accessors(chain = true)
@Getter
@Setter
@ToString
@Component
public class PublishContextCache {

    @ApiModelProperty(value = "模板id为key的模板map")
    private Map<Long, Template> templateMap;

    @ApiModelProperty(value = "站点id为key的map")
    private Map<Long, Site> siteMap;

    @ApiModelProperty(value = "站点id为key的栏目根列表")
    private Map<Long, List<Channel>> siteChannelMap;

    @ApiModelProperty(value = "栏目id为key的map,最好同时是树结构,可直接获取子栏目")
    private Map<Long, Channel> channelIdMap;

    @ApiModelProperty(value = "站点id#栏目唯一标示为key的map,最好同时是树结构,可直接获取子栏目")
    private Map<String, Channel> channelKeyMap;

    @ApiModelProperty(value = "栏目全路径缓存,需要把\\替换为/")
    private Map<Long, ChannelPathVO> channelPath;

    @ApiModelProperty(value = "站点全路径缓存")
    private Map<Long, SitePathVO> websitePath;

    @ApiModelProperty(value = "系统配置路径缓存,发布的物理目录,")
    private PublishBasePathVO publishBasePathVO;

    public void flashPath(){
        Map<Long, ChannelPathVO> pathMap = new HashMap<>();
        channelIdMap.forEach( (chnlId,channel) -> {

            ChannelPathVO channelPathVO = new ChannelPathVO();

            //栏目存放目录
            channelPathVO.setChnlDataPath(channel.getChnlDataPath().replace("\\", "/"));

            //栏目path
            //目前这俩是一样的,以防后续chnlPath不是栏目的全物理路径的情况
            channelPathVO.setChnlPath(channel.getChnlDataPath().replace("\\", "/"));

            //web访问路径
            //目前看上去已经拼接了站点的目录路径,故不在拼接
            //channelPathVO.setWebPath("/" + sitePathMap.get(channel.getSiteId()) + channelPathVO.getChnlPath());
            //处理某些配置错误的情况
            if (channelPathVO.getChnlPath().contains(":")) {
//                log.error("栏目路径配置错误,channelId:{}   chnlDataPath:{}", chnlId
//                        , channelPathVO.getChnlPath());
                //TODO error  栏目路径配置错误,需处理
                channelPathVO.setWebPath(channelPathVO.getChnlPath());
            } else if (!channelPathVO.getChnlPath().startsWith("/")) {
                channelPathVO.setWebPath("/" + channelPathVO.getChnlPath());
            } else {
                channelPathVO.setWebPath(channelPathVO.getChnlPath());
            }

            Site site = readSiteBySiteId(channel.getSiteId());
            String webSite = site.getWebUrl();
            if (webSite != null && webSite.endsWith("/")) {
                webSite = webSite.substring(0, webSite.length() - 1);
            }
            //发布待域名的目录,主要用于文档发布
            channelPathVO.setFullWebPath(webSite + channelPathVO.getWebPath());

            //物理路径
            channelPathVO.setFilePath(publishBasePathVO.getFinalPubDirPath()
                    + channelPathVO.getWebPath());

            Template template = templateMap.get(channel.getChnlOutlineTemp());
            if (template != null) {
                //文件名
                channelPathVO.setFileName(template.getOutputfilename() + "." + template.getTempext());
            } else {
                channelPathVO.setFileName("");
            }

            pathMap.put(chnlId, channelPathVO);
        });
        this.setChannelPath(pathMap);

    }

    public Site readSiteBySiteId(long siteId) {
        Site site = siteMap.get(siteId);
        AssertUtil.isTrue(site != null, String.format("找不到对应的站点,id:%s", siteId));
        return site;
    }
}
