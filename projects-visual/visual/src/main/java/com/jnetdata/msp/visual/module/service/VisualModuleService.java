package com.jnetdata.msp.visual.module.service;

import com.jnetdata.msp.core.service.BaseService;
import com.jnetdata.msp.visual.module.model.VisualModule;
import org.thenicesys.data.api.util.ConditionMap;
import org.thenicesys.mybatis.impl.PropertyWrapper;

import java.util.List;
import java.util.Map;
/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zyj
 * @since 2020-03-23
 */
public interface VisualModuleService extends BaseService<VisualModule> {
        PropertyWrapper<VisualModule> makeSearchWrapper(Map<String, Object> condition);

        List<VisualModule> getTree(ConditionMap entity);
}
