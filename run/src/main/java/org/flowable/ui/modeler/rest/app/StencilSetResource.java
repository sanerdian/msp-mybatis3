//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package org.flowable.ui.modeler.rest.app;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.flowable.ui.common.service.exception.InternalServerErrorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping({"/app"})
public class StencilSetResource {
    private static final Logger LOGGER = LoggerFactory.getLogger(StencilSetResource.class);
    @Autowired
    protected ObjectMapper objectMapper;

    public StencilSetResource() {
    }

    @RequestMapping(
            value = {"/rest/stencil-sets/editor"},
            method = {RequestMethod.GET},
            produces = {"application/json"}
    )
    public JSONObject getStencilSetForEditor() {
        try {
            JsonNode stencilNode = this.objectMapper.readTree(this.getClass().getClassLoader().getResourceAsStream("stencilset_bpmn.json"));
            return JSON.parseObject(stencilNode.toString());
        } catch (Exception var2) {
            LOGGER.error("Error reading bpmn stencil set json", var2);
            throw new InternalServerErrorException("Error reading bpmn stencil set json");
        }
    }

    @RequestMapping(
            value = {"/rest/stencil-sets/cmmneditor"},
            method = {RequestMethod.GET},
            produces = {"application/json"}
    )
    public JsonNode getCmmnStencilSetForEditor() {
        try {
            JsonNode stencilNode = this.objectMapper.readTree(this.getClass().getClassLoader().getResourceAsStream("stencilset_cmmn.json"));
            return stencilNode;
        } catch (Exception var2) {
            LOGGER.error("Error reading bpmn stencil set json", var2);
            throw new InternalServerErrorException("Error reading bpmn stencil set json");
        }
    }
}
