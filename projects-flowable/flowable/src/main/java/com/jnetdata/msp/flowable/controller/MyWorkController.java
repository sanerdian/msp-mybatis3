package com.jnetdata.msp.flowable.controller;

import com.jnetdata.msp.flowable.service.common.MyWorkService;
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
@RequestMapping("/flowable/mywork")
@Api(tags = "我的工作(MyWorkController)")
public class MyWorkController {

    @Autowired
    private MyWorkService myWorkService;

    @PostMapping(value = "/myTodo")
    @ApiOperation(value = "我的待办")
    public JsonResult<Pager<WorkVo>> myTodo(@RequestBody BaseVo<WorkVo> vo) {
        return myWorkService.myTodo(vo);
    }

    @PostMapping(value = "/myDone")
    @ApiOperation(value = "我的已办")
    public JsonResult<Pager<WorkVo>> myDone(@RequestBody BaseVo<WorkVo> vo) {
        return myWorkService.myDone(vo);
    }

}
