package com.jnetdata.msp.visual.module_type.service;

import com.jnetdata.msp.visual.module_type.model.VisualModuleType;
import com.jnetdata.msp.core.service.BaseService;
import org.thenicesys.mybatis.impl.PropertyWrapper;

import java.util.Map;
import java.util.List;
/**
 * <p>
 * 组件类型 服务类
 * </p>
 *
 * @author zyj
 * @since 2022-08-08
 */
public interface VisualModuleTypeService extends BaseService<VisualModuleType> {
    PropertyWrapper<VisualModuleType> makeSearchWrapper(Map<String, Object> condition);
    void add(VisualModuleType entity);

    void addBatch(List<VisualModuleType> list);

    /**
     * 获取所有组件类型id与code的映射
     */
    Map<Long, String> getTypeMap();
}
