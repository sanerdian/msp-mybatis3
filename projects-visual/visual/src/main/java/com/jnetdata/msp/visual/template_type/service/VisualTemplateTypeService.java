package com.jnetdata.msp.visual.template_type.service;

import com.jnetdata.msp.core.service.BaseService;
import com.jnetdata.msp.visual.template_type.model.VisualTemplateType;
import org.thenicesys.mybatis.impl.PropertyWrapper;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 组件类型 服务类
 * </p>
 *
 * @author zyj
 * @since 2022-08-08
 */
public interface VisualTemplateTypeService extends BaseService<VisualTemplateType> {
    PropertyWrapper<VisualTemplateType> makeSearchWrapper(Map<String, Object> condition);
}
