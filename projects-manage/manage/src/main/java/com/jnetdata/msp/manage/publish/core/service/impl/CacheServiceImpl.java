package com.jnetdata.msp.manage.publish.core.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.jnetdata.msp.column.mapper.ChannelMapper;
import com.jnetdata.msp.column.model.Channel;
import com.jnetdata.msp.config.config.mapper.ConfigModelMapper;
import com.jnetdata.msp.config.config.model.ConfigModel;
import com.jnetdata.msp.core.model.ChannelPathVO;
import com.jnetdata.msp.core.model.SitePathVO;
import com.jnetdata.msp.manage.site.mapper.SiteMapper;
import com.jnetdata.msp.manage.site.model.Site;
import com.jnetdata.msp.manage.template.mapper.TemplateMapper;
import com.jnetdata.msp.manage.template.model.Template;
import com.jnetdata.msp.manage.publish.core.common.utils.AssertUtil;
import com.jnetdata.msp.manage.publish.core.common.utils.PublishUtil;
import com.jnetdata.msp.manage.publish.core.common.vo.PublishBasePathVO;
import com.jnetdata.msp.manage.publish.core.common.vo.PublishContextCache;
import com.jnetdata.msp.manage.publish.core.service.CacheService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author penghe.li
 * @version 1.0
 * @description: TODO
 * @date 2022/8/31 14:55
 */
@Service
@Slf4j
public class CacheServiceImpl implements CacheService {

    @Resource
    private PublishContextCache publishContextCache;

    @Resource
    private ChannelMapper columnUtilMapper;

    @Resource
    private SiteMapper siteMapper;

    @Resource
    private TemplateMapper templateMapper;

    @Resource
    private ConfigModelMapper configModelMapper;

    @Resource
    private PublishUtil publishUtil;

    @Override
    public void initSite() {
        LambdaQueryWrapper<Site> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Site::getStatus, 0);
        List<Site> siteList = siteMapper.selectList(lambdaQueryWrapper);
        Map<Long, Site> map = siteList.stream().collect(Collectors.toMap(Site::getId
                , v -> v, (k1, k2) -> k1));
        publishContextCache.setSiteMap(map);

        PublishBasePathVO publishBasePathVO = publishContextCache.getPublishBasePathVO();

