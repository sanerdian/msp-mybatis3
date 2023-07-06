package com.jnetdata.msp.visual.moduletable.service;

import com.jnetdata.msp.core.service.BaseService;
import com.jnetdata.msp.visual.moduletable.model.ModuleTable;
import com.jnetdata.msp.visual.relationmoduletemplate.model.RelationModuleTemplate;
import org.thenicesys.data.api.EntityId;
import org.thenicesys.web.JsonResult;

public interface ModuleTableService extends BaseService<ModuleTable> {

    /**
     * 新增表格组件
     */
    JsonResult<EntityId<Long>> insertEntity(ModuleTable entity);

    /**
     * 删除表格组件
     */
    JsonResult<Void> deleteEntity(Long id);

    /**
     * 修改表格组件
     */
    JsonResult<Void> updateEntity(Long id, ModuleTable entity);

    /**
     * 查询表格组件
     */
    JsonResult<ModuleTable> getEntity(Long id);

    /**
     * 生成table对应的js代码
     */
    String generateJs(RelationModuleTemplate relation);
}
