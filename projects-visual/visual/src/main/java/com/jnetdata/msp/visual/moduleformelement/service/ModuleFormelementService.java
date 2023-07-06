package com.jnetdata.msp.visual.moduleformelement.service;

import com.jnetdata.msp.core.service.BaseService;
import com.jnetdata.msp.visual.moduleformelement.model.ModuleFormelement;
import com.jnetdata.msp.visual.relationmoduletemplate.model.RelationModuleTemplate;
import org.thenicesys.data.api.EntityId;
import org.thenicesys.web.JsonResult;

public interface ModuleFormelementService extends BaseService<ModuleFormelement> {

    /**
     * 新增表单元素组件
     */
    JsonResult<EntityId<Long>> insertEntity(ModuleFormelement entity);

    /**
     * 删除表单元素组件
     */
    JsonResult<Void> deleteEntity(Long id);

    /**
     * 修改表单元素组件
     */
    JsonResult<Void> updateEntity(Long id, ModuleFormelement entity);

    /**
     * 查询表单元素组件
     */
    JsonResult<ModuleFormelement> getEntity(Long id);

}
