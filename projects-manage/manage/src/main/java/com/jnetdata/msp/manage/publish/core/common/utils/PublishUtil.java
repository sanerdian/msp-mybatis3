package com.jnetdata.msp.manage.publish.core.common.utils;

import com.jnetdata.msp.column.model.Channel;
import com.jnetdata.msp.constants.PublishConstants;
import com.jnetdata.msp.core.model.ChannelPathVO;
import com.jnetdata.msp.core.model.DocPathVO;
import com.jnetdata.msp.core.model.PublishObj;
import com.jnetdata.msp.core.model.SitePathVO;
import com.jnetdata.msp.manage.site.model.Site;
import com.jnetdata.msp.manage.template.model.Template;
import com.jnetdata.msp.manage.publish.core.common.constant.BelongPublishEnum;
import com.jnetdata.msp.manage.publish.core.common.vo.ChannelSiteVO;
import com.jnetdata.msp.manage.publish.core.common.vo.PublishContextCache;
import com.jnetdata.msp.manage.publish.core.common.vo.PublishVO;
import com.jnetdata.msp.manage.publish.explain.impl.beetl.tags.attr.ChannelTagAttr;
import com.jnetdata.msp.manage.publish.explain.impl.beetl.tags.attr.ChannelsTagAttr;
import com.jnetdata.msp.manage.publish.explain.impl.beetl.tags.attr.SiteTagAttr;
import lombok.extern.slf4j.Slf4j;
import oracle.sql.CLOB;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author penghe.li
 * @version 1.0
 * @description: TODO
 * @date 2022/8/28 13:47
 */
@Slf4j
@Component
public class PublishUtil {
    private static ThreadLocal<SimpleDateFormat> THREAD_LOCAL = new ThreadLocal<>();

    @Resource
    PublishContextCache publishContextCache;

    /**
     * 文档获取当前的栏目id
     *
     * @param publishObj
     * @param id
     * @return
     */
    public Long readDocumentsChannelId(PublishObj publishObj, String id) {
        long channelId;
        if (BelongPublishEnum.OWNER.getValue().equalsIgnoreCase(id)) {
            channelId = publishObj.getChannelId();
        } else if (BelongPublishEnum.PARENT.getValue().equalsIgnoreCase(id)) {
            channelId = readParentChannelByChannelId(publishObj.getChannelId()).getId();
        } else if (id.toLowerCase().startsWith(BelongPublishEnum.CHANNELID.getValue())) {
            channelId = Long.parseLong(id.toLowerCase().replace(BelongPublishEnum.CHANNELID.getValue()
                    , ""));
        } else {
            //此时是  栏目唯一标示
            channelId = readChannelByChannelKey(publishObj.getSiteId(), id).getId();
        }
        return channelId;
    }

    /**
     * channels 获取当前栏目
     *
     * @param publishObj
     * @param channelsTagAttr
     * @return
     */
    public ChannelSiteVO readChannelSiteByAttr(PublishObj publishObj, ChannelsTagAttr channelsTagAttr) {
        ChannelSiteVO channelSiteVO = new ChannelSiteVO();
        String id = channelsTagAttr.getId();
        Long channelId;
        long siteId = publishObj.getSiteId();
        if (BelongPublishEnum.OWNER.getValue().equalsIgnoreCase(id)) {
            channelId = publishObj.getChannelId();
        } else if (BelongPublishEnum.PARENT.getValue().equalsIgnoreCase(id)) {
            channelId = readParentChannelByChannelId(publishObj.getChannelId()).getId();
        } else if (BelongPublishEnum.SITE.getValue().equalsIgnoreCase(id)) {
            //此时需要配合站点id
            channelId = 0L;
        } else if (id.toLowerCase().startsWith(BelongPublishEnum.CHANNELID.getValue())) {
            channelId = Long.parseLong(id.toLowerCase().replace(BelongPublishEnum.CHANNELID.getValue()
                    , ""));
        } else if (id.toLowerCase().startsWith(BelongPublishEnum.SITEID.getValue())) {
            channelId = 0L;
            siteId = Long.parseLong(id.toLowerCase().replace(BelongPublishEnum.SITEID.getValue()
                    , ""));
        } else {
            //此时是  栏目唯一标示
            channelId = readChannelByChannelKey(publishObj.getSiteId(), id).getId();
        }
        //TODO 尚未处理定位到子栏目问题  channelsTagAttr.getChildindex()
        return channelSiteVO.setSiteId(siteId).setChannelId(channelId);
    }

