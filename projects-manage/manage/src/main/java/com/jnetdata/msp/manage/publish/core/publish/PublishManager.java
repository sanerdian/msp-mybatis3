package com.jnetdata.msp.manage.publish.core.publish;

import com.jnetdata.msp.column.model.Channel;
import com.jnetdata.msp.constants.PublishConstants;
import com.jnetdata.msp.core.model.ChannelPathVO;
import com.jnetdata.msp.core.model.DocPathVO;
import com.jnetdata.msp.core.model.PublishObj;
import com.jnetdata.msp.core.model.SitePathVO;
import com.jnetdata.msp.manage.site.model.Site;
import com.jnetdata.msp.manage.template.model.Template;
import com.jnetdata.msp.manage.publish.core.common.utils.PublishUtil;
import com.jnetdata.msp.manage.publish.core.common.vo.PublishContextCache;
import com.jnetdata.msp.manage.publish.core.common.vo.PublishCountVO;
import com.jnetdata.msp.manage.publish.core.common.vo.PublishVO;
import com.jnetdata.msp.manage.publish.core.system.thread.ExecutorManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.LongAdder;

/**
 * @author penghe.li
 * @version 1.0
 * @description: TODO
 * @date 2022/9/11 11:12
 */
@Service
@Slf4j
public class PublishManager {

    @Resource
    private PublishUtil publishUtil;

    @Resource
    private PublishContextCache publishContextCache;

    @Resource
    private ExecutorManager executorManager;

    @Resource
    private PublishContentManager publishContentManager;

    @Value("${project.publish.document.batchSize:500}")
    int batchSize;


