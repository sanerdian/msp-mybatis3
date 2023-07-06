package com.jnetdata.msp.media.service.impl;

import com.jnetdata.msp.manage.column.mapper.ProgramaMapper;
import com.jnetdata.msp.manage.column.model.Programa;
import com.jnetdata.msp.manage.site.mapper.SiteMapper;
import com.jnetdata.msp.manage.site.model.Site;
import com.jnetdata.msp.manage.template.mapper.TemplateMapper;
import com.jnetdata.msp.manage.template.model.Template;
import com.jnetdata.msp.media.directive.PublishConstant;
import com.jnetdata.msp.media.directive.TemplateConfig;
import com.jnetdata.msp.media.directive.TemplateContext;
import com.jnetdata.msp.media.service.MediaPublishService;
import com.jnetdata.msp.tlujy.xinwen020.mapper.Xinwen020Mapper;
import com.jnetdata.msp.tlujy.xinwen020.model.Xinwen020;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thenicesys.mybatis.impl.PropertyWrapper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Service
public class MediaPublishServiceImpl implements MediaPublishService {
    public static final String PUB_HTML = "/pub/html/";
    @Autowired
    private SiteMapper siteMapper;
    @Autowired
    private ProgramaMapper programaMapper;
    @Autowired
    private Xinwen020Mapper xinwenMapper;
    @Autowired
    private TemplateMapper templateMapper;
    @Override
    public String publishDetail(Long docid) throws IOException {
        Integer publishType=PublishConstant.PUBLISH_DOC;
        String templateHtml = getTemphtml(docid, publishType);
        File publishFile = getPublishFile(docid, publishType, null);
        Xinwen020 xinwen = getXinwen(docid);
        Programa channel = getChannel(xinwen.getColumnid());
        Site site = getSite(channel.getSiteId());
        TemplateConfig config = new TemplateConfig();
        config.context.put(PublishConstant.MEDIA_PUBLISH_TYPE,publishType);
        config.context.put(PublishConstant.MEDIA_DOC_ID,docid);
        config.context.put(PublishConstant.MEDIA_DOC_DATA,xinwen);
        config.context.put(PublishConstant.MEDIA_CHANNEL_DATA,channel);
        config.context.put(PublishConstant.MEDIA_SITE_DATA,site);
        config.setTempalteHtml(templateHtml);
        config.setTargetPath(publishFile.getPath());

        savePublish(docid, publishType, null);
        doPublish(config);

        config.context.remove(PublishConstant.MEDIA_PUBLISH_TYPE,publishType);
        config.context.remove(PublishConstant.MEDIA_DOC_ID,docid);
        config.context.remove(PublishConstant.MEDIA_DOC_DATA,xinwen);
        config.context.remove(PublishConstant.MEDIA_CHANNEL_DATA,channel);
        config.context.remove(PublishConstant.MEDIA_SITE_DATA,site);
        return getPublishUrl(docid, publishType, null);
    }

