package com.jnetdata.msp.flowable.controller;

import com.jnetdata.msp.flowable.enums.AuditStatus;
import com.jnetdata.msp.flowable.service.common.FlowTaskService;
import com.jnetdata.msp.flowable.vo.TaskVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.thenicesys.web.JsonResult;

/**
 * 工作流任务
 */
@Slf4j
@RestController
@RequestMapping("/flowable/task")
@Api(tags = "工作流任务(FlowTaskController)")
public class FlowTaskController {

    @Autowired
    private FlowTaskService flowTaskService;

    @PostMapping(value = "/completeTask")
    @ApiOperation(value = "完成任务")
    public JsonResult completeTask(@RequestBody TaskVo vo) {
        return flowTaskService.completeTask(vo);
    }

    @PostMapping(value = "/completeTaskBatch")
    @ApiOperation(value = "批量完成任务")
    public JsonResult completeTaskBatch(@RequestBody TaskVo vo) {
        return flowTaskService.completeTaskBatch(vo);
    }

    @PostMapping(value = "/toFirst")
    @ApiOperation(value = "驳回任务到第一环节")
    public JsonResult toFirst(@RequestBody TaskVo vo) {
        return flowTaskService.toFirst(vo);
    }

    @PostMapping(value = "/toFirstBatch")
    @ApiOperation(value = "批量驳回任务到第一环节")
    public JsonResult toFirstBatch(@RequestBody TaskVo vo) {
        return flowTaskService.toFirstBatch(vo);
    }

    @PostMapping(value = "/takeWork")
    @ApiOperation(value = "认领任务")
    public JsonResult takeWork(@RequestBody TaskVo vo) {
        return flowTaskService.takeWork(vo);
    }

    @PostMapping(value = "/transferTask")
    @ApiOperation(value = "驳回任务到上一个节点")
    public JsonResult transferTask(@RequestBody String data) {
        return flowTaskService.transferTask(data);
    }

    @PostMapping(value = "/rollbackTask")
    @ApiOperation(value = "驳回任务")
    public JsonResult rollbackTask(@RequestBody String data) {
        return flowTaskService.rollbackTask(data);
    }

    @PostMapping(value = "/recallTask")
    @ApiOperation(value = "撤回任务")
    public JsonResult recallTask(@RequestBody String data) {
        return flowTaskService.recallTask(data,  AuditStatus.RECALL.getCode());
    }
}