    public void publishSiteIndex(PublishVO publishVO) {

        List<Long> siteIdList = publishVO.getSiteList();

        int size = siteIdList.size();
        Long siteId;
        //必须倒叙,先发布底层栏目
        CountDownLatch countDownLatch = new CountDownLatch(size);
        for (int i = 0; i < size; i++) {
            siteId = siteIdList.get(i);

            SitePathVO sitePathVO = publishContextCache.getWebsitePath().get(siteId);

            File file = new File(sitePathVO.getFilePath());
            if (!file.exists()) {
                file.mkdirs();
            }
            Site site = publishUtil.readSiteBySiteId(siteId);


            PublishObj publishObj = PublishObj.builder()
                    .siteId(siteId)
                    .publishType(PublishObj.PublishType.site)
                    .sitePathVO(sitePathVO)
                    .monitorId(publishVO.getMonitorId())
                    .build();

            Template template = publishContextCache.getTemplateMap().get(Long.valueOf(site.getHomeTemplateId()));
            File siteFile = new File(file, template.getOutputfilename() + "." + template.getTempext());
            if (siteFile.exists()) {
                siteFile.delete();
            }

            executorManager.threadPoolTaskExecutor().submit(() -> {
                publishContentManager.createHtml(template.getTemphtml()
                        , publishObj, siteFile, countDownLatch);
            });
        }
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void publishCancelSiteIndex(PublishVO publishVO) {

        List<Long> siteIdList = publishVO.getSiteList();

        int size = siteIdList.size();
        Long siteId;
        //必须倒叙,先发布底层栏目
        CountDownLatch countDownLatch = new CountDownLatch(size);
        for (int i = 0; i < size; i++) {
            siteId = siteIdList.get(i);

            SitePathVO sitePathVO = publishContextCache.getWebsitePath().get(siteId);

            File file = new File(sitePathVO.getFilePath());
            if (!file.exists()) {
                file.mkdirs();
            }
            Site site = publishUtil.readSiteBySiteId(siteId);


            PublishObj publishObj = PublishObj.builder()
                    .siteId(siteId)
                    .publishType(PublishObj.PublishType.site)
                    .sitePathVO(sitePathVO)
                    .monitorId(publishVO.getMonitorId())
                    .build();

            Template template = publishContextCache.getTemplateMap().get(Long.valueOf(site.getHomeTemplateId()));
            File siteFile = new File(file, template.getOutputfilename() + "." + template.getTempext());
            if (siteFile.exists()) {
                siteFile.renameTo(new File(file, template.getOutputfilename()
                        + "." + template.getTempext() + "_delete"));
            }
        }
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public CountDownLatch publishChannelIndex(PublishVO publishVO) {
        PublishCountVO publishCountVO = publishVO.getPublishCountVO();
        LongAdder channelAllNum = publishCountVO.getChannelAllNum();
        LongAdder channelFinishNum = publishCountVO.getChannelFinishNum();
        LongAdder channelErrorNum = publishCountVO.getChannelErrorNum();

        List<Long> channelIdList = publishVO.getChannelList();

        int size = channelIdList.size();
        Long chnlId;

        //必须倒叙,先发布底层栏目
        channelAllNum.add(size);
        CountDownLatch countDownLatch = new CountDownLatch(size);
        for (int i = size - 1; i >= 0; i--) {
            chnlId = channelIdList.get(i);
            ChannelPathVO channelPathVO = publishContextCache.getChannelPath().get(chnlId);
            File file = new File(channelPathVO.getFilePath());
            if (!file.exists()) {
                file.mkdirs();
            }
            Channel channel = publishUtil.readChannelByChannelId(chnlId);


            PublishObj publishObj = PublishObj.builder()
                    .siteId(channel.getSiteId()).channelId(chnlId)
                    .publishType(PublishObj.PublishType.channel)
                    .channelPathVO(channelPathVO)
                    .monitorId(publishVO.getMonitorId())
                    .build();

            Template template = publishContextCache.getTemplateMap().get(channel.getChnlOutlineTemp());
            File channelFile = new File(file, template.getOutputfilename() + "." + template.getTempext());
            if (channelFile.exists()) {
                channelFile.delete();
            }

            executorManager.threadPoolTaskExecutor().submit(() -> {
                boolean bool = publishContentManager.createHtml(template.getTemphtml()
                        , publishObj, channelFile, countDownLatch);
                if (bool) {
                    channelFinishNum.add(1);
                } else {
                    channelErrorNum.add(1);
                }
            });
        }
        return countDownLatch;
    }

    public void publishCancelChannelIndex(PublishVO publishVO) {
        List<Long> channelIdList = publishVO.getChannelList();

        int size = channelIdList.size();
        Long chnlId;

        //必须倒叙,先发布底层栏目
        CountDownLatch countDownLatch = new CountDownLatch(size);
        for (int i = size - 1; i >= 0; i--) {
            chnlId = channelIdList.get(i);
            ChannelPathVO channelPathVO = publishContextCache.getChannelPath().get(chnlId);
            File file = new File(channelPathVO.getFilePath());
            if (!file.exists()) {
                file.mkdirs();
            }
            Channel channel = publishUtil.readChannelByChannelId(chnlId);


            PublishObj publishObj = PublishObj.builder()
                    .siteId(channel.getSiteId()).channelId(chnlId)
                    .publishType(PublishObj.PublishType.channel)
                    .channelPathVO(channelPathVO)
                    .monitorId(publishVO.getMonitorId())
                    .build();

            Template template = publishContextCache.getTemplateMap().get(channel.getChnlOutlineTemp());
            File channelFile = new File(file, template.getOutputfilename() + "." + template.getTempext());
            if (channelFile.exists()) {
                channelFile.renameTo(new File(file, template.getOutputfilename()
                        + "." + template.getTempext() + "_delete"));
            }
        }
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void publishChannelMetadatas(PublishVO publishVO) {
        PublishCountVO publishCountVO = publishVO.getPublishCountVO();
        LongAdder metadataAllNum = publishCountVO.getMetadataAllNum();
        LongAdder metadataFinishNum = publishCountVO.getMetadataFinishNum();
        LongAdder metadataErrorNum = publishCountVO.getMetadataErrorNum();

        List<Long> channelIdList = publishVO.getChannelList();

        int size = channelIdList.size();

        for (int i = size - 1; i >= 0; i--) {
            Long chnlId = channelIdList.get(i);

            Channel channel = publishUtil.readChannelByChannelId(chnlId);

            ChannelPathVO channelPathVO = publishContextCache.getChannelPath().get(chnlId);

            Template template = publishContextCache.getTemplateMap().get(channel.getChnlDetailTemp());
            String templateStr = template.getTemphtml();
            String ext = template.getTempext();

            PublishObj publishObj = PublishObj.builder()
                    .siteId(channel.getSiteId()).channelId(chnlId)
                    .publishType(PublishObj.PublishType.document)
                    .monitorId(publishVO.getMonitorId())
                    .build();

            List<Map<String, Object>> list = publishContentManager.getMetadatas(publishObj, chnlId);
            int max = list.size();
            metadataAllNum.add(max);
            int step = Math.min(batchSize, max);
            for (int y = 0; y < max; y += step) {
                List<Map<String, Object>> listEach = list.subList(y, Math.min(y + step, max));
                executorManager.threadPoolTaskExecutor().submit(() -> {

                    DocPathVO docPathVO = new DocPathVO();
                    docPathVO.setExt(ext);
                    File file;
                    boolean bool;
                    PublishObj publishDocObj = PublishObj.builder()
                            .siteId(channel.getSiteId()).channelId(chnlId)
                            .publishType(PublishObj.PublishType.document)
                            .docPathVO(docPathVO).channelPathVO(channelPathVO).build();
                    for (Map<String, Object> metaMap : listEach) {
                        try {
                            publishUtil.buildMetadataDocPath(publishDocObj, metaMap);
                            metaMap.put(PublishConstants.PUBLISH_METADATA_FIELD_DOCCHANNELID, chnlId);
                            file = new File(docPathVO.getFilePath());
                            bool = publishContentManager.createMetaDataHtml(templateStr, publishDocObj, file, metaMap);
                            if (bool) {
                                metadataFinishNum.add(1);
                                metaMap.put(PublishConstants.PUBLISH_METADATA_FIELD_DOCPUBURL, docPathVO.getDocPubUrl());
                            } else {
                                metadataErrorNum.add(1);
                            }
                        } catch (Exception e) {
                            metadataErrorNum.add(1);
                            log.error("文档发布出现错误,channelId:{}", chnlId);
                        }
                    }
                    //TODO lph批量更新入库
                });
            }
        }
    }

    public void publishCancelChannelMetadatas(PublishVO publishVO) {
        List<Long> channelIdList = publishVO.getChannelList();

        int size = channelIdList.size();

        //必须倒叙,先发布底层栏目
        LongAdder allTask = new LongAdder();
        LongAdder finishTask = new LongAdder();
        LongAdder failTask = new LongAdder();

        for (int i = size - 1; i >= 0; i--) {
            Long chnlId = channelIdList.get(i);

            Channel channel = publishUtil.readChannelByChannelId(chnlId);

            ChannelPathVO channelPathVO = publishContextCache.getChannelPath().get(chnlId);

            Template template = publishContextCache.getTemplateMap().get(channel.getChnlDetailTemp());
            String ext = template.getTempext();

            PublishObj publishObj = PublishObj.builder()
                    .siteId(channel.getSiteId()).channelId(chnlId)
                    .publishType(PublishObj.PublishType.document)
                    .monitorId(publishVO.getMonitorId())
                    .build();

            List<Map<String, Object>> list = publishContentManager.getMetadatas(publishObj, chnlId);
            int max = list.size();
            int step = Math.min(batchSize, max);
            for (int y = 0; y < max; y += step) {
                List<Map<String, Object>> listEach = list.subList(y, Math.min(y + step, max));
                executorManager.threadPoolTaskExecutor().submit(() -> {
                    DocPathVO docPathVO = new DocPathVO();
                    docPathVO.setExt(ext);
                    File file;
                    PublishObj publishDocObj = PublishObj.builder()
                            .siteId(channel.getSiteId()).channelId(chnlId)
                            .publishType(PublishObj.PublishType.document)
                            .docPathVO(docPathVO).channelPathVO(channelPathVO).build();
                    for (Map<String, Object> metaMap : listEach) {
                        publishUtil.buildMetadataDocPath(publishDocObj, metaMap);
                        metaMap.put(PublishConstants.PUBLISH_METADATA_FIELD_DOCCHANNELID, chnlId);
                        file = new File(docPathVO.getFilePath());
                        if (file.exists()) {
                            file.renameTo(new File(docPathVO.getFilePath() + "_delete"));
                        }
                    }
                    //TODO lph批量撤销发布更新入库
                });
            }
        }
    }
}
