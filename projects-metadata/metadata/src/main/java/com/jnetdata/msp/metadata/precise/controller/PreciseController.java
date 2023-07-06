package com.jnetdata.msp.metadata.precise.controller;

import com.jnetdata.msp.core.controller.BaseController;
import com.jnetdata.msp.metadata.Push.service.PushService;
import com.jnetdata.msp.metadata.precise.model.PreciseModel;
import com.jnetdata.msp.metadata.precise.service.PreciseService;
import com.jnetdata.msp.metadata.precise.vo.PreciseVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.thenicesys.data.api.EntityId;
import org.thenicesys.web.JsonResult;

import javax.annotation.Resource;

@Controller
@RequestMapping("/precise/tuisong")
@ApiModel(value = "精准推送", description = "精准推送")
public class PreciseController extends BaseController<Long,PreciseModel> {

    @Resource
    private PreciseService preciseService;

    @ApiOperation(value = "单位添加添加操作", notes="单位添加添加操作")
    @PostMapping("/add")
    @ResponseBody
    public JsonResult<EntityId<Long>> add(@RequestBody PreciseModel entity) {
        JsonResult<EntityId<Long>> entityIdJsonResult = doAdd(getService(), entity);
        return entityIdJsonResult;
    }

    @ApiOperation(value = "单位添加添加操作", notes="单位添加添加操作")
    @PostMapping("/addlist")
    @ResponseBody
    public JsonResult<EntityId<Long>> addli(@RequestBody PreciseModel entity) {
        return null;
    }

    /*@ApiOperation(value = "添加操作", notes="添加操作")
    @PostMapping("/addl")
    @ResponseBody
    public JsonResult<EntityId<Long>> addl(@RequestBody PreciseVo entity) {

      Boolean b =preciseService.addlist(entity);

        return null;
    }*/


    private PreciseService getService() {
        return preciseService;
    }

}
