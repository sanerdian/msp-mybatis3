package com.jnetdata.msp.visual.modulechart.service;

import com.jnetdata.msp.core.service.BaseService;
import com.jnetdata.msp.visual.modulechart.model.ModuleChart;
import com.jnetdata.msp.visual.relationmoduletemplate.model.RelationModuleTemplate;
import org.thenicesys.data.api.EntityId;
import org.thenicesys.web.JsonResult;

public interface ModuleChartService extends BaseService<ModuleChart> {
    /**
     *新增echarts组件
     */
    JsonResult<EntityId<Long>> insertEntity(ModuleChart entity);
    /**
     *删除echarts组件
     */
    JsonResult<Void> deleteEntity(Long id);
    /**
     *修改echarts组件
     */
    JsonResult<Void> updateEntity(Long id, ModuleChart entity);
    /**
     *查询echarts组件
     */
    JsonResult<ModuleChart> getEntity(Long id);
    /**
     * 生成对应的js代码
     */
    String generateJs(RelationModuleTemplate relation);
}
