package com.jnetdata.msp.manage.template.controller;

import com.jnetdata.msp.core.controller.BaseController;
import com.jnetdata.msp.manage.template.model.TableTemplate;
import com.jnetdata.msp.manage.template.model.Template;
import com.jnetdata.msp.manage.template.service.TableTemplateService;
import com.jnetdata.msp.manage.template.service.TemplateService;
import com.jnetdata.msp.manage.template.vo.TableTemplateVo;
import com.jnetdata.msp.manage.template.vo.TemplateVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.thenicesys.data.api.Pager;
import org.thenicesys.web.JsonResult;

import java.util.List;

@Controller
@RequestMapping("/manage/tableTemplate")
@ApiModel(value = "元数据模板管理(TableTemplateController)", description = "元数据模板管理")
@Api(tags = "元数据模板管理(TableTemplateController)")
public class TableTemplateController extends BaseController<Long,Template> {

    @Autowired
    private TableTemplateService service;

    private TableTemplateService getService(){
        return  service;
    }

    @ApiOperation(value = "查询", notes="根据vo查询")
    @PostMapping("/getTemp")
    @ResponseBody
    public JsonResult getTemp(@RequestBody TableTemplateVo vo) {
        JsonResult result=new JsonResult();
        List<TableTemplate> templates = service.getTemplates(vo.getEntity());
        result.setObj(templates);
        return  result;
    }
}
