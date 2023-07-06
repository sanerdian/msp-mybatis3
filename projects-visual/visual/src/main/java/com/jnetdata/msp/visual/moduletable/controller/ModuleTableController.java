package com.jnetdata.msp.visual.moduletable.controller;

import com.jnetdata.msp.core.controller.BaseController;
import com.jnetdata.msp.visual.log.service.ElementService;
import com.jnetdata.msp.visual.moduletable.model.ModuleTable;
import com.jnetdata.msp.visual.moduletable.service.ModuleTableService;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.thenicesys.data.api.EntityId;
import org.thenicesys.web.JsonResult;


@RestController
@RequestMapping("/visual/moduletable")
@ApiModel(value = "ModuleTableController", description = "表格组件管理")
public class ModuleTableController extends BaseController<Long, ModuleTable> {


    @Autowired
    private ElementService elementService;
    @Autowired
    private ModuleTableService moduleTableService;
    @ApiOperation(value = "添加")
    @PostMapping
    public JsonResult<EntityId<Long>> add(@Validated @RequestBody ModuleTable entity) {
        elementService.AddLog("表格组件","添加表格组件");
        return getService().insertEntity(entity);
    }

    @ApiOperation(value = "删除")
    @DeleteMapping("/{id}")
    public JsonResult<Void> deleteById(@PathVariable("id") Long id) {
        elementService.AddLog("表格组件","删除表格组件");
        return getService().deleteEntity(id);
    }

    @ApiOperation(value = "修改")
    @PutMapping("/{id}")
    public JsonResult<Void> updateById(@PathVariable("id") Long id, @RequestBody ModuleTable entity) {
        elementService.AddLog("表格组件","修改表格组件");
        return getService().updateEntity(id, entity);
    }

    @ApiOperation(value = "查看")
    @GetMapping("/{id}")
    public JsonResult<ModuleTable> getById(@PathVariable("id") Long id) {
        elementService.AddLog("表格组件","查看表格组件");
        return this.getService().getEntity(id);
    }

    private ModuleTableService getService() {
        return this.moduleTableService;
    }

}

