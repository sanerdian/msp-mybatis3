package com.jnetdata.msp.manage.tplresource.service.impl;

import com.jnetdata.msp.config.config.model.ConfigModel;
import com.jnetdata.msp.config.config.service.ConfigModelService;
import com.jnetdata.msp.manage.site.model.Site;
import com.jnetdata.msp.manage.site.service.SiteService;
import com.jnetdata.msp.manage.tplresource.model.JTemplateResource;
import com.jnetdata.msp.manage.tplresource.mapper.JTemplateResourceMapper;
import com.jnetdata.msp.manage.tplresource.service.JTemplateResourceService;
import com.jnetdata.msp.core.service.impl.BaseServiceImpl;
import com.jnetdata.msp.util.Base64ImgUtil;
import com.jnetdata.msp.vo.BaseVo;
import org.apache.logging.log4j.util.Base64Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.thenicesys.data.api.Pager;
import org.thenicesys.data.api.util.ConditionMap;
import org.thenicesys.mybatis.impl.PropertyWrapper;
import com.baomidou.mybatisplus.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.thenicesys.web.JsonResult;
import org.thenicesys.web.vo.PageVo1;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;


/**
 * <p>
 * 模板资源(js,css,img) 服务实现类
 * </p>
 *
 * @author zyj
 * @since 2022-07-12
 */
@Service
public class JTemplateResourceServiceImpl extends BaseServiceImpl<JTemplateResourceMapper, JTemplateResource> implements JTemplateResourceService {


    @Autowired
    private ConfigModelService configModelService;

    @Autowired
    private SiteService siteService;

    private String andOr = "and";
    @Override
    public PropertyWrapper<JTemplateResource> makeSearchWrapper(Map<String, Object> condition) {
        if(condition.get("andOr")!=null) this.andOr = condition.get("andOr").toString();
        PropertyWrapper<JTemplateResource> pw = createWrapperUtil(condition)
                .eq("content")
                .eq("type")
                .eq("siteid")
                .eq("templateId")
                .eq("sitename")
                .eq("visualType")
        .eq("createBy")
        .getWrapper();
        if (condition.get("columnid") != null) {
            pw.and(m -> m.eq("columnid",condition.get("columnid")).or().like("quotainfo",condition.get("columnid") + ":"));
        }
        andOr(condition,pw,"title");
        andOr(condition,pw,"rdesc");
        andOr(condition,pw,"path");

        if(condition.get("pubIds")!=null){
            List<Long> pubIds = (List<Long>)condition.get("pubIds");
            pw.in(!pubIds.isEmpty(),"id",pubIds);
        }

        return pw;
    }

    @Override
    public Pager listing(String type, PageVo1 pager, Map<String, Object> condition){
        condition.put("docstatus",0);
        condition.put("type",type);
        Pager page = super.list(pager.toPager(), makeSearchWrapper(condition).select("TITLE title","RDESC rdesc","PATH path","FULL_PATH fullPath","PUB_URL pubUrl","SITENAME sitename","TYPE type","TEMPLATE_ID templateId","TEMPLATE_NAME templateName","ID id","DOCSTATUS docstatus","SITEID siteid","DOCPUBTIME docpubtime","STATUS status","MODIFY_USER modifyUser","CRUSER crUser","CRTIME createTime","MODIFY_TIME modifyTime"));
        return page;
    }

    @Override
    public List<JTemplateResource> listingForVisualModule(String type, Long templateId) {
        return listingForVisual(type,templateId,1);
    }

    public List<JTemplateResource> listingForVisual(String type, Long templateId, int visualType) {
        Map<String, Object> condition = new ConditionMap<>();
        condition.put("docstatus",0);
        if(type!=null) condition.put("type",type);
        condition.put("templateId",templateId);
        condition.put("visualType",visualType);
        List page = super.list(makeSearchWrapper(condition));
        return page;
    }

    @Override
    public void setSite(String type,JTemplateResource entity){
        if(entity.getSiteid() == null) throw new RuntimeException("站点不能为空");
        Site site = siteService.getById(entity.getSiteid());
        if(site == null)  throw new RuntimeException("站点不存在");
        entity.setSitename(site.getName());
        entity.setType(type);
        entity.setExt(type);
        String path = "pub/"+type+"/"+site.getDataPath();
        if(!File.separator.equals("/")) path.replace("/",File.separator);
        entity.setPath(path);
    }

