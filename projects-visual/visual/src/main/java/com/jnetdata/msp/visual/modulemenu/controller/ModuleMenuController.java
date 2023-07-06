package com.jnetdata.msp.visual.modulemenu.controller;

import com.jnetdata.msp.core.controller.BaseController;
import com.jnetdata.msp.visual.log.service.ElementService;
import com.jnetdata.msp.visual.modulemenu.model.ModuleMenu;
import com.jnetdata.msp.visual.modulemenu.service.ModuleMenuService;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.thenicesys.data.api.EntityId;
import org.thenicesys.web.JsonResult;

import java.util.Date;

@RestController
@RequestMapping("/visual/modulemenu")
@ApiModel(value = "ModuleMenuController", description = "菜单组件管理")
public class ModuleMenuController extends BaseController<Long, ModuleMenu> {

    @Autowired
    private ElementService elementService;
    @Autowired
    private ModuleMenuService moduleMenuService;

    @ApiOperation(value = "添加")
    @PostMapping
    public JsonResult<EntityId<Long>> add(@Validated @RequestBody ModuleMenu entity) {
        elementService.AddLog("菜单组件管理","添加菜单组件管理");
        return getService().insertEntity(entity);
    }

    @ApiOperation(value = "删除")
    @DeleteMapping("/{id}")
    public JsonResult<Void> deleteById(@PathVariable("id") Long id) {
        elementService.AddLog("菜单组件管理","删除菜单组件管理");
        return getService().deleteEntity(id);
    }

    @ApiOperation(value = "修改")
    @PutMapping("/{id}")
    public JsonResult<Void> updateById(@PathVariable("id") Long id, @RequestBody ModuleMenu entity) {
        elementService.AddLog("菜单组件管理","修改菜单组件管理");
        entity.setUpdateTime(new Date());
        return doUpdateById(getService(), id, entity);
    }

    @ApiOperation(value = "查看")
    @GetMapping("/{id}")
    public JsonResult<ModuleMenu> getById(@PathVariable("id") Long id) {
        elementService.AddLog("菜单组件管理","查看菜单组件管理");
        return doGetById(getService(), id);
    }

    private ModuleMenuService getService() {
        return this.moduleMenuService;
    }

}