    @Override
    public String publishList(Long columnid, boolean isPage, Integer pageSize) throws IOException {
        Integer publishType=PublishConstant.PUBLISH_CHANNEL;
        String templateHtml = getTemphtml(columnid, publishType);
        Programa channel = getChannel(columnid);
        Site site = getSite(channel.getSiteId());

        TemplateConfig config = new TemplateConfig();
        config.context.put(PublishConstant.MEDIA_PUBLISH_TYPE, publishType);
        config.context.put(PublishConstant.MEDIA_CHANNEL_ID,columnid);
        config.context.put(PublishConstant.MEDIA_CHANNEL_DATA,channel);
        config.context.put(PublishConstant.MEDIA_SITE_DATA,site);
        config.setTempalteHtml(templateHtml);

        Integer pageNo;
        savePublish(columnid, publishType, null);
        File publishFile;
        if(isPage){
            int realPageSize=pageSize!=null?pageSize:PublishConstant.DEFAULT_PAGESIZE;
            Integer total = countXinwensByColumn(columnid);
            if(total>0){
                config.context.put(PublishConstant.MEDIA_PAGE_TOTAL,total);
                config.context.put(PublishConstant.MEDIA_PAGE_SIZE,realPageSize);
                config.context.put(PublishConstant.MEDIA_PAGE_URL_FIRST,getPublishUrl(columnid, publishType, null));
                int max=total/realPageSize+1;
                for(pageNo=1;pageNo<=max;pageNo++){
                    config.context.put(PublishConstant.MEDIA_PAGE_CURRENT,pageNo);
                    publishFile = getPublishFile(columnid, publishType, pageNo);
                    config.setTargetPath(publishFile.getPath());
                    doPublish(config);
                }
                config.context.remove(PublishConstant.MEDIA_PAGE_TOTAL,total);
                config.context.remove(PublishConstant.MEDIA_PAGE_SIZE,realPageSize);
                config.context.remove(PublishConstant.MEDIA_PAGE_URL_FIRST);
                config.context.remove(PublishConstant.MEDIA_PAGE_CURRENT);
            }
        }else {
            publishFile = getPublishFile(columnid, publishType, null);
            config.setTargetPath(publishFile.getPath());
            doPublish(config);
        }

        config.context.remove(PublishConstant.MEDIA_PUBLISH_TYPE,publishType);
        config.context.remove(PublishConstant.MEDIA_CHANNEL_ID,columnid);
        config.context.remove(PublishConstant.MEDIA_CHANNEL_DATA,channel);
        config.context.remove(PublishConstant.MEDIA_SITE_DATA,site);
        return getPublishUrl(columnid, publishType, null);
    }

    @Override
    public String publishColumn(Long columnid) throws IOException {
        List<Xinwen020> xinwens = getXinwensByColumn(columnid);
        for(Xinwen020 xinwen:xinwens){
            publishDetail(xinwen.getId());
        }
        return publishList(columnid, true,null );
    }
    public String publishSite(Long siteid) throws IOException {
        List<Programa> channels = getChannelsBySite(siteid);
        for(Programa channel:channels){
            publishColumn(channel.getId());
        }
        return publishHome(siteid);
    }
    @Override
    public String publishHome(Long siteid) throws IOException {
        Integer publishType=PublishConstant.PUBLISH_SITE;
        String templateHtml = getTemphtml(siteid, publishType);
        File publishFile = getPublishFile(siteid, publishType, null);
        Site site = getSite(siteid);
        TemplateConfig config = new TemplateConfig();
        config.context.put(PublishConstant.MEDIA_PUBLISH_TYPE, publishType);
        config.context.put(PublishConstant.MEDIA_SITE_ID,siteid);
        config.context.put(PublishConstant.MEDIA_SITE_DATA,site);
        config.setTempalteHtml(templateHtml);
        config.setTargetPath(publishFile.getPath());

        savePublish(siteid, publishType, null);
        doPublish(config);

        config.context.remove(PublishConstant.MEDIA_SITE_ID,siteid);
        config.context.remove(PublishConstant.MEDIA_SITE_DATA,site);
        return getPublishUrl(siteid, publishType, null);
    }
    @Override
    public void revokeDetail(Long docid) throws IOException {
        Xinwen020 xinwen = getXinwen(docid);
        File file = getPublishFile(docid, PublishConstant.PUBLISH_DOC, null);
        file.delete();
        publishList(xinwen.getColumnid(),true , null);
    }

