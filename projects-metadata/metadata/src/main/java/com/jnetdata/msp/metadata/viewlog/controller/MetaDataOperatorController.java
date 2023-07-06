package com.jnetdata.msp.metadata.viewlog.controller;

import com.jnetdata.msp.core.controller.BaseController;
import com.jnetdata.msp.metadata.viewlog.model.MetaDataOperator;
import com.jnetdata.msp.metadata.viewlog.service.impl.MetaDataOperatorServiceimpl;
import com.jnetdata.msp.metadata.vo.BaseVo;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.thenicesys.data.api.Pager;
import org.thenicesys.web.JsonResult;

/**
 * 功能描述：
 */
@Controller
@RequestMapping("/operator")
public class MetaDataOperatorController extends BaseController {
    @Autowired
    private MetaDataOperatorServiceimpl metaDataOperatorService;
    @ApiOperation("查询日志")
    @PostMapping("/list")
    @ResponseBody
    public JsonResult<Pager<MetaDataOperator>> getList(@RequestBody BaseVo<MetaDataOperator> vo){
        return doList(getService(), vo.getPager(), vo.getEntity());
    }
    private MetaDataOperatorServiceimpl getService(){
        return metaDataOperatorService;
    }
}
