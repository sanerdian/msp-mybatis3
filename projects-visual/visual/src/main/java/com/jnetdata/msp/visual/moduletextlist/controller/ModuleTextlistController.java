package com.jnetdata.msp.visual.moduletextlist.controller;

import com.jnetdata.msp.core.controller.BaseController;
import com.jnetdata.msp.visual.log.service.ElementService;
import com.jnetdata.msp.visual.moduletextlist.model.ModuleTextlist;
import com.jnetdata.msp.visual.moduletextlist.service.ModuleTextlistService;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.thenicesys.data.api.EntityId;
import org.thenicesys.web.JsonResult;


@RestController
@RequestMapping("/visual/moduletextlist")
@ApiModel(value = "ModuleTextlistController", description = "文字列表组件管理")
public class ModuleTextlistController extends BaseController<Long, ModuleTextlist> {
    @Autowired
    private ElementService elementService;
    @Autowired
    private ModuleTextlistService moduleTextlistService;
    @ApiOperation(value = "添加")
    @PostMapping
    public JsonResult<EntityId<Long>> add(@Validated @RequestBody ModuleTextlist entity) {
        elementService.AddLog("文字列表组件","添加文字列表组件");
        return getService().insertEntity(entity);
    }

    @ApiOperation(value = "删除")
    @DeleteMapping("/{id}")
    public JsonResult<Void> deleteById(@PathVariable("id") Long id) {
        elementService.AddLog("文字列表组件","删除文字列表组件");
        return getService().deleteEntity(id);
    }

    @ApiOperation(value = "修改")
    @PutMapping("/{id}")
    public JsonResult<Void> updateById(@PathVariable("id") Long id, @RequestBody ModuleTextlist entity) {
        elementService.AddLog("文字列表组件","修改文字列表组件");
        return getService().updateEntity(id, entity);
    }

    @ApiOperation(value = "查看")
    @GetMapping("/{id}")
    public JsonResult<ModuleTextlist> getById(@PathVariable("id") Long id) {
        elementService.AddLog("文字列表组件","查看文字列表组件");
        return this.getService().getEntity(id);
    }

    private ModuleTextlistService getService() {
        return this.moduleTextlistService;
    }

}

