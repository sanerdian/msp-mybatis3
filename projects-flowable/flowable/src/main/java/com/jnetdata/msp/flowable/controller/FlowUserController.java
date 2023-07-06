package com.jnetdata.msp.flowable.controller;

import com.jnetdata.msp.flowable.service.common.UserService;
import com.jnetdata.msp.flowable.vo.NewsVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.thenicesys.web.JsonResult;

/**
 * 工作流用户
 */
@Slf4j
@RestController
@RequestMapping("/flowable/user")
@Api(tags = "工作流用户(FlowUserController)")
public class FlowUserController {

    @Autowired
    private UserService userService;

    @PostMapping(value = "/auditorList")
    @ApiOperation(value = "审核人列表")
    public JsonResult auditorList(@RequestBody NewsVo vo){
        return userService.auditorList(vo);
    }
}
