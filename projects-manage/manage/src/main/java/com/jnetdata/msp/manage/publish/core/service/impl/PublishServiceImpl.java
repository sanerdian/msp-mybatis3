package com.jnetdata.msp.manage.publish.core.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.jnetdata.msp.column.model.Channel;
import com.jnetdata.msp.column.service.ChannelService;
import com.jnetdata.msp.config.config.service.ConfigModelService;
import com.jnetdata.msp.docs.document.service.DocumentService;
import com.jnetdata.msp.manage.publish.core.common.constant.ChannelPublishEnum;
import com.jnetdata.msp.manage.publish.core.common.constant.DocumentPublishEnum;
import com.jnetdata.msp.manage.publish.core.common.constant.WebsitePublishEnum;
import com.jnetdata.msp.manage.publish.core.common.utils.AssertUtil;
import com.jnetdata.msp.manage.publish.core.common.utils.GenerateIDUtil;
import com.jnetdata.msp.manage.publish.core.common.utils.PublishUtil;
import com.jnetdata.msp.manage.publish.core.common.vo.*;
import com.jnetdata.msp.manage.publish.core.service.ChannelPublishService;
import com.jnetdata.msp.manage.publish.core.service.DocumentPublishService;
import com.jnetdata.msp.manage.publish.core.service.PublishService;
import com.jnetdata.msp.manage.publish.core.service.WebsitePublishService;
import com.jnetdata.msp.manage.publish.core.system.thread.ExecutorManager;
import com.jnetdata.msp.manage.site.model.Site;
import com.jnetdata.msp.manage.site.service.SiteService;
import com.jnetdata.msp.manage.template.model.Template;
import com.jnetdata.msp.manage.template.service.TemplateService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.time.StopWatch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thenicesys.web.JsonResult;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author penghe.li
 * @version 1.0
 * @description: TODO
 * @date 2022/8/7 22:01
 */
@Service
@Slf4j
public class PublishServiceImpl implements PublishService {

    @Resource
    private DocumentService documentService;

    @Resource
    private ChannelService columnUtilService;

    @Resource
    private SiteService siteService;

    @Resource
    private TemplateService templateService;

    @Autowired
    private ConfigModelService configModelService;

    @Resource
    private WebsitePublishService websitePublishService;

    @Resource
    private ChannelPublishService channelPublishService;

    @Resource
    private DocumentPublishService documentPublishService;

    @Resource
    private PublishContextCache publishContextCache;

    @Resource
    private PublishUtil publishUtil;

    @Resource
    private ExecutorManager executorManager;

    /**
     * document文档发布
     *
     * @param publishVO
     * @return
     */
    @Override
    public JsonResult<?> documentPublish(PublishVO publishVO) {
        validateDocumentService(publishVO);

        return null;
    }

    /**
     * 元数据文档发布
     *
     * @param publishVO
     * @return
     */
    @Override
    public JsonResult<?> metadataPublish(PublishVO publishVO) {
        DocumentPublishEnum documentPublishEnum = validateDocumentService(publishVO);

        validateChannelTemplate(publishVO);

        executorManager.taskThreadPoolTaskExecutor().submit(() -> {
            publishVO.setMonitorId(GenerateIDUtil.generateID());

            //buildChannelIdList(publishVO, documentPublishEnum);
        });

        return JsonResult.success();
    }

