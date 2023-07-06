package com.jnetdata.msp.visual.modulesearch.service;

import com.jnetdata.msp.core.service.BaseService;
import com.jnetdata.msp.visual.modulesearch.model.ModuleSearch;
import com.jnetdata.msp.visual.relationmoduletemplate.model.RelationModuleTemplate;
import org.thenicesys.data.api.EntityId;
import org.thenicesys.web.JsonResult;

public interface ModuleSearchService extends BaseService<ModuleSearch> {

    /**
     * 新增搜索组件
     */
    JsonResult<EntityId<Long>> insertEntity(ModuleSearch entity);

    /**
     * 删除搜索组件
     */
    JsonResult<Void> deleteEntity(Long id);

    /**
     * 修改搜索组件
     */
    JsonResult<Void> updateEntity(Long id, ModuleSearch entity);

    /**
     * 查询搜索组件
     */
    JsonResult<ModuleSearch> getEntity(Long id);

    /**
     * 搜索组件生成js
     */
    String generateJs(RelationModuleTemplate relation);
}
