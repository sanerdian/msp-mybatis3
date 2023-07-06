package com.jnetdata.msp.flowable.controller;

import com.jnetdata.msp.flowable.service.common.FlowModelService;
import com.jnetdata.msp.flowable.vo.ModelVo;
import com.jnetdata.msp.vo.BaseVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.flowable.bpmn.converter.BpmnXMLConverter;
import org.flowable.bpmn.model.BpmnModel;
import org.flowable.ui.modeler.domain.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.thenicesys.web.JsonResult;

import java.util.List;

/**
 * 工作流模型
 */
@Slf4j
@RestController
@RequestMapping("/flowable/model")
@Api(tags = "工作流模型(FlowModelController)")
public class FlowModelController {

    @Autowired
    private FlowModelService flowModelService;

    @PostMapping(value = "/procModelList")
    @ApiOperation(value = "流程模型列表")
    public JsonResult<List<ModelVo>> procModelList(@RequestBody BaseVo<ModelVo> vo){
        return flowModelService.procModelList(vo);
    }

    @PostMapping(value = "/deploy")
    @ApiOperation(value = "部署流程")
    public JsonResult deploy(@RequestBody ModelVo vo) {
        return flowModelService.deploy(vo);
    }
}
