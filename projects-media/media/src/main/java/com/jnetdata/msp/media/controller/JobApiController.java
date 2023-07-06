package com.jnetdata.msp.media.controller;

import com.jnetdata.msp.core.controller.BaseController;
import com.jnetdata.msp.media.model.JobApi;
import com.jnetdata.msp.media.service.JobApiService;
import com.jnetdata.msp.media.vo.JobApiVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.thenicesys.data.api.EntityId;
import org.thenicesys.data.api.Pager;
import org.thenicesys.web.JsonResult;

@Controller
@RequestMapping("/media/jobapi")
@Api(description = "定时API任务")
public class JobApiController extends BaseController<Long, JobApi> {
    @Autowired
    private JobApiService service;
    private JobApiService getService(){ return service; }

    @ApiOperation(value = "添加定时API任务", notes="添加定时API任务")
    @PostMapping
    @ResponseBody
    public JsonResult<EntityId<Long>> add(@RequestBody JobApi entity) {
        return doAdd(getService(), entity);
    }

    @ApiOperation(value = "删除", notes="删除指定id对应的定时API任务")
    @DeleteMapping(value = "/{id}")
    @ResponseBody
    public JsonResult<Void> deleteById(@PathVariable("id") Long id) {
        return doDeleteById(getService(), id);
    }

    @ApiOperation(value = "批量删除", notes="根据多个id删除定时API任务")
    @GetMapping("/delByIds")
    @ResponseBody
    public JsonResult deleteBatchIds(@RequestParam("ids") String ids) {
        return doDeleteBatchIds(getService(),ids);
    }


    @ApiOperation(value = "修改", notes="修改指定id对应的定时API任务(只需要填计划任务中的id)")
    @PutMapping(value = "/{id}")
    @ResponseBody
    public JsonResult<Void> updateById(@PathVariable("id") Long id,
                                       @RequestBody JobApi entity) {
        return doUpdateById(getService(), id, entity);
    }

    @ApiOperation(value = "根据id查询", notes="查看指定计划任务id对应的定时API任务")
    @GetMapping(value = "/{id}")
    @ResponseBody
    public JsonResult<JobApi> getById(@PathVariable("id") Long id) {
        return doGetById(getService(), id);
    }

    @ApiOperation(value = "根据实体属性查询", notes="根据vo查询")
    @PostMapping("/list")
    @ResponseBody
    public JsonResult<Pager<JobApi>> list(@RequestBody JobApiVo vo) {
        return doList(getService(), vo.getPager(), vo.getEntity());
    }
}
