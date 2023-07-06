package com.jnetdata.msp.visual.moduletextlist.service;

import com.jnetdata.msp.core.service.BaseService;
import com.jnetdata.msp.visual.moduletextlist.model.ModuleTextlist;
import com.jnetdata.msp.visual.relationmoduletemplate.model.RelationModuleTemplate;
import org.thenicesys.data.api.EntityId;
import org.thenicesys.web.JsonResult;

public interface ModuleTextlistService extends BaseService<ModuleTextlist> {

    /**
     * 新增文字列表组件
     */
    JsonResult<EntityId<Long>> insertEntity(ModuleTextlist entity);

    /**
     * 删除文字列表组件
     */
    JsonResult<Void> deleteEntity(Long id);

    /**
     * 修改文字列表组件
     */
    JsonResult<Void> updateEntity(Long id, ModuleTextlist entity);

    /**
     * 查询文字列表组件
     */
    JsonResult<ModuleTextlist> getEntity(Long id);

    /**
     * 生成text对应的js代码
     */
    String generateJs(RelationModuleTemplate relation);
}
