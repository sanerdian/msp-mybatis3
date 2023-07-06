package com.jnetdata.msp.media.controller;

import com.jnetdata.msp.media.service.YthService;
import com.jnetdata.msp.media.vo.OptionsVo;
import com.jnetdata.msp.media.vo.TreeVo;
import com.jnetdata.msp.tlujy.xinwen020.model.Xinwen020;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.thenicesys.web.JsonResult;

import java.util.List;

@Controller
@RequestMapping("/media/yth/")
@Api(description = "给一体化写的接口")
public class YthController {

    @Autowired
    private YthService service;
    private YthService getService(){ return service; }

    @ApiOperation(value = "获取劳资单位树", notes="获取劳资单位树")
    @GetMapping("/deptTree")
    @ResponseBody
    public JsonResult<TreeVo> deptTree() {
        return JsonResult.success(service.deptTree());
    }


}