    @Override
    public String previewDetail(Long docid) throws IOException {
        Integer publishType=PublishConstant.PREVIEW_DOC;
        String templateHtml = getTemphtml(docid, publishType);
        File publishFile = getPublishFile(docid, publishType, null);
        Xinwen020 xinwen = getXinwen(docid);
        Programa channel = getChannel(xinwen.getColumnid());
        Site site = getSite(channel.getSiteId());
        TemplateConfig config = new TemplateConfig();
        config.context.put(PublishConstant.MEDIA_PUBLISH_TYPE,publishType);
        config.context.put(PublishConstant.MEDIA_DOC_ID,docid);
        config.context.put(PublishConstant.MEDIA_DOC_DATA,xinwen);
        config.context.put(PublishConstant.MEDIA_CHANNEL_DATA,channel);
        config.context.put(PublishConstant.MEDIA_SITE_DATA,site);
        config.setTempalteHtml(templateHtml);
        config.setTargetPath(publishFile.getPath());

        savePublish(docid, publishType, null);
        doPublish(config);

        config.context.remove(PublishConstant.MEDIA_PUBLISH_TYPE,publishType);
        config.context.remove(PublishConstant.MEDIA_DOC_ID,docid);
        config.context.remove(PublishConstant.MEDIA_DOC_DATA,xinwen);
        config.context.remove(PublishConstant.MEDIA_CHANNEL_DATA,channel);
        config.context.remove(PublishConstant.MEDIA_SITE_DATA,site);
        return getPublishUrl(docid, publishType, null);
    }

    private String getPublishUrl(Long id,int publishType,Integer pageNo){
        return getFolderPath(id,publishType)+"/"+getFileName(id,publishType,pageNo);
    }
    /**
     * 获取要发布的文件（用于生成发布的文件）
     * @param id
     * @param publishType
     * @param pageNo
     * @return
     */
    private File getPublishFile(Long id,int publishType,Integer pageNo){
        String folderPath = getFolderPath(id, publishType);
        File folder = new File(getBasePath(),folderPath);
        if(!folder.exists()){
            folder.mkdirs();
        }
        File file = new File(folder,getFileName(id,publishType,pageNo));
        return file;
    }

    /**
     * 用id及id的业务含义生成文件名
     * @param id
     * @param publishType
     * @param pageNo
     * @return
     */
    private String getFileName(Long id,int publishType,Integer pageNo){
        String fileName;
        if(publishType==PublishConstant.PUBLISH_SITE){
            fileName="index";
        }else if(publishType==PublishConstant.PUBLISH_CHANNEL){
            fileName="index";
        }else if(publishType==PublishConstant.PUBLISH_DOC){
            fileName="j_"+id;
        }else if(publishType==PublishConstant.PREVIEW_SITE){
            fileName="j_"+"preview";
        }else if(publishType==PublishConstant.PREVIEW_CHANNEL){
            fileName="j_"+"preview";
        }else if(publishType==PublishConstant.PREVIEW_DOC){
            fileName="j_"+"preview";
        }else{
            throw new RuntimeException("未定义的业务类型！请明确id的业务含义！");
        }
        if(pageNo==null||pageNo==1){
            return fileName+".html";
        }else {
            return fileName+"_"+pageNo+".html";
        }
    }

    /**
     * 用id 及id 的业务含义生成文件的相对发布根目录的相对路径
     * @param id
     * @param publishType
     * @return
     */
    private String getFolderPath(Long id,int publishType){
        String folderPath;
        if(publishType==PublishConstant.PUBLISH_SITE){
            Site site=getSite(id);
            folderPath= PUB_HTML+"/"+site.getCode();
        }else if(publishType==PublishConstant.PUBLISH_CHANNEL){
            Programa channel = getChannel(id);
            Site site=getSite(channel.getSiteId());
            List<Programa> programas= getChannelAndParents(id);
            StringBuffer buffer = new StringBuffer(PUB_HTML);
            buffer.append("/").append(site.getCode());
            for(int index=programas.size()-1;index>=0;index--){
                buffer.append("/").append(programas.get(index).getSkuNumber());
            }
            folderPath = buffer.toString();
        }else if(publishType==PublishConstant.PUBLISH_DOC){
            Xinwen020 xinwen=getXinwen(id);
            folderPath = getFolderPath(xinwen.getColumnid(),PublishConstant.PUBLISH_CHANNEL);
        }else if(publishType==PublishConstant.PREVIEW_SITE
                ||publishType==PublishConstant.PREVIEW_CHANNEL
                ||publishType==PublishConstant.PREVIEW_DOC){
            folderPath=PUB_HTML;
        }else{
            throw new RuntimeException("未定义的业务类型！请明确id的业务含义！");
        }
        return folderPath;
    }
    private String getBasePath() {
        return MediaPublishServiceImpl.class.getResource("/").getPath().replace("/WEB-INF/classes/","");
    }

