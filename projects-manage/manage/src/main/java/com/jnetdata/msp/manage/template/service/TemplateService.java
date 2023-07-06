package com.jnetdata.msp.manage.template.service;

import com.jnetdata.msp.core.service.BaseService;
import com.jnetdata.msp.manage.template.model.Template;
import org.thenicesys.mybatis.impl.PropertyWrapper;

import java.util.Map;

public interface TemplateService extends BaseService<Template> {
    PropertyWrapper<Template> makeSearchWrapper(Map<String, Object> condition);
    void updateUse(Long[] ids);
}