    public JTemplateResource getEntityFromFile(String type,Long id,String visualType, MultipartFile file){
        JTemplateResource entity = new JTemplateResource();
        entity.setType(type);
        entity.setTitle(file.getOriginalFilename().substring(0,file.getOriginalFilename().lastIndexOf(".")));
        entity.setExt(file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".")+1));
        if(visualType.equals("common")){
            entity.setSiteid(id);
            setSite(type,entity);
        }else {
            entity.putVisualTypeByString(visualType);
            entity.setTemplateId(id);
            String path = type+"/"+id+"_"+file.getOriginalFilename();
            entity.setPubUrl(path);
        }
        return entity;
    }

    @Override
    public JTemplateResource getJsEntity(Long siteId, MultipartFile file){
        return getEntity("js",siteId,file);
    }

    @Override
    public JTemplateResource getCssEntity(Long siteId, MultipartFile file){
        return getEntity("css",siteId,file);
    }

    @Override
    public JTemplateResource getEntity(String type,Long siteId, MultipartFile file){
        return getEntity1(type,siteId,"common",file);
    }

    @Override
    public void editImg(Long rid, String title, MultipartFile file) {
        try {
            JTemplateResource entity = new JTemplateResource();
            entity.setId(rid);
            entity.setTitle(title);
            entity.setContent(Base64ImgUtil.ImageToBase64ByInputStream(file.getInputStream()));
            super.updateById(entity);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public JTemplateResource addVisualModuleImg(Long moduleId, MultipartFile file) {
        return addForVisualModule("img", moduleId, file);
    }

    @Override
    public JTemplateResource addVisualModuleJs(Long moduleId, MultipartFile file) {
        return addForVisualModule("js",moduleId, file);
    }

    @Override
    public JTemplateResource addVisualModuleCss(Long moduleId, MultipartFile file) {
        return  addForVisualModule("css",moduleId, file);
    }

    @Override
    public void updateById4Visual(Long id, JTemplateResource entity) {
        entity.setId(id);
        super.updateById(entity);
        pubFile(id);
    }

    public JTemplateResource addForVisualModule(String type, Long moduleId, MultipartFile file) {
        JTemplateResource entity = getEntity1(type,moduleId,"module",file);
        super.insert(entity);
        pubFile(entity);
        return entity;
    }

    public JTemplateResource getEntity1(String type,Long id,String visualType, MultipartFile file){
        if(type.equals("img")){
            JTemplateResource entity = getEntityFromFile("img",id,visualType,file);
            try {
                entity.setContent(Base64ImgUtil.ImageToBase64ByInputStream(file.getInputStream()));
            } catch (IOException e) {
                e.printStackTrace();
            }
            return entity;
        }else{
            if (!file.getOriginalFilename().contains(type)) {
                throw new RuntimeException("请上传"+type+"文件");
            }
            JTemplateResource entity = getEntityFromFile(type,id,visualType,file);
            BufferedReader br = null;
            try {
                br = new BufferedReader(new InputStreamReader(file.getInputStream(), "UTF-8"));
                String content = br.lines().collect(Collectors.joining("\r\n"));
                entity.setContent(content);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return entity;
        }
    }

    @Override
    public JTemplateResource getImgEntity(Long siteId, MultipartFile file){
        return getEntity("img",siteId,file);
    }

    @Override
    public void pubFile(Long id){
        pubFile(super.getById(id));
    }

    public void pubFile(JTemplateResource entity){
        if(entity.getTemplateId() != null){
            entity.setPath("visual" + File.separator +entity.getType() + File.separator + entity.getTemplateId());
        }else{
            Site site = siteService.getById(entity.getSiteid());
            entity.setPath("pub" + File.separator +entity.getType()+File.separator+site.getDataPath());
        }
        String path = configModelService.getBaseUploadPath() + File.separator + entity.getPath();
        File dir = new File(path);
        dir.mkdirs();
        path = path + File.separator + entity.getTitle() + "." + entity.getExt();
        if(entity.getType().equals("img")){
            pubImg(entity.getContent(),path);
        }else{
            pubFile(entity.getContent(),new File(path));
        }

        entity.setFullPath(path);
        entity.setPubUrl("/files/" + entity.getPath().replace("\\","/")+"/"+entity.getTitle() + "." + entity.getExt());
        entity.setStatus(1);
        super.updateById(entity);
    }

    @Override
    public void pubFiles(Long[] ids){
        for (Long id : ids) {
            this.pubFile(id);
        }
    }

    public void pubImg(String content,String path){
        Base64ImgUtil.Base64ToImage(content,path);
    }

    public void pubFile(String content,File file){
        InputStream inputStream = null;
        try {
            inputStream = new ByteArrayInputStream(content.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        inputstreamtofile(inputStream, file);
    }

    public static void inputstreamtofile(InputStream ins, File file){
        try {
            OutputStream os = new FileOutputStream(file);
            int bytesRead = 0;
            byte[] buffer = new byte[8192];
            while ((bytesRead = ins.read(buffer, 0, 8192)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
            os.close();
            ins.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void andOr(Map<String,Object>condition,PropertyWrapper pw,String proName){
        if(condition.get(proName)!=null&&StringUtils.isNotEmpty(condition.get(proName).toString())){
            Consumer<QueryWrapper<JTemplateResource>> queryWrapperConsumer = w -> {
                for(String str:condition.get(proName).toString().split(" ")){
                    if(StringUtils.isNotEmpty(str)){
                        if(andOr.equals("or" )) w.or();
                        w.like(pw.getColumn(proName),str);
                    }
                }
            };
            pw.and(queryWrapperConsumer);
        }
    }
}