    private Site getSite(Long siteid){
        Site site=siteMapper.selectById(siteid);
        if(site==null){
            throw new RuntimeException("获取网站失败！");
        }
        return site;
    }
    /**
     * 用栏目主键获取对应的栏目以及其父栏目、祖栏目、曾祖栏目......
     * @param channelid
     * @return
     */
    private List<Programa> getChannelAndParents(Long channelid){
        List<Programa> programas=new ArrayList<>();
        Long pk=channelid;
        while (pk!=null){
            Programa programa = programaMapper.selectById(pk);
            if(programa!=null){
                pk=programa.getParentId();
                programas.add(programa);
            }else {
                pk=null;
            }
        }
        if(programas.size()==0){
            throw new RuntimeException("获取栏目失败！");
        }
        return programas;
    }
    private Programa getChannel(Long channelid) {
        Programa programa = programaMapper.selectById(channelid);
        if(programa==null){
            throw new RuntimeException("获取栏目失败！");
        }
        return programa;
    }
    private List<Programa> getChannelsByParentid(Long parentid){
        PropertyWrapper wrapper = new PropertyWrapper(Programa.class);
        wrapper.eq("parentid",parentid)
                .eq("status",0)
                .orderBy(Arrays.asList(new String[]{"chnlorder"}),false);
        return programaMapper.selectList(wrapper.getWrapper());
    }
    private List<Programa> getChannelsBySite(Long siteid){
        PropertyWrapper wrapper = new PropertyWrapper(Programa.class);
        wrapper.eq("siteid",siteid)
                .eq("status",0)
                .orderBy(Arrays.asList(new String[]{"chnlorder"}),false);
        return programaMapper.selectList(wrapper.getWrapper());
    }
    private Xinwen020 getXinwen(Long docid) {
        Xinwen020 xinwen = xinwenMapper.selectById(docid);
        if(xinwen==null){
            throw new RuntimeException("获取新闻失败！");
        }
        return xinwen;
    }

    private List getXinwensByColumn(Long columnId){
        PropertyWrapper wrapper = getXinwenWrapperByColumn(columnId);
        return xinwenMapper.selectList(wrapper.getWrapper());
    }

    private Integer countXinwensByColumn(Long columnId){
        PropertyWrapper wrapper = getXinwenWrapperByColumn(columnId);
        return xinwenMapper.selectCount(wrapper.getWrapper());
    }
    private PropertyWrapper getXinwenWrapperByColumn(Long columnId) {
        PropertyWrapper wrapper = new PropertyWrapper(Xinwen020.class);
        wrapper.eq("columnid",columnId)
                .eq("status",21)
                .eq("docstatus",0)
                .orderBy(Arrays.asList(new String[]{"ptime"}),false)
        ;
        return wrapper;
    }

