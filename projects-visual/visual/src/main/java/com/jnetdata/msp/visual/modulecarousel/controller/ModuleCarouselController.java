package com.jnetdata.msp.visual.modulecarousel.controller;

import com.jnetdata.msp.core.controller.BaseController;
import com.jnetdata.msp.visual.log.service.ElementService;
import com.jnetdata.msp.visual.modulecarousel.model.ModuleCarousel;
import com.jnetdata.msp.visual.modulecarousel.service.ModuleCarouselService;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.thenicesys.data.api.EntityId;
import org.thenicesys.web.JsonResult;


@RestController
@RequestMapping("/visual/modulecarousel")
@ApiModel(value = "ModuleCarouselController", description = "轮播组件管理")
public class ModuleCarouselController extends BaseController<Long, ModuleCarousel> {
    @Autowired
    private ElementService elementService;
    @Autowired
    private ModuleCarouselService moduleCarouselService;
    @ApiOperation(value = "添加")
    @PostMapping
    public JsonResult<EntityId<Long>> add(@Validated @RequestBody ModuleCarousel entity) {
        elementService.AddLog("轮播组件","添加轮播组件");
        return getService().insertEntity(entity);
    }

    @ApiOperation(value = "删除")
    @DeleteMapping("/{id}")
    public JsonResult<Void> deleteById(@PathVariable("id") Long id) {
        elementService.AddLog("轮播组件","删除轮播组件");
        return getService().deleteEntity(id);
    }

    @ApiOperation(value = "修改")
    @PutMapping("/{id}")
    public JsonResult<Void> updateById(@PathVariable("id") Long id, @RequestBody ModuleCarousel entity) {
        elementService.AddLog("轮播组件","修改轮播组件");
        return getService().updateEntity(id, entity);
    }

    @ApiOperation(value = "查看")
    @GetMapping("/{id}")
    public JsonResult<ModuleCarousel> getById(@PathVariable("id") Long id) {
        elementService.AddLog("轮播组件","查看轮播组件");
        return this.getService().getEntity(id);
    }

    private ModuleCarouselService getService() {
        return this.moduleCarouselService;
    }

}

