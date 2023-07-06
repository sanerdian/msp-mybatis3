package com.jnetdata.msp.visual.moduleform.controller;

import com.jnetdata.msp.core.controller.BaseController;
import com.jnetdata.msp.visual.log.service.ElementService;
import com.jnetdata.msp.visual.moduleform.model.ModuleForm;
import com.jnetdata.msp.visual.moduleform.service.ModuleFormService;
import com.jnetdata.msp.vo.BaseVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.thenicesys.data.api.EntityId;
import org.thenicesys.data.api.Pager;
import org.thenicesys.web.JsonResult;


@RestController
@RequestMapping("/visual/moduleform")
@ApiModel(value = "ModuleFormController", description = "表单组件管理")
public class ModuleFormController extends BaseController<Long, ModuleForm> {
    @Autowired
    private ElementService elementService;
    @Autowired
    private ModuleFormService moduleFormService;
    @ApiOperation(value = "添加")
    @PostMapping
    public JsonResult<EntityId<Long>> add(@Validated @RequestBody ModuleForm entity) {
        elementService.AddLog("表单组件管理","添加表单组件管理");
        return getService().insertEntity(entity);
    }

    @ApiOperation(value = "删除")
    @DeleteMapping("/{id}")
    public JsonResult<Void> deleteById(@PathVariable("id") Long id) {
        elementService.AddLog("表单组件管理","删除表单组件管理");
        return getService().deleteEntity(id);
    }

    @ApiOperation(value = "修改")
    @PutMapping("/{id}")
    public JsonResult<Void> updateById(@PathVariable("id") Long id, @RequestBody ModuleForm entity) {
        elementService.AddLog("表单组件管理","修改表单组件管理");
        return getService().updateEntity(id, entity);
    }

    @ApiOperation(value = "查看")
    @GetMapping("/{id}")
    public JsonResult<ModuleForm> getById(@PathVariable("id") Long id) {
        elementService.AddLog("表单组件管理","查看表单组件管理");
        return this.getService().getEntity(id);
    }

    @ApiOperation(value = "列表查询")
    @PostMapping("/listing")
    public JsonResult<Pager<ModuleForm>> listing(@RequestBody BaseVo<ModuleForm> vo) {
        elementService.AddLog("表单组件管理","列表查询表单组件管理");
        return doList(this.getService(), vo.getPager(), vo.getEntity());
    }

    private ModuleFormService getService() {
        return this.moduleFormService;
    }

}

