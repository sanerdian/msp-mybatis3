package com.jnetdata.msp.visual.log.Controller;

import com.jnetdata.msp.core.controller.BaseController;
import com.jnetdata.msp.log.template.model.TemplateLog;
import com.jnetdata.msp.log.template.service.TemplateLogService;
import com.jnetdata.msp.log.template.vo.TemplateLogVo;
import com.jnetdata.msp.metadata.encoding.model.Encodingmodel;
import com.jnetdata.msp.visual.log.model.ElementModel;
import com.jnetdata.msp.visual.log.service.ElementService;
import com.jnetdata.msp.vo.BaseVo;
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

import javax.annotation.Resource;

/**
 * 功能描述：
 */
@Controller
@RequestMapping("/log/element")
@ApiModel(value = "ElementController", description = "组件操作日志")
public class ElementController  extends BaseController{

    @Resource
    private ElementService elementService;

    @ApiOperation(value = "根据实体属性查询", notes="根据vo查询")
    @PostMapping("/list")
    @ResponseBody
    public JsonResult<Pager<ElementModel>> list(@RequestBody BaseVo<ElementModel> vo) {
        return doList(getService(), vo.getPager(), vo.getEntity());
    }

    private ElementService getService() {
        return elementService;
    }
}