    /**
     * 获取指定id对应的模板的html数据
     * @param id
     * @param publishType id的类型，该值为null时则认为id为模板的id
     * @return
     */
    private String getTemphtml(Long id,Integer publishType){
        Long templateid;
        if(publishType==null){
            templateid=id;
        }else if(publishType==PublishConstant.PUBLISH_SITE||publishType==PublishConstant.PREVIEW_SITE){
            Site site = getSite(id);
            templateid=Long.parseLong(site.getHomeTemplateId());
        }else if(publishType==PublishConstant.PUBLISH_CHANNEL||publishType==PublishConstant.PREVIEW_CHANNEL){
            Programa channel = getChannel(id);
            templateid=channel.getListtpl();
        }else if(publishType==PublishConstant.PUBLISH_DOC||publishType==PublishConstant.PREVIEW_DOC){
            Xinwen020 xinwen = getXinwen(id);
            Programa channel = getChannel(xinwen.getColumnid());
            templateid=channel.getDetailtpl();
        }else {
            throw new RuntimeException("未定义的业务类型！请明确id的业务含义！");
        }
        Template tableTemplate = templateMapper.selectById(templateid);
        if(tableTemplate==null){
            throw new RuntimeException("获取模板失败！");
        }
        return tableTemplate.getTemphtml();
    }
    private String getPublishMessge(Long id,Integer publishType){
        String msg,type;
        if(publishType==PublishConstant.PUBLISH_SITE){
            type="发布";
            Site site = getSite(id);
            msg="网站[主键="+id+",站点名称="+site.getName()+",唯一标识="+site.getCode()+"]";
        }else if(publishType==PublishConstant.PUBLISH_CHANNEL){
            type="发布";
            Programa channel = getChannel(id);
            msg="栏目[主键="+id+",栏目名称="+channel.getName()+",唯一标识="+channel.getSkuNumber()+"]";
        }else if(publishType==PublishConstant.PUBLISH_DOC){
            type="发布";
            Xinwen020 xinwen = getXinwen(id);
            msg="文档[主键="+id+"标题="+xinwen.getTitle()+"]";
        }else if(publishType==PublishConstant.PREVIEW_SITE){
            type="预览";
            Site site = getSite(id);
            msg="网站[主键="+id+",站点名称="+site.getName()+",唯一标识="+site.getCode()+"]";
        }else if(publishType==PublishConstant.PREVIEW_CHANNEL){
            type="预览";
            Programa channel = getChannel(id);
            msg="栏目[主键="+id+",栏目名称="+channel.getName()+",唯一标识="+channel.getSkuNumber()+"]";
        }else if(publishType==PublishConstant.PREVIEW_DOC){
            type="预览";
            Xinwen020 xinwen = getXinwen(id);
            msg="文档[主键="+id+"标题="+xinwen.getTitle()+"]";
        }else{
            throw new RuntimeException("未定义的业务类型！请明确id的业务含义！");
        }
        return type+":"+msg;
    }
    private String savePublish(Long id,Integer publishType,Integer pageNo){
        String publishUrl = getPublishUrl(id, publishType, pageNo);
        String dataPath = getPublishFile(id,publishType,pageNo).getAbsolutePath();
        if(pageNo==null||pageNo==1){
            if(publishType==PublishConstant.PUBLISH_SITE){
                Site site = getSite(id);
                site.setWebUrl(publishUrl);
                site.setDataPath(dataPath);
                siteMapper.updateById(site);
            }else if(publishType==PublishConstant.PUBLISH_CHANNEL){
                Programa channel = getChannel(id);
                channel.setChnlDataPath(dataPath);
                channel.setListUrl(publishUrl);
                programaMapper.updateById(channel);
            }else if(publishType==PublishConstant.PUBLISH_DOC){
                Xinwen020 xinwen = getXinwen(id);
                xinwen.setDocpuburl(publishUrl);
                //todo 新闻没保存本地的路径
                Date now =new Date();
                xinwen.setPtime(now);
                xinwen.setDocpubtime(now);
                xinwenMapper.updateById(xinwen);
            }else if(publishType==PublishConstant.PREVIEW_DOC
                    ||publishType==PublishConstant.PREVIEW_CHANNEL
                    ||publishType==PublishConstant.PREVIEW_SITE){
                //预览无需保存！不执行任何代码
            }else{
                throw new RuntimeException("未定义的业务类型！请明确id的业务含义！");
            }
        }

        return publishUrl;
    }

    /**
     * 模板与数据合成后，生成发布文件
     * @param config
     * @throws IOException
     */
    public void doPublish(TemplateConfig config) throws IOException {
        TemplateContext ctx = new TemplateContext(config);
        ctx.init();
        ctx.merge();
    }

}

