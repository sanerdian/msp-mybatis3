package com.jnetdata.msp.flowable.controller;

import com.jnetdata.msp.core.controller.BaseController;
import com.jnetdata.msp.flowable.model.MetaPub;
import com.jnetdata.msp.flowable.service.FlowMetadataService;
import com.jnetdata.msp.flowable.service.common.*;
import com.jnetdata.msp.flowable.vo.*;
import com.jnetdata.msp.vo.BaseVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiOperation;
import org.flowable.ui.modeler.repository.ModelRepository;
import org.flowable.ui.modeler.service.FlowableModelQueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.thenicesys.data.api.Pager;
import org.thenicesys.web.JsonResult;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 历史接口，现在已经全部分散到其它接口中
 */
@Controller
@RequestMapping("/flowable/")
@ApiModel(value = "FlowableController", description = "工作流")
@Api(tags = "工作流(FlowableController)")
public class FlowableController extends BaseController {

    @Autowired
    protected FlowableModelQueryService modelQueryService;
    @Autowired
    protected ModelRepository modelRepository;
    @Autowired
    private FlowModelService flowModelService;
    @Autowired
    private FlowProcessService flowProcessService;
    @Autowired
    private FlowTaskService flowTaskService;
    @Autowired
    private UserService userService;
    @Autowired
    private FlowNodeService flowNodeService;
    @Autowired
    private FlowMetadataService flowMetadataService;
    @Autowired
    private ProcessStartService processStartService;

    @PostMapping(value = "/procModelList")
    @ResponseBody
    @ApiOperation(value = "流程模型列表")
    public JsonResult procModelList(@RequestBody BaseVo<ModelVo> vo){
        return flowModelService.procModelList(vo);
    }

    @PostMapping(value = "/deploy")
    @ResponseBody
    @ApiOperation(value = "部署流程")
    public JsonResult deploy(@RequestBody ModelVo vo) {
        return flowModelService.deploy(vo);
    }

    @PostMapping(value = "/process")
    @ResponseBody
    @ApiOperation(value = "查询流程定义列表")
    public JsonResult<List<ProcessDefiVo>> process(ProcessDefiVo vo) {
        return flowProcessService.process(vo);
    }

    @PostMapping(value = "/procDefiList")
    @ResponseBody
    @ApiOperation(value = "查询流程定义列表(分页)")
    public JsonResult<Pager<ProcessDefiVo>> procDefiList(@RequestBody BaseVo<ProcessDefiVo> vo) {
        return flowProcessService.procDefiList(vo);
    }

    @PostMapping(value = "/startAndSubmit")
    @ResponseBody
    @ApiOperation(value = "启动流程并提交")
    public JsonResult startAndSubmit(@RequestBody ProcessStartVo vo) {
        return processStartService.startAndSubmit(vo);
    }

    @PostMapping(value = "/completeTask")
    @ResponseBody
    @ApiOperation(value = "完成任务")
    public JsonResult completeTask(@RequestBody TaskVo vo) {
        return flowTaskService.completeTask(vo);
    }

    @GetMapping(value = "/getImg")
    @ResponseBody
    @ApiOperation(value = "查看流程图")
    public void getProcessImage(String procInstId, String formId,  HttpServletResponse response) {
        flowProcessService.getProcessImage(procInstId, formId, response);
    }

    @PostMapping(value = "/transferTask")
    @ResponseBody
    @ApiOperation(value = "转办任务")
    public JsonResult transferTask(@RequestBody String data) {
        return flowTaskService.transferTask(data);
    }

    @PostMapping(value = "/rollbackTask")
    @ResponseBody
    @ApiOperation(value = "驳回任务")
    public JsonResult rollbackTask(@RequestBody String data) {
        return flowTaskService.rollbackTask(data);
    }

    @PostMapping(value = "/toFirst")
    @ResponseBody
    @ApiOperation(value = "驳回任务到第一环节")
    public JsonResult toFirst(@RequestBody TaskVo vo) {
        return flowTaskService.toFirst(vo);
    }

    @PostMapping(value = "/getNextNode")
    @ResponseBody
    @ApiOperation(value = "获取下一节点信息")
    public JsonResult getNextNode(@RequestBody String data) {
        return flowNodeService.getNextNode(data);
    }

    @PostMapping(value = "/operateList")
    @ResponseBody
    @ApiOperation(value = "操作列表")
    public JsonResult operateList(@RequestBody BaseVo<ProcessVo> vo){
        return flowNodeService.operateList(vo);
    }

    @PostMapping(value = "/nodeList")
    @ResponseBody
    @ApiOperation(value = "节点列表")
    public JsonResult nodeList(@RequestBody ProcessVo vo){
        return flowNodeService.nodeList(vo);
    }

    @GetMapping(value = "/latestProc")
    @ResponseBody
    @ApiOperation(value = "根据流程key获取最新版本的流程信息")
    public JsonResult latestProc(String procKey){
        return flowProcessService.latestProc(procKey);
    }

    @PostMapping(value = "/terminate")
    @ResponseBody
    @ApiOperation(value = "终止流程")
    public JsonResult terminate(@RequestBody ProcessVo vo){
        return flowProcessService.terminate(vo);
    }

    @PostMapping(value = "/auditorList")
    @ResponseBody
    @ApiOperation(value = "审核人列表")
    public JsonResult auditorList(@RequestBody NewsVo vo){
        return userService.auditorList(vo);
    }

    @GetMapping(value = "/getMetaPub")
    @ResponseBody
    @ApiOperation(value = "获取元数据公共信息")
    public JsonResult<MetaPub> getMetaPub(String procInstId){
        return JsonResult.success(flowMetadataService.getMetadata(procInstId));
    }
}
