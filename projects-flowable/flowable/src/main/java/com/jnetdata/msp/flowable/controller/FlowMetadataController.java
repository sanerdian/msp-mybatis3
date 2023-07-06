package com.jnetdata.msp.flowable.controller;

import com.jnetdata.msp.flowable.model.FlowMetadata;
import com.jnetdata.msp.flowable.model.MetaPub;
import com.jnetdata.msp.flowable.service.FlowMetadataService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.thenicesys.web.JsonResult;

@RestController
@RequestMapping("/flowable/metadata/")
@ApiModel(value = "MetadataController", description = "工作流相关的元数据信息")
@Api(tags = "工作流相关的元数据信息(MetadataController)")
public class FlowMetadataController {

    @Autowired
    private FlowMetadataService flowMetadataService;

    @PostMapping("/getProc")
    @ApiOperation(value = "获取关联的流程实例")
    public JsonResult getProcInst(@RequestBody FlowMetadata flowMetadata) {
        return flowMetadataService.getProcInst(flowMetadata);
    }


    @GetMapping(value = "/getMetaPub")
    @ApiOperation(value = "获取元数据公共信息")
    public JsonResult<MetaPub> getMetaPub(String procInstId){
        return JsonResult.success(flowMetadataService.getMetadata(procInstId));
    }
}
