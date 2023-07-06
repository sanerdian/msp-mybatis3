package com.jnetdata.msp.visual.moduledetail.controller;

import com.jnetdata.msp.core.controller.BaseController;
import com.jnetdata.msp.visual.log.service.ElementService;
import com.jnetdata.msp.visual.moduledetail.model.ModuleDetail;
import com.jnetdata.msp.visual.moduledetail.service.ModuleDetailService;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.thenicesys.data.api.EntityId;
import org.thenicesys.web.JsonResult;

@RestController
@RequestMapping("/visual/moduledetail")
@ApiModel(value = "ModuleDetailController", description = "详情组件管理")
public class ModuleDetailController extends BaseController<Long, ModuleDetail> {
    @Autowired
    private ElementService elementService;
    @Autowired
    private ModuleDetailService moduleDetailService;
    @ApiOperation(value = "添加")
    @PostMapping
    public JsonResult<EntityId<Long>> add(@Validated @RequestBody ModuleDetail entity) {
        elementService.AddLog("详情组件","添加详情组件");
        return getService().insertEntity(entity);
    }

    @ApiOperation(value = "删除")
    @DeleteMapping("/{id}")
    public JsonResult<Void> deleteById(@PathVariable("id") Long id) {
        elementService.AddLog("详情组件","删除详情组件");
        return getService().deleteEntity(id);
    }

    @ApiOperation(value = "修改")
    @PutMapping("/{id}")
    public JsonResult<Void> updateById(@PathVariable("id") Long id, @RequestBody ModuleDetail entity) {
        elementService.AddLog("详情组件","修改详情组件");
        return getService().updateEntity(id, entity);
    }

    @ApiOperation(value = "查看")
    @GetMapping("/{id}")
    public JsonResult<ModuleDetail> getById(@PathVariable("id") Long id) {
        elementService.AddLog("详情组件","查看详情组件");
        return this.getService().getEntity(id);
    }

    private ModuleDetailService getService() {
        return this.moduleDetailService;
    }

}