    /**
     * channels 获取子栏目列表
     *
     * @param publishObj
     * @param channelId
     * @param channelsTagAttr
     * @return
     */
    public List<Channel> readChildChannelByAttr(PublishObj publishObj, Long channelId
            , ChannelsTagAttr channelsTagAttr) {
        ChannelSiteVO channelSiteVO = readChannelSiteByAttr(publishObj, channelsTagAttr);
        if (channelId != null && channelId > 0) {
            Channel columnUtil = readChannelByChannelId(channelId);
            return columnUtil.getChildList();
        } else if (channelSiteVO.getChannelId() != null && channelSiteVO.getChannelId() > 0) {
            Channel columnUtil = readChannelByChannelId(channelSiteVO.getChannelId());
            return columnUtil.getChildList();
        } else {
            return readRootChannelsBySiteId(channelSiteVO.getSiteId());
        }
    }

    /**
     * channels 获取栏目
     *
     * @param publishObj
     * @param channelTagAttr
     * @return
     */
    public Channel readChannelByAttr(PublishObj publishObj, ChannelTagAttr channelTagAttr) {
        String id = channelTagAttr.getId();
        Long channelId;
        if (BelongPublishEnum.OWNER.getValue().equalsIgnoreCase(id)) {
            channelId = publishObj.getChannelId();
        } else if (BelongPublishEnum.PARENT.getValue().equalsIgnoreCase(id)) {
            channelId = readParentChannelByChannelId(publishObj.getChannelId()).getId();
        } else if (BelongPublishEnum.CHILD.getValue().equalsIgnoreCase(id)) {
            channelId = readChildrenChannelByChannelId(publishObj.getChannelId(),channelTagAttr.getChildindex()).getId();
        } else if (id.toLowerCase().startsWith(BelongPublishEnum.CHANNELID.getValue())) {
            channelId = Long.parseLong(id.toLowerCase().replace(BelongPublishEnum.CHANNELID.getValue()
                    , ""));
        } else {
            //此时是  栏目唯一标示
            channelId = readChannelByChannelKey(publishObj.getSiteId(), id).getId();
        }
        //TODO 尚未处理定位到子栏目问题  channelsTagAttr.getChildindex()
        return readChannelByChannelId(channelId);
    }

    /**
     * channels 获取栏目
     *
     * @param publishObj
     * @param siteTagAttr
     * @return
     */
    public Site readSiteByAttr(PublishObj publishObj, SiteTagAttr siteTagAttr) {
        String id = siteTagAttr.getId();
        long siteId;
        if (BelongPublishEnum.OWNER.getValue().equalsIgnoreCase(id)) {
            siteId = publishObj.getSiteId();
        } else {
            //此时是  站点id
            siteId = Long.parseLong(id);
        }
        return readSiteBySiteId(siteId);
    }

    /**
     * 文档字段发布截取
     *
     * @param str
     * @param length
     * @param truncatedext
     * @return
     */
    public String trancateString(String str, int length, String truncatedext) {
        if (str == null) {
            return "";
        } else if (str.length() * 2 <= length) {
            return str;
        } else {
            int len = str.length();
            int lenByte = 0;
            if (truncatedext == null) {
                truncatedext = "...";
            }
            for (int i = 0; i < len; i++) {
                if (str.charAt(i) > 255) {
                    lenByte += 2;
                } else {
                    ++lenByte;
                }
                if (lenByte >= length) {
                    if (lenByte == length) {
                        return str.substring(0, i + 1);
                    } else {
                        return str.substring(0, i) + truncatedext;
                    }
                }
            }
            return str;
        }
    }