    /**
     * 栏目发布
     *
     * @param publishVO
     * @return
     */
    @Override
    public JsonResult<?> channelPublish(PublishVO publishVO) {
        ChannelPublishEnum channelPublishEnum = validateChannelService(publishVO);

        validateChannelTemplate(publishVO);

        executorManager.taskThreadPoolTaskExecutor().submit(() -> {
            //buildChannelIdList(publishVO, channelPublishEnum);
            publishVO.setMonitorId(GenerateIDUtil.generateID());

            PublishCountVO publishCountVO = new PublishCountVO();
            publishVO.setPublishCountVO(publishCountVO);

            Channel columnUtil = publishUtil.readChannelByChannelId(publishVO.getChnlId());

            StopWatch stopWatch = new StopWatch();
            stopWatch.start();

            MonitorVO monitor = MonitorVO.builder()
                    .monitorName(String.format("%s-栏目:%s(id:%s)", channelPublishEnum.getDesc()
                            , columnUtil.getName(), publishVO.getChnlId()))
                    .monitorStatus(MonitorVO.MONITORSTATUS.wait.ordinal())
                    .startTime(new Date(stopWatch.getStartTime()))
                    .siteId(publishVO.getSiteId())
                    .build();
            monitor.setId(publishVO.getMonitorId());
            //monitorService.insert(monitor);

            switch (channelPublishEnum) {
                case INDEX:
                    channelPublishService.publishIndex(publishVO);
                    break;
                case INCREMENT:
                    channelPublishService.publishIncrement(publishVO);
                    break;
                case ALL:
                    channelPublishService.publishAll(publishVO);
                    break;
                case CANCEL:
                    channelPublishService.publishCancel(publishVO);
                    break;
                case PREVIEW:
                    channelPublishService.publishPreview(publishVO);
                    break;
                default:
                    break;
            }
            stopWatch.stop();
            MonitorVO updateMonitor = MonitorVO.builder()
                    .endTime(new Date())
                    .totalTime(stopWatch.getTime())
                    .build();
            updateMonitor.setId(monitor.getId());
            //monitorService.updateById(updateMonitor);
            log.info("finish publish, name:{},data:{}", monitor.getMonitorName(), JSONObject.toJSONString(publishVO));

            while ((publishCountVO.getMetadataAllNum().sum() == 0
                    || publishCountVO.getMetadataAllNum().sum() > (
                    publishCountVO.getMetadataFinishNum().sum()
                            + publishCountVO.getChannelErrorNum().sum()))
                    && (publishCountVO.getChannelAllNum().sum() == 0
                    || publishCountVO.getChannelAllNum().sum() > (
                    publishCountVO.getChannelFinishNum().sum()
                            + publishCountVO.getChannelErrorNum().sum()))) {
                log.info("当前发布任务进行中,栏目:{}-{}/{}  文档:{}-{}/{}", publishCountVO.getChannelFinishNum().sum()
                        , publishCountVO.getChannelErrorNum().sum(), publishCountVO.getChannelAllNum().sum()
                        , publishCountVO.getMetadataFinishNum().sum(), publishCountVO.getMetadataErrorNum().sum()
                        , publishCountVO.getMetadataAllNum().sum());
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            log.info("当前发布任务执行完毕,栏目:{}-{}/{}  文档:{}-{}/{}", publishCountVO.getChannelFinishNum().sum()
                    , publishCountVO.getChannelErrorNum().sum(), publishCountVO.getChannelAllNum().sum()
                    , publishCountVO.getMetadataFinishNum().sum(), publishCountVO.getMetadataErrorNum().sum()
                    , publishCountVO.getMetadataAllNum().sum());
        });
        return JsonResult.success();
    }

    /**
     * 站点发布
     *
     * @param publishVO
     * @return
     */
    @Override
    public JsonResult<?> websitePublish(PublishVO publishVO) {
        WebsitePublishEnum websitePublishEnum = validateWebsiteService(publishVO);

        validateWebsiteTemplate(publishVO);

        executorManager.taskThreadPoolTaskExecutor().submit(() -> {
            publishVO.setMonitorId(GenerateIDUtil.generateID());

            //buildSiteChannelIdList(publishVO, websitePublishEnum);

            //TODO 此处后续可改造发送消息队列,实现分布式发布
            switch (websitePublishEnum) {
                case INDEX:
                    websitePublishService.publishIndex(publishVO);
                    break;
                case INCREMENT:
                    websitePublishService.publishIncrement(publishVO);
                    break;
                case ALL:
                    websitePublishService.publishAll(publishVO);
                    break;
                case CANCEL:
                    websitePublishService.publishCancel(publishVO);
                    break;
                default:
                    break;
            }
        });
        return JsonResult.success();
    }

    @Override
    public DocumentPublishEnum validateDocumentService(PublishVO publishVO) {
        String service = publishVO.getService();
        DocumentPublishEnum documentPublishEnum = DocumentPublishEnum.readByValue(service);
        AssertUtil.notNull(documentPublishEnum, "不能识别的服务名");
        return documentPublishEnum;
    }

    @Override
    public ChannelPublishEnum validateChannelService(PublishVO publishVO) {
        String service = publishVO.getService();
        ChannelPublishEnum channelPublishEnum = ChannelPublishEnum.readByValue(service);
        AssertUtil.notNull(channelPublishEnum, "不能识别的服务名");
        return channelPublishEnum;
    }

    @Override
    public WebsitePublishEnum validateWebsiteService(PublishVO publishVO) {
        String service = publishVO.getService();
        WebsitePublishEnum websitePublishEnum = WebsitePublishEnum.readByValue(service);
        AssertUtil.notNull(websitePublishEnum, "不能识别的服务名");
        return websitePublishEnum;
    }

    @Override
    public void validateWebsiteTemplate(PublishVO publishVO) {
        Site site = publishUtil.readSiteBySiteId(publishVO.getSiteId());
        String outLineTemp = site.getHomeTemplateId();

        AssertUtil.isTrue(StringUtils.isNotEmpty(outLineTemp)
                , String.format("站点未配置模板,id:%s", publishVO.getSiteId()));
        //TODO 模板属于热点数据,后续要在模板管理处维护缓存
        Template outLineTemplate = publishUtil.readTemplateByTemplateId(Long.parseLong(outLineTemp));
    }

    @Override
    public void validateChannelTemplate(PublishVO publishVO) {
        Channel columnUtil = publishUtil.readChannelByChannelId(publishVO.getChnlId());
        Long outLineTemp = columnUtil.getChnlOutlineTemp();
        Long detailTemp = columnUtil.getChnlDetailTemp();

        AssertUtil.isTrue(outLineTemp != null && outLineTemp > 0
                , String.format("栏目未配置概览模板,id:%s", publishVO.getChnlId()));

        AssertUtil.isTrue(detailTemp != null && detailTemp > 0
                , String.format("栏目未配置细览模板,id:%s", publishVO.getChnlId()));

        //TODO 模板属于热点数据,后续要在模板管理处维护缓存
        Template outLineTemplate = publishUtil.readTemplateByTemplateId(outLineTemp);

        //TODO 模板属于热点数据,后续要在模板管理处维护缓存
        Template detailTemplate = publishUtil.readTemplateByTemplateId(detailTemp);

    }

    /*private void buildChannelIdList(PublishVO publishVO, ChannelPublishEnum channelPublishEnum) {
        List<Long> chnlIdList = new ArrayList<>();
        switch (channelPublishEnum){
            case INDEX:
        }
        publishUtil.readChildrenChannelId(publishVO.getChnlId(), chnlIdList);

        publishVO.setChannelList(chnlIdList);
    }

    private void buildChannelIdList(PublishVO publishVO, DocumentPublishEnum documentPublishEnum) {
        List<Long> chnlIdList = new ArrayList<>();
        publishUtil.readChildrenChannelId(publishVO.getChnlId(), chnlIdList);
        publishVO.setChannelList(chnlIdList);
    }

    private void buildSiteChannelIdList(PublishVO publishVO, WebsitePublishEnum websitePublishEnum) {
        List<Long> chnlIdList = new ArrayList<>();
        List<ColumnUtil> rootChannelList = publishUtil.readRootChannelsByChannelId(publishVO.getSiteId());

        for (ColumnUtil column : rootChannelList) {
            publishUtil.readChildrenChannelId(column.getId(), chnlIdList);
        }
        publishVO.setChannelList(chnlIdList);
    }*/

    /**
     * 废弃,该方法在当前栏目使用了其他栏目的时候无缓存
     * @param columnUtil
     * @param publishVO
     * @return
     */
    /*private PublishContext buildPublishContext(ColumnUtil columnUtil, PublishVO publishVO) {
        //模板校验
        ChannelContextVO channelContextVO = validateChannelTemplate(columnUtil, true);

        List<ColumnUtil> list = columnUtilService.selectChildChannel(columnUtil.getId());

        Map<Long, ChannelContextVO> contextVOMap = new LinkedHashMap<>();
        contextVOMap.put(columnUtil.getId(), channelContextVO);

        return buildPublishContext(contextVOMap, list, publishVO);
    }*/

    /**
     * TODO xuning
     * 全部栏目的id和path的缓存,从第一级栏目开始到当前栏目结束
     * 如    1000   /lm1/lm2/lm3   在redis读取和维护
     *
     * @return
     */
    private Map<Long, String> readChannelPath() {
        return new HashMap<>();
    }

    /**
     * TODO xuning
     * 全部站点的id和path的缓存,在redis读取和维护
     * 如  1   /zhandian1
     *
     * @return
     */
    private Map<Long, String> readWebsitePath() {
        return new HashMap<>();
    }

    /**
     * 全部站点的id和path的缓存,在redis读取和维护
     * 如  1   /zhandian1
     *
     * @return
     */
    private PublishBasePathVO readSysconfigPath() {
        PublishBasePathVO publishBaseDir = new PublishBasePathVO();

        //TODO 建议后续固定常量变量使用Enum维护
        //上传根地址
        String uploadDirBase = configModelService.getPath("dir_upload_base");
        publishBaseDir.setUploadDirBase(uploadDirBase);

        //前端部署根地址
        String pubDirBase = configModelService.getPath("dir_front");
        publishBaseDir.setPubDirBase(pubDirBase);

        //发布地址
        String dirPub = StringUtils.defaultIfEmpty(configModelService.getPath("dir_pub"), "pub");
        publishBaseDir.setFinalPubDirPath(String.join("/", pubDirBase, dirPub));

        //上传附图地址
        String dirPic = StringUtils.defaultIfEmpty(configModelService.getPath("dir_pic"), "webpic");
        publishBaseDir.setFinalWebpicPath(String.join("/", uploadDirBase, dirPic));

        //上传附件地址
        String dirFile = StringUtils.defaultIfEmpty(configModelService.getPath("dir_file"), "webfile");
        publishBaseDir.setFinalWebpicPath(String.join("/", uploadDirBase, dirFile));

        return publishBaseDir;
    }


    /**
     * 元数据动态查询
     * @return
     */
    /*private List<Map<Long, String>> metadataysconfigPath(Map<Long, ChannelContextVO> contextVOMap) {

    }*/

}
