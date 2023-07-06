package com.jnetdata.msp.flowable.controller.feiyi;

import com.jnetdata.msp.flowable.service.feiyi.WorkbenchService;
import com.jnetdata.msp.flowable.vo.feiyi.HistoryVo;
import com.jnetdata.msp.flowable.vo.feiyi.SubmitVo;
import com.jnetdata.msp.flowable.vo.feiyi.TodoVo;
import com.jnetdata.msp.vo.BaseVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.thenicesys.data.api.Pager;
import org.thenicesys.web.JsonResult;

/**
 * 工作台
 * 我的待办、我的已办、我的提交
 */
@RestController
@RequestMapping("/flowable/workbench")
@Api(tags = "工作台(WorkbenchController)")
public class WorkbenchController {

    @Autowired
    private WorkbenchService workbenchService;

    @PostMapping(value = "/taskList")
    @ApiOperation(value = "我的待办")
    public JsonResult<Pager<TodoVo>> taskList(@RequestBody BaseVo<TodoVo> vo) {
        return workbenchService.taskList(vo);
    }

    @PostMapping(value = "/historyList")
    @ApiOperation(value = "我的已办")
    public JsonResult<Pager<HistoryVo>> historyList(@RequestBody BaseVo<HistoryVo> vo) {
        return workbenchService.historyList(vo);
    }

    @PostMapping(value = "/submitList")
    @ApiOperation(value = "我的提交")
    public JsonResult<Pager<SubmitVo>> submitList(@RequestBody BaseVo<SubmitVo> vo) {
        return workbenchService.submitList(vo);
    }
}
