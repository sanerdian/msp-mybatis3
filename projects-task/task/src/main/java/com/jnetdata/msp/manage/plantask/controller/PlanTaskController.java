package com.jnetdata.msp.manage.plantask.controller;

import com.jnetdata.msp.core.controller.BaseController;
import com.jnetdata.msp.manage.plantask.model.PlanTask;
import com.jnetdata.msp.manage.plantask.service.PlanTaskService;
import com.jnetdata.msp.manage.plantask.vo.PlanTaskVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.thenicesys.data.api.EntityId;
import org.thenicesys.data.api.Pager;
import org.thenicesys.web.JsonResult;

import java.util.List;

@Controller
@RequestMapping("/task/planTask")
@ApiModel(value = "PlanTaskController", description = "计划任务配置")
public class PlanTaskController extends BaseController<Long,PlanTask> {
    private static final String BASE_URL = "/manage/planTask";

    @Autowired
    private PlanTaskService service;


    @ApiOperation(value = "添加计划任务信息", notes="添加计划任务信息")
    @PostMapping
    @ResponseBody
    public JsonResult<EntityId<Long>> add(@RequestBody PlanTask entity) {
        return doAdd(getService(), entity);
    }

    @ApiOperation(value = "删除", notes="删除指定id对应的计划任务信息")
    @DeleteMapping(value = "/{id}")
    @ResponseBody
    public JsonResult<Void> deleteById(@ApiParam("用户id") @PathVariable("id") Long id) {
        return doDeleteById(getService(), id);
    }

    @ApiOperation(value = "批量删除", notes="根据多个id删除计划任务信息")
    @GetMapping("/delByIds")
    @ResponseBody
    public JsonResult deleteBatchIds(@RequestParam("ids") String ids) {
        return doDeleteBatchIds(getService(),ids);
    }


    @ApiOperation(value = "修改", notes="修改指定id对应的计划任务信息(只需要填计划任务中的id)")
    @PutMapping(value = "/{id}")
    @ResponseBody
    public JsonResult<Void> updateById(@ApiParam("用户id") @PathVariable("id") Long id,
                                       @RequestBody PlanTask entity) {
        return doUpdateById(getService(), id, entity);
    }

    @ApiOperation(value = "查看", notes="查看指定计划任务id对应的计划任务信息")
    @GetMapping(value = "/{id}")
    @ResponseBody
    public JsonResult<PlanTask> getById(@ApiParam("计划任务信息id") @PathVariable("id") Long id) {
        return doGetById(getService(), id);
    }

    @ApiOperation(value = "查询", notes="根据vo查询")
    @PostMapping("/list")
    @ResponseBody
    public JsonResult<Pager<PlanTask>> list(@RequestBody PlanTaskVo vo) {
        return doList(getService(), vo.getPager(), vo.getEntity());
    }


    @ApiOperation(value = "查询", notes="根据vo查询")
    @PostMapping("/getPlanTasks")
    @ResponseBody
    public JsonResult getPlanTasks(@RequestBody PlanTaskVo vo) {
        JsonResult result=new JsonResult();
        List<PlanTask> planTask = service.getPlanTask(vo.getEntity());
        result.setObj(planTask);
        result.setMsg("查询成功");
        return result;
    }

    /**
     *
     * @return
     */
    @GetMapping("/index")
    public String toList() {
        return pageFilePath("/list");
    }


    private String pageFilePath(String path) {
        return super.webPath(BASE_URL, path);
    }

    private PlanTaskService getService() {
        return service;
    }




}
