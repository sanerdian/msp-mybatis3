package com.jnetdata.msp.visual.modulecarousel.service;

import com.jnetdata.msp.core.service.BaseService;
import com.jnetdata.msp.visual.modulecarousel.model.ModuleCarousel;
import com.jnetdata.msp.visual.relationmoduletemplate.model.RelationModuleTemplate;
import org.thenicesys.data.api.EntityId;
import org.thenicesys.web.JsonResult;

public interface ModuleCarouselService extends BaseService<ModuleCarousel> {

    /**
     * 新增轮播组件
     */
    JsonResult<EntityId<Long>> insertEntity(ModuleCarousel entity);

    /**
     * 删除轮播组件
     */
    JsonResult<Void> deleteEntity(Long id);

    /**
     * 修改轮播组件
     */
    JsonResult<Void> updateEntity(Long id, ModuleCarousel entity);

    /**
     * 查询轮播组件
     */
    JsonResult<ModuleCarousel> getEntity(Long id);

    /**
     * 生成对应的js代码
     */
    String generateJs(RelationModuleTemplate relation);
}
