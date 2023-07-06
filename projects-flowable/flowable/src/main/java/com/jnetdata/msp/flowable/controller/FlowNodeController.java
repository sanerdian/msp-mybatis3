package com.jnetdata.msp.flowable.controller;

import com.jnetdata.msp.flowable.service.common.FlowNodeService;
import com.jnetdata.msp.flowable.vo.ProcessVo;
import com.jnetdata.msp.flowable.vo.StepVo;
import com.jnetdata.msp.vo.BaseVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.thenicesys.data.api.Pager;
import org.thenicesys.web.JsonResult;

import java.util.List;

/**
 * 工作流节点
 */
@Slf4j
@RestController
@RequestMapping("/flowable/node")
@Api(tags = "工作流节点(FlowNodeController)")
public class FlowNodeController {

    @Autowired
    private FlowNodeService flowNodeService;

    @PostMapping(value = "/getNextNode")
    @ApiOperation(value = "获取下一节点信息")
    public JsonResult getNextNode(@RequestBody String data) {
        return flowNodeService.getNextNode(data);
    }

    @PostMapping(value = "/operateList")
    @ApiOperation(value = "操作列表")
    public JsonResult operateList(@RequestBody BaseVo<ProcessVo> vo){
        return flowNodeService.operateList(vo);
    }

    @PostMapping(value = "/nodeList")
    @ApiOperation(value = "节点列表")
    public JsonResult nodeList(@RequestBody ProcessVo vo){
        return flowNodeService.nodeList(vo);
    }

    @PostMapping(value = "/stepList")
    @ApiOperation(value = "流程步骤列表")
    public JsonResult<Pager<StepVo>> stepList(@RequestBody BaseVo<StepVo> vo){
        return flowNodeService.stepList(vo);
    }
}
