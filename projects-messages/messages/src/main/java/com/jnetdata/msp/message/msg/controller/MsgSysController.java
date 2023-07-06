package com.jnetdata.msp.message.msg.controller;

import com.jnetdata.msp.config.config.model.ConfigModel;
import com.jnetdata.msp.config.config.service.ConfigModelService;
import com.jnetdata.msp.core.controller.BaseController;
import com.jnetdata.msp.message.msg.model.Msg;
import com.jnetdata.msp.vo.BaseVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.thenicesys.web.JsonResult;

import java.util.*;

/**
 * Created by TF on 2019/3/14.
 */
@Controller
@RequestMapping("/message/system")
@ApiModel(value = "MsgController", description = "消息管理")
public class MsgSysController extends BaseController<Long, Msg> {

    @Autowired
    private ConfigModelService systemService;

    @ApiOperation(value = "查询", notes="根据vo查询")
    @PostMapping("/getSys")
    @ResponseBody
    public JsonResult getSys(@RequestBody BaseVo<ConfigModel> vo) {
        return renderSuccess(systemService.getSystem(vo.getEntity()));
    }

}
