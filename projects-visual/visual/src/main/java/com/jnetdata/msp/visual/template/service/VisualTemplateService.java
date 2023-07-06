package com.jnetdata.msp.visual.template.service;

import com.jnetdata.msp.core.service.BaseService;
import com.jnetdata.msp.manage.template.model.Template;
import com.jnetdata.msp.visual.template.model.VisualTemplate;
import org.thenicesys.mybatis.impl.PropertyWrapper;
import org.thenicesys.web.JsonResult;

import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zyj
 * @since 2020-03-23
 */
public interface VisualTemplateService extends BaseService<VisualTemplate> {

    PropertyWrapper<VisualTemplate> makeSearchWrapper(Map<String, Object> condition);

    /**
     * 生成可视化模板对应的js代码和css代码
     * @param entity
     * @author chunping
     * @date 2022/11/18
     */
    void generateJsAndCss(VisualTemplate entity);

    /**
     * 预览
     * @param id
     * @author chunping
     * @date 2020/11/18
     */
    JsonResult<String> preview(Long id);

    /**
     * 获取表单页面发布地址
     * @param formId 表单组件id
     * @author tang.chunping
     * @date 2023/06/09
     */
    JsonResult<String> getFormUrl(Long formId);

    /**
     * 同步到模板并发布
     * @param template
     * @author chunping
     * @date 2022/12/06
     */
    JsonResult<String> syncAndPublish(Template template);


}
