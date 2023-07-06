package com.jnetdata.msp.visual.modulecustombutton.service;

import com.jnetdata.msp.core.service.BaseService;
import com.jnetdata.msp.visual.modulecustombutton.model.ModuleCustombutton;
import com.jnetdata.msp.visual.relationmoduletemplate.model.RelationModuleTemplate;
import org.thenicesys.data.api.EntityId;
import org.thenicesys.web.JsonResult;

public interface ModuleCustombuttonService extends BaseService<ModuleCustombutton> {

    /**
     * 新增自定义按钮组件
     */
    JsonResult<EntityId<Long>> insertEntity(ModuleCustombutton entity);

    /**
     * 删除自定义按钮组件
     */
    JsonResult<Void> deleteEntity(Long id);

    /**
     * 修改自定义按钮组件
     */
    JsonResult<Void> updateEntity(Long id, ModuleCustombutton entity);

    /**
     * 查询自定义按钮组件
     */
    JsonResult<ModuleCustombutton> getEntity(Long id);

    /**
     * 生成对应的js代码
     */
    String generateJs(RelationModuleTemplate relation);
}
