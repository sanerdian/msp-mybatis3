package com.jnetdata.msp.flowable.controller;

import com.jnetdata.msp.flowable.model.FlowFocus;
import com.jnetdata.msp.flowable.model.FlowSuspend;
import com.jnetdata.msp.flowable.service.FlowFocusService;
import com.jnetdata.msp.flowable.service.FlowSuspendService;
import com.jnetdata.msp.flowable.vo.ProcessVo;
import com.jnetdata.msp.flowable.vo.WorkVo;
import com.jnetdata.msp.vo.BaseVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.thenicesys.data.api.Pager;
import org.thenicesys.web.JsonResult;

@RestController
@RequestMapping("/flowable/flowsuspend")
@Api(tags = "流程挂起(FlowSuspendController)")
public class FlowSuspendController {

    @Autowired
    private FlowSuspendService flowSuspendService;

    @PostMapping("/suspend")
    @ApiOperation(value = "挂起流程实例")
    public JsonResult<String> suspend(@RequestBody FlowSuspend entity){
        return flowSuspendService.suspend(entity);
    }

    @PostMapping("/activate")
    @ApiOperation(value = "激活流程实例")
    public JsonResult<String> activate(@RequestBody FlowSuspend entity){
        return flowSuspendService.activate(entity);
    }

    @PostMapping("/mySuspend")
    @ApiOperation(value = "我挂起的")
    public JsonResult<Pager<WorkVo>> mySuspend(@RequestBody BaseVo<WorkVo> vo) {
        return flowSuspendService.mySuspend(vo);
    }
}
