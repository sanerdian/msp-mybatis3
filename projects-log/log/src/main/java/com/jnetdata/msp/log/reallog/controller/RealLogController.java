package com.jnetdata.msp.log.reallog.controller;

import com.jnetdata.msp.constants.WebPathConstant;
import com.jnetdata.msp.log.reallog.service.RealLogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.thenicesys.web.JsonResult;

import java.util.List;

/**
 * Created by TF on 2019/3/28.
 */

@Controller
@RequestMapping("/log/realLog")
@ApiModel(value = "ModelLogController", description = "实时日志")
@Api(tags = "实时日志(ModelLogController)")
public class RealLogController {
    @Autowired
    RealLogService service;

    private static final String BASE_URL = "/log/reallog";


    @GetMapping("/index")
    public String toList() {
        return pageFilePath("/list");
    }

    @ApiOperation("获取日志")
    @GetMapping("/getLog")
    @ResponseBody
    public JsonResult getLog(@RequestParam("pointer") int pointer){
        return JsonResult.renderSuccess(service.getLog(pointer));
    }

    @ApiOperation("读日志文件")
    @GetMapping("/readLog")
    @ResponseBody
    public JsonResult<List<String>> readLog(){
        JsonResult jsonResult=null;
        try {
            jsonResult=new JsonResult();
            List<String> strings = service.readLog();
            jsonResult.setObj(strings);
            jsonResult.setMsg("读取成功");
            return jsonResult;
        } catch (Exception e) {
            e.printStackTrace();
            return jsonResult;
        }

    }

    private String pageFilePath(String path) {
        return webPath(BASE_URL, path);
    }


    protected String webPath(String baseUrl, String url) {
        return WebPathConstant.getHtmlFilePath(baseUrl + url);
    }


}
