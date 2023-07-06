package com.jnetdata.msp.flowable.controller;

import com.jnetdata.msp.flowable.model.MetaPub;
import com.jnetdata.msp.flowable.service.common.FlowProcessService;
import com.jnetdata.msp.flowable.service.common.ProcessStartService;
import com.jnetdata.msp.flowable.vo.ProcessDefiVo;
import com.jnetdata.msp.flowable.vo.ProcessStartVo;
import com.jnetdata.msp.flowable.vo.ProcessVo;
import com.jnetdata.msp.vo.BaseVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.thenicesys.data.api.Pager;
import org.thenicesys.web.JsonResult;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 工作流流程
 */
@Slf4j
@RestController
@RequestMapping("/flowable/process")
@Api(tags = "工作流流程(FlowProcessController)")
public class FlowProcessController {

    @Autowired
    private FlowProcessService flowProcessService;
    @Autowired
    private ProcessStartService processStartService;

    @GetMapping(value = "/getProcessImage")
    @ApiOperation(value = "获取流程图")
    public void getProcessImage(String procInstId, String formId, HttpServletResponse response) {
        flowProcessService.getProcessImage(procInstId, formId, response);
    }

    @PostMapping(value = "/all")
    @ApiOperation(value = "查询流程定义列表")
    public JsonResult<List<ProcessDefiVo>> process(@RequestBody ProcessDefiVo vo) {
        return flowProcessService.process(vo);
    }

    @PostMapping(value = "/procDefiList")
    @ApiOperation(value = "查询流程定义列表(分页)")
    public JsonResult<Pager<ProcessDefiVo>> procDefiList(@RequestBody BaseVo<ProcessDefiVo> vo) {
        return flowProcessService.procDefiList(vo);
    }

    @PostMapping(value = "/start")
    @ApiOperation(value = "启动流程")
    public JsonResult<MetaPub> start(@RequestBody ProcessStartVo vo) {
        return processStartService.start(vo);
    }

    @PostMapping(value = "/startAndSubmit")
    @ApiOperation(value = "启动流程并提交")
    public JsonResult<MetaPub> startAndSubmit(@RequestBody ProcessStartVo vo) {
        return processStartService.startAndSubmit(vo);
    }

    @GetMapping(value = "/latestProc")
    @ApiOperation(value = "根据流程key获取最新版本的流程信息")
    public JsonResult latestProc(String procKey){
        return flowProcessService.latestProc(procKey);
    }

    @PostMapping(value = "/terminate")
    @ApiOperation(value = "终止流程")
    public JsonResult terminate(@RequestBody ProcessVo vo){
        return flowProcessService.terminate(vo);
    }


}
