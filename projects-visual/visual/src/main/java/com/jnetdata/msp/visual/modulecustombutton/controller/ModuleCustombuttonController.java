package com.jnetdata.msp.visual.modulecustombutton.controller;

import com.jnetdata.msp.core.controller.BaseController;
import com.jnetdata.msp.visual.log.service.ElementService;
import com.jnetdata.msp.visual.modulecustombutton.model.ModuleCustombutton;
import com.jnetdata.msp.visual.modulecustombutton.service.ModuleCustombuttonService;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.thenicesys.data.api.EntityId;
import org.thenicesys.web.JsonResult;

@RestController
@RequestMapping("/visual/modulecustombutton")
@ApiModel(value = "ModuleCustombuttonController", description = "自定义按钮组件管理")
public class ModuleCustombuttonController extends BaseController<Long, ModuleCustombutton> {
    @Autowired
    private ElementService elementService;
    @Autowired
    private ModuleCustombuttonService moduleCustombuttonService;
    @ApiOperation(value = "添加")
    @PostMapping
    public JsonResult<EntityId<Long>> add(@Validated @RequestBody ModuleCustombutton entity) {
        elementService.AddLog("自定义按钮组件","添加自定义按钮组件");
        return getService().insertEntity(entity);
    }

    @ApiOperation(value = "删除")
    @DeleteMapping("/{id}")
    public JsonResult<Void> deleteById(@PathVariable("id") Long id) {
        elementService.AddLog("自定义按钮组件","删除自定义按钮组件");
        return getService().deleteEntity(id);
    }

    @ApiOperation(value = "修改")
    @PutMapping("/{id}")
    public JsonResult<Void> updateById(@PathVariable("id") Long id, @RequestBody ModuleCustombutton entity) {
        elementService.AddLog("自定义按钮组件","修改自定义按钮组件");
        return getService().updateEntity(id, entity);
    }

    @ApiOperation(value = "查看")
    @GetMapping("/{id}")
    public JsonResult<ModuleCustombutton> getById(@PathVariable("id") Long id) {
        elementService.AddLog("自定义按钮组件","查看自定义按钮组件");
        return this.getService().getEntity(id);
    }

    private ModuleCustombuttonService getService() {
        return this.moduleCustombuttonService;
    }

}

