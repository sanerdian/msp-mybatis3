package com.jnetdata.msp.visual.modulenavigation.service;

import com.jnetdata.msp.core.service.BaseService;
import com.jnetdata.msp.visual.modulenavigation.model.ModuleNavigation;
import org.thenicesys.data.api.EntityId;
import org.thenicesys.web.JsonResult;

public interface ModuleNavigationService extends BaseService<ModuleNavigation> {

    /**
     * 新增导航组件
     */
    JsonResult<EntityId<Long>> insertEntity(ModuleNavigation entity);

    /**
     * 删除导航组件
     */
    JsonResult<Void> deleteEntity(Long id);
}
