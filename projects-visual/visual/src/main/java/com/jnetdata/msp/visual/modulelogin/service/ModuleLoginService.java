package com.jnetdata.msp.visual.modulelogin.service;

import com.jnetdata.msp.core.service.BaseService;
import com.jnetdata.msp.visual.modulelogin.model.ModuleLogin;
import com.jnetdata.msp.visual.relationmoduletemplate.model.RelationModuleTemplate;
import org.thenicesys.data.api.EntityId;
import org.thenicesys.web.JsonResult;

public interface ModuleLoginService extends BaseService<ModuleLogin> {

    /**
     * 新增登录组件
     */
    JsonResult<EntityId<Long>> insertEntity(ModuleLogin entity);

    /**
     * 删除登录组件
     */
    JsonResult<Void> deleteEntity(Long id);

    /**
     * 修改登录组件
     */
    JsonResult<Void> updateEntity(Long id, ModuleLogin entity);

    /**
     * 查询登录组件
     */
    JsonResult<ModuleLogin> getEntity(Long id);

    /**
     * 生成对应的js代码
     */
    String generateJs(RelationModuleTemplate relation);

    /**
     *登录组件生成CSS代码
     * @return
     */
    String generateCss(RelationModuleTemplate relation);
}
