package com.jnetdata.msp.config.systemmsg.controller;

import com.jnetdata.msp.core.controller.BaseController;
import com.jnetdata.msp.config.systemmsg.model.SystemMsg;
import com.jnetdata.msp.config.systemmsg.service.SystemMsgService;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.thenicesys.web.JsonResult;

import java.util.Map;

/**
 * Created by TF on 2019/3/27.
 */

@Controller
@RequestMapping("/config/systemMsg")
@ApiModel(value = "SystemMsgController", description = "系统信息")
public class SystemMsgController extends BaseController<Long,SystemMsg>{
    private static final String BASE_URL = "/manage/systemmsg";
    @Autowired
    private SystemMsgService service;


    @ApiOperation(value = "获取信息", notes="获取系统信息")
    @GetMapping("")
    @ResponseBody
    public JsonResult<Map<String,Object>> getSystem() throws Exception{
        return  renderSuccess(getService().getSystemMsg());
    }


    private SystemMsgService getService() {
        return service;
    }

    @GetMapping("/index")
    public String toList() {
        return pageFilePath("/list");
    }

    private String pageFilePath(String path) {
        return super.webPath(BASE_URL, path);
    }
}
