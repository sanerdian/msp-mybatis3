package com.jnetdata.msp.visual.moduleformelement.controller;

import com.jnetdata.msp.core.controller.BaseController;
import com.jnetdata.msp.visual.log.service.ElementService;
import com.jnetdata.msp.visual.moduleformelement.model.ModuleFormelement;
import com.jnetdata.msp.visual.moduleformelement.service.ModuleFormelementService;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.thenicesys.data.api.EntityId;
import org.thenicesys.web.JsonResult;


@RestController
@RequestMapping("/visual/moduleformelement")
@ApiModel(value = "ModuleFormelementController", description = "表单元素组件管理")
public class ModuleFormelementController extends BaseController<Long, ModuleFormelement> {
    @Autowired
    private ElementService elementService;
    @Autowired
    private ModuleFormelementService moduleFormelementService;
    @ApiOperation(value = "添加")
    @PostMapping
    public JsonResult<EntityId<Long>> add(@Validated @RequestBody ModuleFormelement entity) {
        elementService.AddLog("表单元组件","添加表单元组件");
        return getService().insertEntity(entity);
    }

    @ApiOperation(value = "删除")
    @DeleteMapping("/{id}")
    public JsonResult<Void> deleteById(@PathVariable("id") Long id) {
        elementService.AddLog("表单元组件","删除表单元组件");
        return getService().deleteEntity(id);
    }

    @ApiOperation(value = "修改")
    @PutMapping("/{id}")
    public JsonResult<Void> updateById(@PathVariable("id") Long id, @RequestBody ModuleFormelement entity) {
        elementService.AddLog("表单元组件","修改表单元组件");
        return getService().updateEntity(id, entity);
    }

    @ApiOperation(value = "查看")
    @GetMapping("/{id}")
    public JsonResult<ModuleFormelement> getById(@PathVariable("id") Long id) {
        elementService.AddLog("表单元组件","查看表单元组件");
        return this.getService().getEntity(id);
    }

    private ModuleFormelementService getService() {
        return this.moduleFormelementService;
    }

}