        //Map<Long, String> pathMap = siteList.stream().collect(Collectors.toMap(Site::getId, Site::getDataPath, (k1, k2) -> k1));
        Map<Long, SitePathVO> pathMap = new HashMap<>();
        SitePathVO sitePathVO;
        Template template;
        String templateId;
        for (Site site : siteList) {
            sitePathVO = new SitePathVO()
                    .setDataPath(site.getDataPath().startsWith("/") ? site.getDataPath()
                            : "/" + site.getDataPath())
                    .setWeburl(site.getWebUrl());
            String fileName="";
            try{
                templateId = site.getHomeTemplateId();
                template = publishContextCache.getTemplateMap().get(Long.parseLong(templateId));
                if (!ObjectUtils.isEmpty(template)) {
                    fileName=template.getOutputfilename() + "." + template.getTempext();
                }
            }catch (Exception e){
                e.printStackTrace();
            }
            sitePathVO.setFileName(fileName);
            //物理路径
            sitePathVO.setFilePath(publishBasePathVO.getFinalPubDirPath()
                    + sitePathVO.getDataPath());
            pathMap.put(site.getId(), sitePathVO);
        }
        publishContextCache.setWebsitePath(pathMap);
    }

    @Override
    public void initChannel() {
        LambdaQueryWrapper<Channel> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Channel::getStatus, 0);
        List<Channel> channelList = columnUtilMapper.selectList(lambdaQueryWrapper);

        Map<Long, List<Channel>> parentMap = channelList.stream().collect(
                Collectors.groupingBy(Channel::getParentId));

        Map<Long, Channel> idMap = new HashMap<>();
        Map<String, Channel> keyMap = new HashMap<>();
        Map<Long, ChannelPathVO> pathMap = new HashMap<>();

        Map<Long, Template> templateMap = publishContextCache.getTemplateMap();
        PublishBasePathVO publishBasePathVO = publishContextCache.getPublishBasePathVO();
        //Map<Long, String> sitePathMap = publishContextCache.getWebsitePath();
        Long chnlId;
        List<Channel> childList;
        Template template;
        for (Channel channel : channelList) {
            chnlId = channel.getId();
            childList = parentMap.get(chnlId);
            if (childList != null) {
                childList = childList.stream().sorted(Comparator.comparing(
                        Channel::getChnlOrder, Comparator.naturalOrder()))
                        .collect(Collectors.toList());
                channel.setChildList(childList);
            }
            idMap.put(channel.getId(), channel);
            keyMap.put(String.format("%s#%s", channel.getSiteId(), channel.getSkuNumber()), channel);

            ChannelPathVO channelPathVO = new ChannelPathVO();
            if (!ObjectUtils.isEmpty(channel.getChnlDataPath())) {
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
                    log.error("栏目路径配置错误,channelId:{}   chnlDataPath:{}", chnlId
                            , channelPathVO.getChnlPath());
                    //TODO error  栏目路径配置错误,需处理
                    channelPathVO.setWebPath(channelPathVO.getChnlPath());
                } else if (!channelPathVO.getChnlPath().startsWith("/")) {
                    channelPathVO.setWebPath("/" + channelPathVO.getChnlPath());
                } else {
                    channelPathVO.setWebPath(channelPathVO.getChnlPath());
                }
            }
            Site site = publishUtil.readSiteBySiteId(channel.getSiteId());
            String webSite = site.getWebUrl();
            if (webSite != null && webSite.endsWith("/")) {
                webSite = webSite.substring(0, webSite.length() - 1);
            }
            //发布待域名的目录,主要用于文档发布
            channelPathVO.setFullWebPath(webSite + channelPathVO.getWebPath());

            //物理路径
            channelPathVO.setFilePath(publishBasePathVO.getFinalPubDirPath()
                    + channelPathVO.getWebPath());

            template = templateMap.get(channel.getChnlOutlineTemp());
            if (template != null) {
                //文件名
                channelPathVO.setFileName(template.getOutputfilename() + "." + template.getTempext());
            } else {
                channelPathVO.setFileName("");
            }

            pathMap.put(chnlId, channelPathVO);
        }
        Map<Long, List<Channel>> siteChannelMap = parentMap.get(0L).stream().collect(
                Collectors.groupingBy(Channel::getSiteId));

        for (List<Channel> tempList : siteChannelMap.values())
            tempList = tempList.stream().sorted(Comparator.comparing(
                    Channel::getChnlOrder, Comparator.naturalOrder()))
                    .collect(Collectors.toList());
        publishContextCache.setSiteChannelMap(siteChannelMap);
        publishContextCache.setChannelIdMap(idMap);
        publishContextCache.setChannelKeyMap(keyMap);
        publishContextCache.setChannelPath(pathMap);

    }

    @Override
    public void initTemplate() {
        LambdaQueryWrapper<Template> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        //目前发现模板状态未使用
        //lambdaQueryWrapper.eq(Template::getStatus, 0);
        lambdaQueryWrapper.eq(Template::getTpltype, 1);
        List<Template> templateList = templateMapper.selectList(lambdaQueryWrapper);
        Map<Long, Template> map = templateList.stream().collect(Collectors.toMap(Template::getId
                , v -> v, (k1, k2) -> k1));
        publishContextCache.setTemplateMap(map);
    }

    @Override
    public void initSysPath() {
        LambdaQueryWrapper<ConfigModel> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(ConfigModel::getCtype, 1);
        List<ConfigModel> list = configModelMapper.selectList(lambdaQueryWrapper);
        Map<String, ConfigModel> map = list.stream().collect(Collectors.toMap(ConfigModel::getMark
                , v -> v, (k1, k2) -> k1));

        ConfigModel dirUploadBase = map.get(ConfigModel.dir_front);
        AssertUtil.notNull(dirUploadBase, "根目录不能为空");

        File rootDirFile = new File(dirUploadBase.getValue());
        if (!rootDirFile.exists() || !rootDirFile.isDirectory()) {
            AssertUtil.notNull(dirUploadBase, "根目录不存在,请检查");
        }

        ConfigModel dirPic = map.get(ConfigModel.dir_pic);
        AssertUtil.notNull(dirPic, "附图目录不能为空");

        //ConfigModel webFront = map.get(ConfigModel.dir_front);

        ConfigModel dirPub = map.get(ConfigModel.dir_pub);
        AssertUtil.notNull(dirPub, "发布目录不能为空");

        ConfigModel dirPreview = map.get(ConfigModel.dir_preview);
        AssertUtil.notNull(dirPreview, "预览目录不能为空");

        ConfigModel dirFile = map.get(ConfigModel.dir_file);
        AssertUtil.notNull(dirFile, "附件目录不能为空");

        PublishBasePathVO publishBasePathVO = new PublishBasePathVO();

        String rootDir = dirUploadBase.getValue().replace("\\", "/");
        rootDir = rootDir.endsWith("/") ? rootDir : rootDir + "/";

        publishBasePathVO.setDirPub(dirPub.getValue());
        publishBasePathVO.setDirWebpic(dirPic.getValue());
        publishBasePathVO.setDirPreview(dirPreview.getValue());
        publishBasePathVO.setDirUpload(dirFile.getValue());
        publishBasePathVO.setUploadDirBase(rootDir);
        publishBasePathVO.setFinalWebpicPath(rootDir + publishBasePathVO.getDirWebpic());
        publishBasePathVO.setFinalPubDirPath(rootDir + publishBasePathVO.getDirPub()  + "/html");
        publishBasePathVO.setFinalPreviewDirPath(rootDir + publishBasePathVO.getDirPreview()  + "/html");
        publishBasePathVO.setFinalWebfilePath(rootDir + publishBasePathVO.getDirUpload());
        publishContextCache.setPublishBasePathVO(publishBasePathVO);
    }
}
