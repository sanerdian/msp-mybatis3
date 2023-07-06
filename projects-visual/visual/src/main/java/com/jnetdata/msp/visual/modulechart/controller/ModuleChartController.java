package com.jnetdata.msp.visual.modulechart.controller;

import com.jnetdata.msp.core.controller.BaseController;
import com.jnetdata.msp.visual.log.service.ElementService;
import com.jnetdata.msp.visual.modulechart.model.ModuleChart;
import com.jnetdata.msp.visual.modulechart.service.ModuleChartService;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.thenicesys.data.api.EntityId;
import org.thenicesys.web.JsonResult;

@RestController
@RequestMapping("/visual/modulechart")
@ApiModel(value = "ModuleChartController", description = "echarts组件管理")
public class ModuleChartController extends BaseController<Long, ModuleChart> {;
    @Autowired
    private ElementService elementService;
    @Autowired
    private ModuleChartService moduleChartService;

    @ApiOperation(value = "添加")
    @PostMapping
    public JsonResult<EntityId<Long>> add(@Validated @RequestBody ModuleChart entity) {
        elementService.AddLog("echarts组件","添加echarts组件");
        return getService().insertEntity(entity);
    }

    @ApiOperation(value = "删除")
    @DeleteMapping("/{id}")
    public JsonResult<Void> deleteById(@PathVariable("id") Long id) {
        elementService.AddLog("echarts组件","删除echarts组件");
        return getService().deleteEntity(id);
    }

    @ApiOperation(value = "修改")
    @PutMapping("/{id}")
    public JsonResult<Void> updateById(@PathVariable("id") Long id, @RequestBody ModuleChart entity) {
        elementService.AddLog("echarts组件","修改echarts组件");
        return getService().updateEntity(id, entity);
    }

    @ApiOperation(value = "查看")
    @GetMapping("/{id}")
    public JsonResult<ModuleChart> getById(@PathVariable("id") Long id) {
        elementService.AddLog("echarts组件","查看echarts组件");
        return this.getService().getEntity(id);
    }

    private ModuleChartService getService() {
        return this.moduleChartService;
    }
}
