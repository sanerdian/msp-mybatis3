package com.jnetdata.msp.manage.publish.core.publish;

import com.jnetdata.msp.column.model.Channel;
import com.jnetdata.msp.column.service.ChannelService;
import com.jnetdata.msp.constants.PublishConstants;
import com.jnetdata.msp.core.model.PublishObj;
import com.jnetdata.msp.docs.document.model.Document;
import com.jnetdata.msp.docs.document.service.DocumentService;
import com.jnetdata.msp.manage.site.model.Site;
import com.jnetdata.msp.metadata.tableinfo.service.TableinfoService;
import com.jnetdata.msp.manage.publish.core.common.utils.NumberUtil;
import com.jnetdata.msp.manage.publish.core.common.utils.PublishUtil;
import com.jnetdata.msp.manage.publish.core.common.utils.TagsUtils;
import com.jnetdata.msp.manage.publish.core.common.vo.PublishContextCache;
import com.jnetdata.msp.manage.publish.explain.common.constant.TagsConstant;
import lombok.extern.slf4j.Slf4j;
import org.beetl.core.ByteWriter;
import org.beetl.core.GroupTemplate;
import org.beetl.core.Template;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.*;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

/**
 * Created by xujian on 2017/11/30.
 */
@Service
@Slf4j
public class PublishContentManager {

    @Resource
    ChannelService columnUtilService;

    @Resource
    DocumentService documentService;

    @Resource
    TableinfoService tableinfoService;

    @Resource
    private GroupTemplate groupTemplate;

    @Resource
    private PublishContextCache publishContextCache;

    @Resource
    private PublishUtil publishUtil;

