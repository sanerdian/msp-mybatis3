package com.jnetdata.msp.generator.controller.component.controller;

import com.jnetdata.msp.core.controller.BaseController;
import com.jnetdata.msp.generator.controller.component.model.Component;
import com.jnetdata.msp.generator.controller.component.service.ComponentService;
import com.jnetdata.msp.vo.BaseVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.thenicesys.data.api.EntityId;
import org.thenicesys.data.api.Pager;
import org.thenicesys.web.JsonResult;

import java.util.Arrays;

@Controller
@RequestMapping("/metadata/Component")
@ApiModel(value = "ComponentController" , description = "模块管理")
@Api(tags = "模块管理(ComponentController)")
public class ComponentController extends BaseController<Long , Component> {

    @Autowired
    private ComponentService componentService;

    @ApiOperation(value = "添加字段")
    @PostMapping(value = "/add")
    @ResponseBody
    public JsonResult<EntityId<Long>> add(@RequestBody Component entity) {
        return doAdd(getService(), entity);
    }

    @PostMapping(value = "/list")
    @ResponseBody
    @ApiOperation(value = "模块列表")
    public JsonResult<Pager<Component>> getList(@RequestBody BaseVo<Component> vo){
        return doList(getService(),vo.getPager(),vo.getEntity());
    }


    @ApiOperation(value = "查看", notes="查看模块")
    @GetMapping(value = "/{id}")
    @ResponseBody
    public JsonResult<Component> getById(@ApiParam("模块id") @PathVariable("id") Long id) {
        return doGetById(getService(), id);
    }

    @ApiOperation(value = "修改", notes="修改模块")
    @PutMapping(value = "/{id}")
    @ResponseBody
    public JsonResult<Void> updateById(@ApiParam("模块id") @PathVariable("id") Long id,
                                       @RequestBody Component entity) {
        return doUpdateAllColumnById(getService(), id, entity);
    }

    @PostMapping(value = "delByIds")
    @ResponseBody
    @ApiOperation(value = "批量删除" , notes = "根据多个id删除对应模块数据")
    public JsonResult deleteBatchIds(@RequestBody String[] ids){
        return doDeleteBatchIds(getService(), Arrays.asList(ids));
    }

    private ComponentService getService(){
        return componentService;
    }

}
