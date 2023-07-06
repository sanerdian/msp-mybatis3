package com.jnetdata.msp.visual.modulenavigation.controller;

import com.jnetdata.msp.core.controller.BaseController;
import com.jnetdata.msp.visual.log.service.ElementService;
import com.jnetdata.msp.visual.modulenavigation.model.ModuleNavigation;
import com.jnetdata.msp.visual.modulenavigation.service.ModuleNavigationService;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.thenicesys.data.api.EntityId;
import org.thenicesys.web.JsonResult;

import java.util.Date;

@RestController
@RequestMapping("/visual/modulenavigation")
@ApiModel(value = "ModuleNavigationController", description = "导航组件管理")
public class ModuleNavigationController extends BaseController<Long, ModuleNavigation> {
    @Autowired
    private ElementService elementService;

    @Autowired
    private ModuleNavigationService moduleNavigationService;

    @ApiOperation(value = "添加")
    @PostMapping
    public JsonResult<EntityId<Long>> add(@Validated @RequestBody ModuleNavigation entity) {
        elementService.AddLog("导航组件","添加导航组件");
        return getService().insertEntity(entity);
    }

    @ApiOperation(value = "删除")
    @DeleteMapping("/{id}")
    public JsonResult<Void> deleteById(@PathVariable("id") Long id) {
        elementService.AddLog("导航组件","删除导航组件");
        return getService().deleteEntity(id);
    }

    @ApiOperation(value = "修改")
    @PutMapping("/{id}")
    public JsonResult<Void> updateById(@PathVariable("id") Long id, @RequestBody ModuleNavigation entity) {
        elementService.AddLog("导航组件","修改导航组件");
        entity.setUpdateTime(new Date());
        return doUpdateById(getService(), id, entity);
    }

    @ApiOperation(value = "查看")
    @GetMapping("/{id}")
    public JsonResult<ModuleNavigation> getById(@PathVariable("id") Long id) {
        elementService.AddLog("导航组件","查看导航组件");
        return doGetById(getService(), id);
    }

    private ModuleNavigationService getService() {
        return this.moduleNavigationService;
    }

}

