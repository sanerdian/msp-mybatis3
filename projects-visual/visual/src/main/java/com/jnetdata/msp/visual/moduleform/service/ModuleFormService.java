package com.jnetdata.msp.visual.moduleform.service;

import com.jnetdata.msp.core.service.BaseService;
import com.jnetdata.msp.visual.moduleform.model.ModuleForm;
import com.jnetdata.msp.visual.relationmoduletemplate.model.RelationModuleTemplate;
import org.thenicesys.data.api.EntityId;
import org.thenicesys.web.JsonResult;

public interface ModuleFormService extends BaseService<ModuleForm> {

    /**
     * 新增表单组件
     */
    JsonResult<EntityId<Long>> insertEntity(ModuleForm entity);

    /**
     * 删除表单组件
     */
    JsonResult<Void> deleteEntity(Long id);

    /**
     * 修改表单组件
     */
    JsonResult<Void> updateEntity(Long id, ModuleForm entity);

    /**
     * 查询表单组件
     */
    JsonResult<ModuleForm> getEntity(Long id);
    
    /**
     * 生成对应的js代码
     */
    String generateJs(RelationModuleTemplate relation);

    /**
     *表单组件生成CSS代码
     * @return
     */
    String generateCss(RelationModuleTemplate relation);
}
