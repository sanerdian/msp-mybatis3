package com.jnetdata.msp.visual.modulesearch.controller;

import com.jnetdata.msp.core.controller.BaseController;
import com.jnetdata.msp.visual.log.service.ElementService;
import com.jnetdata.msp.visual.modulesearch.model.ModuleSearch;
import com.jnetdata.msp.visual.modulesearch.service.ModuleSearchService;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.thenicesys.data.api.EntityId;
import org.thenicesys.web.JsonResult;

@RestController
@RequestMapping("/visual/modulesearch")
@ApiModel(value = "ModuleSearchController", description = "搜索组件管理")
public class ModuleSearchController extends BaseController<Long, ModuleSearch> {
    @Autowired
    private ElementService elementService;
    @Autowired
    private ModuleSearchService moduleSearchService;
    @ApiOperation(value = "添加")
    @PostMapping
    public JsonResult<EntityId<Long>> add(@Validated @RequestBody ModuleSearch entity) {
        elementService.AddLog("搜索组件","添加搜索组件");
        return getService().insertEntity(entity);
    }

    @ApiOperation(value = "删除")
    @DeleteMapping("/{id}")
    public JsonResult<Void> deleteById(@PathVariable("id") Long id) {
        elementService.AddLog("搜索组件","删除搜索组件");
        return getService().deleteEntity(id);
    }

    @ApiOperation(value = "修改")
    @PutMapping("/{id}")
    public JsonResult<Void> updateById(@PathVariable("id") Long id, @RequestBody ModuleSearch entity) {
        elementService.AddLog("搜索组件","修改搜索组件");
        return getService().updateEntity(id, entity);
    }

    @ApiOperation(value = "查看")
    @GetMapping("/{id}")
    public JsonResult<ModuleSearch> getById(@PathVariable("id") Long id) {
        elementService.AddLog("搜索组件","查看搜索组件");
        return this.getService().getEntity(id);
    }

    private ModuleSearchService getService() {
        return this.moduleSearchService;
    }

}

