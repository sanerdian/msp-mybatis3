package com.jnetdata.msp.visual.moduledetail.service;

import com.jnetdata.msp.core.service.BaseService;
import com.jnetdata.msp.visual.moduledetail.model.ModuleDetail;
import com.jnetdata.msp.visual.relationmoduletemplate.model.RelationModuleTemplate;
import org.thenicesys.data.api.EntityId;
import org.thenicesys.web.JsonResult;

public interface ModuleDetailService extends BaseService<ModuleDetail> {

    /**
     * 新增详情组件
     */
    JsonResult<EntityId<Long>> insertEntity(ModuleDetail entity);

    /**
     * 删除详情组件
     */
    JsonResult<Void> deleteEntity(Long id);

    /**
     * 修改详情组件
     */
    JsonResult<Void> updateEntity(Long id, ModuleDetail entity);

    /**
     * 查询详情组件
     */
    JsonResult<ModuleDetail> getEntity(Long id);

    /**
     * 生成对应的js代码
     */
    String generateJs(RelationModuleTemplate relation);
}
