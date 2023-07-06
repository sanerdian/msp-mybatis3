package com.jnetdata.msp.visual.modulemenu.service;

import com.jnetdata.msp.core.service.BaseService;
import com.jnetdata.msp.visual.modulemenu.model.ModuleMenu;
import com.jnetdata.msp.visual.relationmoduletemplate.model.RelationModuleTemplate;
import org.thenicesys.data.api.EntityId;
import org.thenicesys.web.JsonResult;

public interface ModuleMenuService extends BaseService<ModuleMenu> {

    /**
     * 新增菜单组件
     * @author chunping
     * @date 2022/12/12
     */
    JsonResult<EntityId<Long>> insertEntity(ModuleMenu entity);

    /**
     * 删除菜单组件
     * @author chunping
     * @date 2022/12/12
     */
    JsonResult<Void> deleteEntity(Long id);

    /**
     * 生成menu对应的js代码
     */
    String generateMenuJs(RelationModuleTemplate relation);
}
