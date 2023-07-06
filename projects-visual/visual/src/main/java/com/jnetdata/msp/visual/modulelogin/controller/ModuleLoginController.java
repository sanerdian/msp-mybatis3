package com.jnetdata.msp.visual.modulelogin.controller;

import com.jnetdata.msp.core.controller.BaseController;
import com.jnetdata.msp.visual.log.service.ElementService;
import com.jnetdata.msp.visual.modulelogin.model.ModuleLogin;
import com.jnetdata.msp.visual.modulelogin.service.ModuleLoginService;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.thenicesys.data.api.EntityId;
import org.thenicesys.web.JsonResult;


@RestController
@RequestMapping("/visual/modulelogin")
@ApiModel(value = "ModuleLoginController", description = "图文列表组件管理")
public class ModuleLoginController extends BaseController<Long, ModuleLogin> {
    @Autowired
    private ElementService elementService;
    @Autowired
    private ModuleLoginService moduleLoginService;
    @ApiOperation(value = "添加")
    @PostMapping
    public JsonResult<EntityId<Long>> add(@Validated @RequestBody ModuleLogin entity) {
        elementService.AddLog("图文列表组件","添加图文列表组件");
        return getService().insertEntity(entity);
    }

    @ApiOperation(value = "删除")
    @DeleteMapping("/{id}")
    public JsonResult<Void> deleteById(@PathVariable("id") Long id) {

        elementService.AddLog("图文列表组件","删除图文列表组件");
        return getService().deleteEntity(id);
    }

    @ApiOperation(value = "修改")
    @PutMapping("/{id}")
    public JsonResult<Void> updateById(@PathVariable("id") Long id, @RequestBody ModuleLogin entity) {
        elementService.AddLog("图文列表组件","修改图文列表组件");
        return getService().updateEntity(id, entity);
    }

    @ApiOperation(value = "查看")
    @GetMapping("/{id}")
    public JsonResult<ModuleLogin> getById(@PathVariable("id") Long id) {
        elementService.AddLog("图文列表组件","查看图文列表组件");
        return this.getService().getEntity(id);
    }

    private ModuleLoginService getService() {
        return this.moduleLoginService;
    }

}

