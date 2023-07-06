//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package org.flowable.ui.modeler.rest.app;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.flowable.cmmn.model.GraphicInfo;
import org.flowable.ui.modeler.domain.Model;
import org.flowable.ui.modeler.domain.ModelHistory;
import org.flowable.ui.modeler.service.BpmnDisplayJsonConverter;
import org.flowable.ui.modeler.service.CmmnDisplayJsonConverter;
import org.flowable.ui.modeler.serviceapi.ModelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping({"/app"})
public class EditorDisplayJsonClientResource {
    @Autowired
    protected ModelService modelService;
    @Autowired
    protected BpmnDisplayJsonConverter bpmnDisplayJsonConverter;
    @Autowired
    protected CmmnDisplayJsonConverter cmmnDisplayJsonConverter;
    protected ObjectMapper objectMapper = new ObjectMapper();

    public EditorDisplayJsonClientResource() {
    }

    @RequestMapping(
            value = {"/rest/models/{modelId}/model-json"},
            method = {RequestMethod.GET},
            produces = {"application/json"}
    )
    public JSONObject getModelJSON(@PathVariable String modelId) {
        ObjectNode displayNode = this.objectMapper.createObjectNode();
        Model model = this.modelService.getModel(modelId);
        if (model.getModelType() != null && 5 == model.getModelType()) {
            this.cmmnDisplayJsonConverter.processCaseElements(model, displayNode, new GraphicInfo());
        } else {
            this.bpmnDisplayJsonConverter.processProcessElements(model, displayNode, new org.flowable.bpmn.model.GraphicInfo());
        }

        return JSON.parseObject(displayNode.toString());
    }

    @RequestMapping(
            value = {"/rest/models/{processModelId}/history/{processModelHistoryId}/model-json"},
            method = {RequestMethod.GET},
            produces = {"application/json"}
    )
    public JsonNode getModelHistoryJSON(@PathVariable String processModelId, @PathVariable String processModelHistoryId) {
        ObjectNode displayNode = this.objectMapper.createObjectNode();
        ModelHistory model = this.modelService.getModelHistory(processModelId, processModelHistoryId);
        if (model.getModelType() != null && 5 == model.getModelType()) {
            this.cmmnDisplayJsonConverter.processCaseElements(model, displayNode, new GraphicInfo());
        } else {
            this.bpmnDisplayJsonConverter.processProcessElements(model, displayNode, new org.flowable.bpmn.model.GraphicInfo());
        }

        return displayNode;
    }
}

