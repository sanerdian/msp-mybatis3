package com.jnetdata.msp.metadata.metadata.controller;

import com.jnetdata.msp.core.controller.BaseController;
import com.jnetdata.msp.metadata.fieldinfo.model.Fieldinfo;
import com.jnetdata.msp.metadata.tableinfo.model.Tableinfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.thenicesys.web.JsonResult;

@Controller
@RequestMapping("/metadata")
@ApiModel(value = "元数据(MetadataController)", description = "元数据")
public class MetadataController extends BaseController<Long, Fieldinfo> {

    @PostMapping(value = "/generat")
    @ResponseBody
    @ApiOperation(value = "生成代码")
    public JsonResult getList(@RequestBody Tableinfo vo) {
        String path = System.getProperty("user.dir"); //D:\\tomcat-8.5.38\\bin
        return renderSuccess(path);
    }

}
