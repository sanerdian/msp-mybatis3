package com.jnetdata.msp.manage.tplresource.service;

import com.jnetdata.msp.manage.tplresource.model.JTemplateResource;
import com.jnetdata.msp.core.service.BaseService;
import org.springframework.web.multipart.MultipartFile;
import org.thenicesys.data.api.Pager;
import org.thenicesys.mybatis.impl.PropertyWrapper;
import org.thenicesys.web.JsonResult;
import org.thenicesys.web.vo.PageVo1;

import java.util.Map;
import java.util.List;
/**
 * <p>
 * 模板资源(js,css,img) 服务类
 * </p>
 *
 * @author zyj
 * @since 2022-07-12
 */
public interface JTemplateResourceService extends BaseService<JTemplateResource> {
    PropertyWrapper<JTemplateResource> makeSearchWrapper(Map<String, Object> condition);
    Pager listing(String type, PageVo1 pager, Map<String, Object> condition);
    List<JTemplateResource> listingForVisualModule(String type,Long templateId);
    JTemplateResource getEntity(String type,Long siteId, MultipartFile file);
    JTemplateResource getJsEntity(Long siteId, MultipartFile file);
    JTemplateResource getCssEntity(Long siteId, MultipartFile file);
    JTemplateResource getImgEntity(Long siteId, MultipartFile file);
    void setSite(String type,JTemplateResource entity);
    void pubFile(Long id);
    void pubFiles(Long[] ids);
    void editImg(Long rid, String title, MultipartFile file);
    JTemplateResource addVisualModuleImg(Long moduleId, MultipartFile file);
    JTemplateResource addVisualModuleJs(Long moduleId, MultipartFile file);
    JTemplateResource addVisualModuleCss(Long moduleId, MultipartFile file);
    void updateById4Visual(Long id, JTemplateResource entity);
}