    public boolean createHtml(String templateStr, PublishObj publishObj, File outputFile
            , CountDownLatch countDownLatch) {
        Template beelTemplate = this.groupTemplate.getTemplate(templateStr);
//        BeetlException b =template.validate();

        BufferedWriter bufferedWriter = null;
        try {
            bufferedWriter = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(outputFile, false), PublishConstants.OUTPUT_CHARSET));
            beelTemplate.binding(TagsUtils.buildMapObj(publishObj));
            beelTemplate.renderTo(bufferedWriter);
            bufferedWriter.flush();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            countDownLatch.countDown();
            if (bufferedWriter != null) {
                try {
                    bufferedWriter.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public boolean createMetaDataHtml(String templateStr, PublishObj publishObj
            , File outputFile, Map<String, Object> metaMap) {
        Template beelTemplate = this.groupTemplate.getTemplate(templateStr);
//        BeetlException b =template.validate();

        BufferedWriter bufferedWriter = null;
        try {
            bufferedWriter = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(outputFile, false), PublishConstants.OUTPUT_CHARSET));
            beelTemplate.binding(TagsUtils.buildMapObj(publishObj));
            beelTemplate.binding(PublishConstants.TAG_SEPERATE + TagsConstant.JNET_METADATA, metaMap);
            beelTemplate.renderTo(bufferedWriter);

            bufferedWriter.flush();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            if (bufferedWriter != null) {
                try {
                    bufferedWriter.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public boolean createTemplateInner(String templateStr, PublishObj publishObj
            , ByteWriter byteWriter) {
        Template beelTemplate = this.groupTemplate.getTemplate(templateStr);
//        BeetlException b =template.validate();
        try {
            beelTemplate.binding(TagsUtils.buildMapObj(publishObj));
            beelTemplate.renderTo(byteWriter);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 获取站点信息（支持缓存）
     *
     * @param publishObj
     * @param siteId
     * @return
     */
    public Site getWebSite(PublishObj publishObj, long siteId) {
        return null;
    }

    /**
     * 根据id获取栏目
     *
     * @param publishObj
     * @param channelId
     * @return
     */
    public Channel getChannel(PublishObj publishObj, long channelId) {
        return null;
    }

    /**
     * 获取子栏目信息
     *
     * @param publishObj
     * @param id
     * @return
     */
    public List<Channel> getChildChannels(PublishObj publishObj, String id) {
        long channelId;
        if (NumberUtil.isId(id)) {
            channelId = Long.parseLong(id);
        } else {
            channelId = publishObj.getChannelId();
        }
        return columnUtilService.selectChildChannel(channelId);
    }

    /**
     * 根据文档id获取文档
     *
     * @param publishObj
     * @param docId
     * @return
     */
    public Document getDocument(PublishObj publishObj, long docId) {
        return documentService.getById(docId);
    }

    /**
     * 获取待发布文档列表
     *
     * @param publishObj
     * @param id
     * @return
     */
    public List<Map<String, Object>> getDocuments(PublishObj publishObj, String id) {
        long channelId;
        if (NumberUtil.isId(id)) {
            channelId = Long.parseLong(id);
        } else {
            channelId = publishObj.getChannelId();
        }
        return documentService.getListPublish(channelId, 0, publishObj);
    }

    /**
     * 根据元数据id获取元数据
     *
     * @param publishObj
     * @param metaDataId
     * @return
     */
    public Map<String, Object> getMetadata(PublishObj publishObj, long metaDataId) {
        return tableinfoService.getOnePublish(metaDataId, publishObj.getChannelId(), publishObj);
    }

    /**
     * 置标里获取待发布文档列表
     * 根据  数量  publishObj.getNum()
     * 栏目id  publishObj.getChannelId()
     *
     * @param publishObj
     * @param id
     * @return
     */
    public List<Map<String, Object>> getMetadatas(PublishObj publishObj, String id) {
        long chnlId = publishUtil.readDocumentsChannelId(publishObj, id);
        Channel channel = publishUtil.readChannelByChannelId(chnlId);
        return tableinfoService.getListPublish(chnlId, channel.getTableId(), publishObj);
    }

    /**
     * 列表里获取文档
     *
     * @param publishObj
     * @param chnlId
     * @return
     */
    public List<Map<String, Object>> getMetadatas(PublishObj publishObj, long chnlId) {
        Channel channel = publishUtil.readChannelByChannelId(chnlId);
        Long tableId = channel.getTableId();
        if (tableId == null) {
            tableId = 0L;
        }
        return tableinfoService.getListPublish(chnlId, tableId, publishObj);
    }

    /**
     * 列表里获取文档
     *
     * @param publishObj
     * @param chnlId
     * @return
     */
    public List<Map<String, Object>> getPubList(PublishObj publishObj, long chnlId) {
        Channel channel = publishUtil.readChannelByChannelId(chnlId);
        Long tableId = channel.getTableId();
        if (tableId == null) {
            tableId = 0L;
        }
        return tableinfoService.getPubList(chnlId, tableId, publishObj);
    }

    /**
     * 列表里获取文档
     *
     * @param publishObj
     * @return
     */
    public List<Map<String, Object>> getPubList(PublishObj publishObj, String chnlIds) {
        Channel channel = publishUtil.readChannelByChannelId(Long.valueOf(chnlIds.split(",")[0]));
        Long tableId = channel.getTableId();
        if (tableId == null) {
            tableId = 0L;
        }
        return tableinfoService.getPubList(chnlIds, tableId, publishObj);
    }


    /**
     * 根据模板名称获取模板
     *
     * @param name
     * @return
     */
    /*public String getCmsTemplate(PublishObj publishObj, String name) {
        if (redisManager.existsHashField(getKey(PUBLISH_TEMPLATE_CACHE, publishObj.getMonitorId()), name)) {
            return redisManager.getH(getKey(PUBLISH_TEMPLATE_CACHE, publishObj.getMonitorId()), name);
        }
        com.zkjw.cms.common.model.template.Template template = this.templateService.selectByTempNameAndSiteId(name, publishObj.getSiteId());
        StringWriter writer = new StringWriter();
        if (template != null) {
            Map<String, Object> attr = TagsUtils.buildMapObj(publishObj);
            Template tp = this.groupTemplate.getTemplate(template.getTempHtml());
            tp.binding(attr);
            tp.renderTo(writer);
        } else {
            log.error("没有找到siteid:{}下的模板【{}】", publishObj.getSiteId(), name);
        }


        try {
            redisManager.saveH(getKey(PUBLISH_TEMPLATE_CACHE, publishObj.getMonitorId()), name, writer.toString(), PUBLISH_CACHE_TIME);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return writer.toString();
        return null;
    }*/

    /**
     * 拼接缓存key
     *
     * @param prefix
     * @param publishObj
     * @return
     */
    private String getKey(final String prefix, PublishObj publishObj) {
        return String.format(prefix, publishObj.getMonitorId());
    }

    /**
     * 发布缓存时间 20分钟
     */
    public final static int PUBLISH_CACHE_TIME = 60 * 20;
    /**
     * 发布---栏目缓存key
     */
    public final static String PUBLISH_CHANNEL_CACHE = "PUBLISH_CHANNEL_CACHE_%s";
    /**
     * 发布--站点缓存key
     */
    public final static String PUBLISH_SITE_CACHE = "PUBLISH_SITE_CACHE_%s";
    /**
     * 发布--文档缓存key
     */
    public final static String PUBLISH_DOCUMENT_CACHE = "PUBLISH_DOCUMENT_CACHE_%s";
    /**
     * 模板缓存
     */
    public final static String PUBLISH_TEMPLATE_CACHE = "PUBLISH_TEMPLATE_CACHE_%s";


}