    /**
     * 文档字段解析
     *
     * @param obj
     * @param sdf
     * @return
     */
    public String readFieldValue(Object obj, String sdf) {
        if (obj != null) {
            if (sdf != null) {
                if (obj instanceof String) {
                    return (String) obj;
                } else if (obj instanceof Date) {
                    SimpleDateFormat simpleDateFormat = THREAD_LOCAL.get();
                    if (simpleDateFormat == null) {
                        simpleDateFormat = new SimpleDateFormat(sdf);
                        THREAD_LOCAL.set(simpleDateFormat);
                    }
                    return simpleDateFormat.format(obj);
                }else if (obj instanceof CLOB){
                    try {
                        return ClobToString((CLOB)obj);
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                else {
                    return String.valueOf(obj);
                }
            }
        }
        return "";
    }

    public static String ClobToString(CLOB clob) throws SQLException, IOException {
        String reString = "";
        Reader is = clob.getCharacterStream();
        BufferedReader br = new BufferedReader(is);
        String s = br.readLine();
        StringBuffer sb = new StringBuffer();
        while (s != null) {
            sb.append(s);
            s = br.readLine();
        }
        reString = sb.toString();
        if(br!=null){
            br.close();
        }
        if(is!=null){
            is.close();
        }
        return reString;
    }

    /**
     * 获取栏目id
     *
     * @param channelId
     * @return
     */
    public Channel readParentChannelByChannelId(long channelId) {
        Channel columnUtil = publishContextCache.getChannelIdMap().get(channelId);
        AssertUtil.isTrue(columnUtil != null, String.format("找不到对应的栏目,id:%s", channelId));
        Channel parent = publishContextCache.getChannelIdMap().get(columnUtil.getParentId());
        AssertUtil.isTrue(parent != null, String.format("找不到对应的栏目,id:%s", channelId));
        return parent;
    }

    /**
     * 获取栏目id
     *
     * @param channelId
     * @return
     */
    public List<Channel> readRoute(long channelId) {
        List<Channel> list = new ArrayList<>();
        readRoute(channelId,list);
        Collections.reverse(list);
        return list;
    }


    public void readRoute(long channelId,List<Channel> list) {
        Channel columnUtil = publishContextCache.getChannelIdMap().get(channelId);
        AssertUtil.isTrue(columnUtil != null, String.format("找不到对应的栏目,id:%s", channelId));
        list.add(columnUtil);
        if(columnUtil.getParentId()!=null && !columnUtil.getParentId().equals(0L)){
            readRoute(columnUtil.getParentId(), list);
        }
    }

    /**
     * 获取父栏目id
     *
     * @param channelId
     * @return
     */
    public Channel readChannelByChannelId(long channelId) {
        Channel columnUtil = publishContextCache.getChannelIdMap().get(channelId);
        AssertUtil.isTrue(columnUtil != null, String.format("找不到对应的栏目,id:%s", channelId));
        return columnUtil;
    }

    /**
     * 获取栏目通过栏目唯一标示和站点id
     *
     * @param siteId     站点id
     * @param channelKey 栏目唯一标示
     * @return
     */
    public Channel readChannelByChannelKey(long siteId, String channelKey) {
        Channel columnUtil = publishContextCache.getChannelKeyMap().get(String.format("%s#%s", siteId, channelKey));
        AssertUtil.isTrue(columnUtil != null, String.format("找不到对应的栏目,key:%s", channelKey));
        return columnUtil;
    }

    /**
     * 获取站点根栏目列表
     *
     * @param siteId
     * @return
     */
    public List<Channel> readRootChannelsBySiteId(long siteId) {
        return publishContextCache.getSiteChannelMap().get(siteId);
    }

    /**
     * 获取站点根栏目id
     *
     * @param siteId
     * @return
     */
    public List<Long> readChannelIdBySiteId(long siteId) {
        return publishContextCache.getSiteChannelMap().get(siteId).stream()
                .map(Channel::getId).collect(Collectors.toList());
    }

    /**
     * 获取父栏目id
     *
     * @param siteId
     * @return
     */
    public Site readSiteBySiteId(long siteId) {
        Site site = publishContextCache.getSiteMap().get(siteId);
        AssertUtil.isTrue(site != null, String.format("找不到对应的站点,id:%s", siteId));
        return site;
    }

    /**
     * 获取模板
     *
     * @param templateId
     * @return
     */
    public Template readTemplateByTemplateId(long templateId) {
        Template template = publishContextCache.getTemplateMap().get(templateId);
        AssertUtil.isTrue(template != null, String.format("找不到对应的模板点,id:%s", templateId));
        return template;
    }

    /**
     * 获取栏目的全部子栏目
     *
     * @param publishVO
     * @return
     */
    public void readChildrenChannelById(PublishVO publishVO, List<Long> list) {
        if (publishVO.getChnlId() != null) {
            readChildrenChannelByChannelId(publishVO.getChnlId(), list);
        } else {
            readChildrenChannelBySiteId(publishVO.getSiteId(), list);
        }
    }

    /**
     * 获取父栏目id
     *
     * @param siteId
     * @return
     */
    public List<Long> readChildrenChannelBySiteId(long siteId, List<Long> list) {
        List<Channel> chnlList = readRootChannelsBySiteId(siteId);
        for (int i = 0, size = chnlList.size(); i < size; i++) {
            readChildChannel(chnlList.get(i), list);
        }
        return list;
    }

    /**
     * 获取栏目的全部子栏目
     *
     * @param channelId
     * @return
     */
    public void readChildrenChannelByChannelId(long channelId, List<Long> list) {
        Channel columnUtil = readChannelByChannelId(channelId);
        readChildChannel(columnUtil, list);
    }

    public Channel readChildrenChannelByChannelId(long channelId, int num) {
        Channel columnUtil = readChannelByChannelId(channelId);
        return readChildChannel(columnUtil, num);
    }

    public void readChildChannel(Channel columnUtil, List<Long> list) {
        if (columnUtil != null) {
            list.add(columnUtil.getId());
            List<Channel> childList = columnUtil.getChildList();
            if (childList != null) {
                //拆分成2次,以保证层级高的栏目在上方
                Long templateId;
                Long detailTemplateId;
                for (Channel tempChannel : childList) {
                    templateId = tempChannel.getChnlOutlineTemp();
                    detailTemplateId = tempChannel.getChnlDetailTemp();
                    if (templateId != null && templateId > 0 && detailTemplateId != null && detailTemplateId > 0
                            && publishContextCache.getTemplateMap().containsKey(templateId)
                            && publishContextCache.getTemplateMap().containsKey(detailTemplateId)) {
                        readChildChannel(tempChannel, list);
                    } else {
                        log.warn("栏目 id:{} 未配置模板或模板不存在跳过", tempChannel.getId());
                    }
                }
            }
        }
    }

    public Channel readChildChannel(Channel columnUtil, int num) {
        List<Channel> childList = columnUtil.getChildList();
        if (childList != null) {
            List<Channel> collect = childList.stream().filter(m -> m.getChnlOutlineTemp() != null
                    && m.getChnlOutlineTemp() > 0
                    && m.getChnlDetailTemp() != null
                    && m.getChnlDetailTemp() > 0
                    && publishContextCache.getTemplateMap().containsKey(m.getChnlOutlineTemp())
                    && publishContextCache.getTemplateMap().containsKey(m.getChnlDetailTemp())
            ).collect(Collectors.toList());
            if (collect.size() > num) {
                return collect.get(num);
            } else {
                log.warn("指定子栏目不存在或未配置模板");
                return null;
            }
        }
        return null;
    }

    /**
     * 获取栏目发布相对路径
     *
     * @param obj
     * @param targetCannel
     * @return
     */
    public String buildChannelPubUrl(PublishObj obj, long targetCannel, boolean isAbs) {
        String ownerPath;
        String targetPath;
        ChannelPathVO targetChannelPath;
        ChannelPathVO ownerChannelPath;
        long channelId = obj.getChannelId();
        switch (obj.getPublishType()) {
            case document:
                ownerChannelPath = obj.getChannelPathVO();
                if (channelId == targetCannel) {
                    return "../../" + ownerChannelPath.getFileName();
                } else {
                    ownerPath = obj.getDocPathVO().getDocWebDir();
                    targetChannelPath = publishContextCache.getChannelPath().get(targetCannel);
                    targetPath = targetChannelPath.getWebPath();
                    return buildRelativeUrl(ownerPath, targetPath, targetChannelPath.getFileName());
                }
            case channel:
                ownerChannelPath = obj.getChannelPathVO();
                if (channelId == targetCannel) {
                    return "./" + ownerChannelPath.getFileName();
                } else {
                    ownerPath = ownerChannelPath.getWebPath();
                    targetChannelPath = publishContextCache.getChannelPath().get(targetCannel);
                    targetPath = targetChannelPath.getWebPath();
                    return buildRelativeUrl(ownerPath, targetPath, targetChannelPath.getFileName());
                }
            case site:
                ownerPath = publishContextCache.getWebsitePath().get(obj.getSiteId()).getDataPath();
                targetChannelPath = publishContextCache.getChannelPath().get(targetCannel);
                targetPath = targetChannelPath.getWebPath();
                return buildRelativeUrl(ownerPath, targetPath, targetChannelPath.getFileName());
            default:
                return "";
        }
    }

    /**
     * 获取站点发布相对路径
     *
     * @param obj
     * @param targetSiteId
     * @return
     */
    public String buildSitePubUrl(PublishObj obj, long targetSiteId, boolean isAbs) {
        String ownerPath;
        String targetPath;
        long siteId = obj.getSiteId();
        SitePathVO ownerSitePath;
        SitePathVO targetSitePath;
        switch (obj.getPublishType()) {
            case document:
                ownerPath = obj.getDocPathVO().getDocWebDir();
                targetSitePath = publishContextCache.getWebsitePath().get(targetSiteId);
                targetPath = targetSitePath.getDataPath();
                return buildRelativeUrl(ownerPath, targetPath, targetSitePath.getFileName());
            case channel:
                ChannelPathVO ownerChannelPath = obj.getChannelPathVO();
                ownerPath = ownerChannelPath.getWebPath();
                targetSitePath = publishContextCache.getWebsitePath().get(targetSiteId);
                targetPath = targetSitePath.getDataPath();
                return buildRelativeUrl(ownerPath, targetPath, targetSitePath.getFileName());
            case site:
                ownerSitePath = publishContextCache.getWebsitePath().get(siteId);
                if (siteId == targetSiteId) {
                    return "./" + ownerSitePath.getFileName();
                } else {
                    ownerPath = ownerSitePath.getDataPath();
                    targetSitePath = publishContextCache.getWebsitePath().get(targetSiteId);
                    targetPath = targetSitePath.getDataPath();
                    return buildRelativeUrl(ownerPath, targetPath, targetSitePath.getFileName());
                }
            default:
                return "";
        }
    }

    /**
     * 获取文档发布地址
     * TODO lph
     *
     * @param publishObj
     * @param map
     * @return
     */
    public String buildMetadataPubUrl(PublishObj publishObj, Map<String, Object> map, boolean isAbs) {
        Long targetChannelId = (Long) map.get(PublishConstants.PUBLISH_METADATA_FIELD_DOCCHANNELID);
        DocPathVO docPathVO = publishObj.getDocPathVO();
        String targetPath;
        String ownerPath;
        DocPathVO targetDocPathVO;
        long ownerChannelId = publishObj.getChannelId();
        switch (publishObj.getPublishType()) {
            case document:
                if (ownerChannelId == targetChannelId) {
                    return "./" + docPathVO.getDocFileName();
                } else {
                    ownerPath = publishObj.getDocPathVO().getDocPubUrl();
                    targetDocPathVO = buildMetadataDocPath(targetChannelId, map);
                    targetPath = targetDocPathVO.getDocPubUrl();
                    return buildRelativeUrl(ownerPath, targetPath);
                }
            case channel:
                targetDocPathVO = buildMetadataDocPath(targetChannelId, map);
                targetPath = targetDocPathVO.getDocWebDir();
                if (ownerChannelId == targetChannelId) {
                    return "." + targetDocPathVO.getDocChannelUrl();
                } else {
                    ChannelPathVO ownerChannelPath = publishObj.getChannelPathVO();
                    ownerPath = ownerChannelPath.getWebPath();
                    return buildRelativeUrl(ownerPath, targetPath, targetDocPathVO.getDocFileName());
                }
            case site:
                ownerPath = publishContextCache.getWebsitePath().get(publishObj.getSiteId()).getDataPath();
                targetDocPathVO = buildMetadataDocPath(targetChannelId, map);
                targetPath = targetDocPathVO.getDocWebDir();
                return buildRelativeUrl(ownerPath, targetPath, targetDocPathVO.getDocFileName());
            default:
                return "";
        }
    }

    public String readChannelMetadataDir(Date date, Long docId) {
        AssertUtil.notNull(date, "文档创建日期为空,id:" + docId);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return "/" + calendar.get(Calendar.YEAR)
                + (String.format("%02d", calendar.get(Calendar.MONTH) + 1)) + "/"
                + String.format("%02d", calendar.get(Calendar.DAY_OF_MONTH));
    }

    public String readChannelMetadataFileName(Long docId, String ext) {
        return docId + "." + ext;
    }

    /**
     * 获取文档发布地址
     * TODO lph
     *
     * @param publishObj
     * @param map
     * @return
     */
    public void buildMetadataDocPath(PublishObj publishObj, Map<String, Object> map) {

        ChannelPathVO channelPathVO = publishObj.getChannelPathVO();
        DocPathVO docPathVO = publishObj.getDocPathVO();

        buildDocPathVO(docPathVO, map, channelPathVO);
    }

    private void buildDocPathVO(DocPathVO docPathVO, Map<String, Object> map, ChannelPathVO channelPathVO) {
        Date date = (Date) map.get(PublishConstants.PUBLISH_METADATA_FIELD_CRTIME);
        Long docId = (Long) map.get(PublishConstants.PUBLISH_METADATA_FIELD_METAID);
        String dir = readChannelMetadataDir(date, docId);

        String docDir = channelPathVO.getFilePath() + dir;
        String fullWebPath = channelPathVO.getFullWebPath() + dir;
        String docWebPath = channelPathVO.getWebPath() + dir;
        File docDirFile = new File(docDir);
        if (!docDirFile.exists()) {
            docDirFile.mkdirs();
        }
        String docFileName = readChannelMetadataFileName(docId, docPathVO.getExt());
        docPathVO.setDocPubUrl(fullWebPath + "/" + docFileName)
                .setDocWebDir(docWebPath)
                .setDocChannelUrl(dir + "/" + docFileName)
                .setDocFileName(docFileName)
                .setFilePath(docDir + "/" + docFileName);
    }

    public DocPathVO buildMetadataDocPath(long channelId, Map<String, Object> map) {

        Channel channel = publishContextCache.getChannelIdMap().get(channelId);
        ChannelPathVO channelPathVO = publishContextCache.getChannelPath().get(channelId);

        Template template = publishContextCache.getTemplateMap().get(channel.getChnlDetailTemp());

        DocPathVO docPathVO = new DocPathVO();

        AssertUtil.isTrue(template != null, String.format("找不到模板tempId:%s", channel.getChnlDetailTemp()));

        docPathVO.setExt(template.getTempext());

        buildDocPathVO(docPathVO, map, channelPathVO);
        return docPathVO;
    }

    /**
     * 计算相对路径
     *
     * @param ownerPath
     * @param targetPath
     * @param fileName
     * @return
     */
    public String buildRelativeUrl(String ownerPath, String targetPath, String fileName) {
        try {
            Path pathBase = Paths.get(ownerPath);
            Path pathAbsolute = Paths.get(targetPath);
            Path pathRelative = pathBase.relativize(pathAbsolute);
            return pathRelative.toString().replace("\\", "/") + "/" + fileName;
        } catch (Exception e) {
            log.error("路径错误,ownerPath:{}  targetPath:{}", ownerPath, targetPath);
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 计算相对路径
     *
     * @param ownerPath
     * @param targetPath
     * @return
     */
    public String buildRelativeUrl(String ownerPath, String targetPath) {
        Path pathBase = Paths.get(ownerPath);
        Path pathAbsolute = Paths.get(targetPath);
        Path pathRelative = pathBase.relativize(pathAbsolute);
        return pathRelative.toString().replace("\\", "/");
    }


    /**
     * 获取模板
     *
     * @param siteId
     * @param templateName
     * @return
     */
    public String readTemplate(Long siteId, String templateName) {
        Optional<Template> opt = publishContextCache.getTemplateMap().values().stream()
                .filter(temp -> templateName.equals(temp.getTempname())
                        && siteId.longValue() == temp.getSiteid().longValue())
                .findFirst();
        return opt.map(Template::getTemphtml).orElse(null);
    }
}
