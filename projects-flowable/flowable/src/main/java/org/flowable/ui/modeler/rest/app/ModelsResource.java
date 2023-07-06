//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package org.flowable.ui.modeler.rest.app;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.io.IOException;
import java.util.Iterator;
import javax.servlet.http.HttpServletRequest;

import com.jnetdata.msp.flowable.service.ProcessDefiService;
import org.apache.commons.lang3.StringUtils;
import org.flowable.ui.common.model.ResultListDataRepresentation;
import org.flowable.ui.common.security.SecurityUtils;
import org.flowable.ui.common.service.exception.ConflictingRequestException;
import org.flowable.ui.common.service.exception.InternalServerErrorException;
import org.flowable.ui.modeler.domain.Model;
import org.flowable.ui.modeler.model.ModelKeyRepresentation;
import org.flowable.ui.modeler.model.ModelRepresentation;
import org.flowable.ui.modeler.service.FlowableModelQueryService;
import org.flowable.ui.modeler.serviceapi.ModelService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping({"/app"})
public class ModelsResource {
    private static final Logger LOGGER = LoggerFactory.getLogger(ModelsResource.class);
    @Autowired
    protected FlowableModelQueryService modelQueryService;
    @Autowired
    protected ModelService modelService;
    @Autowired
    protected ObjectMapper objectMapper;
    @Autowired
    private ProcessDefiService processDefiService;

    public ModelsResource() {
    }

    @RequestMapping(
            value = {"/rest/models"},
            method = {RequestMethod.GET},
            produces = {"application/json"}
    )
    public ResultListDataRepresentation getModels(@RequestParam(required = false) String filter, @RequestParam(required = false) String sort, @RequestParam(required = false) Integer modelType, HttpServletRequest request) {
        return this.modelQueryService.getModels(filter, sort, modelType, request);
    }

    @RequestMapping(
            value = {"/rest/models-for-app-definition"},
            method = {RequestMethod.GET},
            produces = {"application/json"}
    )
    public ResultListDataRepresentation getModelsToIncludeInAppDefinition() {
        return this.modelQueryService.getModelsToIncludeInAppDefinition();
    }

    @RequestMapping(
            value = {"/rest/cmmn-models-for-app-definition"},
            method = {RequestMethod.GET},
            produces = {"application/json"}
    )
    public ResultListDataRepresentation getCmmnModelsToIncludeInAppDefinition() {
        return this.modelQueryService.getCmmnModelsToIncludeInAppDefinition();
    }

    @RequestMapping(
            value = {"/rest/import-process-model"},
            method = {RequestMethod.POST},
            produces = {"application/json"}
    )
    public ModelRepresentation importProcessModel(HttpServletRequest request, @RequestParam("file") MultipartFile file) {
        return this.modelQueryService.importProcessModel(request, file);
    }

    @RequestMapping(
            value = {"/rest/import-process-model/text"},
            method = {RequestMethod.POST}
    )
    public String importProcessModelText(HttpServletRequest request, @RequestParam("file") MultipartFile file) {
        ModelRepresentation modelRepresentation = this.modelQueryService.importProcessModel(request, file);
        String modelRepresentationJson = null;

        try {
            modelRepresentationJson = this.objectMapper.writeValueAsString(modelRepresentation);
            return modelRepresentationJson;
        } catch (Exception var6) {
            LOGGER.error("Error while processing Model representation json", var6);
            throw new InternalServerErrorException("Model Representation could not be saved");
        }
    }

    @RequestMapping(
            value = {"/rest/import-case-model"},
            method = {RequestMethod.POST},
            produces = {"application/json"}
    )
    public ModelRepresentation importCaseModel(HttpServletRequest request, @RequestParam("file") MultipartFile file) {
        return this.modelQueryService.importCaseModel(request, file);
    }

    @RequestMapping(
            value = {"/rest/import-case-model/text"},
            method = {RequestMethod.POST}
    )
    public String importCaseModelText(HttpServletRequest request, @RequestParam("file") MultipartFile file) {
        ModelRepresentation modelRepresentation = this.modelQueryService.importCaseModel(request, file);
        String modelRepresentationJson = null;

        try {
            modelRepresentationJson = this.objectMapper.writeValueAsString(modelRepresentation);
            return modelRepresentationJson;
        } catch (Exception var6) {
            LOGGER.error("Error while processing Model representation json", var6);
            throw new InternalServerErrorException("Model Representation could not be saved");
        }
    }

    /**
     * 新增模型
     */
    @RequestMapping(
            value = {"/rest/models"},
            method = {RequestMethod.POST},
            produces = {"application/json"}
    )
    public ModelRepresentation createModel(@RequestBody ModelRepresentation modelRepresentation) {
        modelRepresentation.setKey(modelRepresentation.getKey().replaceAll(" ", ""));
        this.checkForDuplicateKey(modelRepresentation);
        String json = this.modelService.createModelJson(modelRepresentation);
        Model newModel = this.modelService.createModel(modelRepresentation, json, SecurityUtils.getCurrentUserObject());

        ModelRepresentation result = new ModelRepresentation(newModel);
        //新增流程定义
        processDefiService.insertEntity(result);

        return result;
    }

    protected void checkForDuplicateKey(ModelRepresentation modelRepresentation) {
        ModelKeyRepresentation modelKeyInfo = this.modelService.validateModelKey((Model)null, modelRepresentation.getModelType(), modelRepresentation.getKey());
        if (modelKeyInfo.isKeyAlreadyExists()) {
            throw new ConflictingRequestException("Provided model key already exists: " + modelRepresentation.getKey());
        }
    }

    @RequestMapping(
            value = {"/rest/models/{modelId}/clone"},
            method = {RequestMethod.POST},
            produces = {"application/json"}
    )
    public ModelRepresentation duplicateModel(@PathVariable String modelId, @RequestBody ModelRepresentation modelRepresentation) {
        String json = null;
        Model model = null;
        if (modelId != null) {
            model = this.modelService.getModel(modelId);
            json = model.getModelEditorJson();
        }

        if (model == null) {
            throw new InternalServerErrorException("Error duplicating model : Unknown original model");
        } else {
            modelRepresentation.setKey(modelRepresentation.getKey().replaceAll(" ", ""));
            this.checkForDuplicateKey(modelRepresentation);
            if (modelRepresentation.getModelType() == null || modelRepresentation.getModelType().equals(0)) {
                ObjectNode editorNode = null;

                try {
                    editorNode = (ObjectNode)this.objectMapper.readTree(json);
                    ObjectNode propertiesNode = (ObjectNode)editorNode.get("properties");
                    String processId = modelRepresentation.getKey().replaceAll(" ", "");
                    propertiesNode.put("process_id", processId);
                    propertiesNode.put("name", modelRepresentation.getName());
                    if (StringUtils.isNotEmpty(modelRepresentation.getDescription())) {
                        propertiesNode.put("documentation", modelRepresentation.getDescription());
                    }

                    editorNode.set("properties", propertiesNode);
                } catch (IOException var8) {
                    var8.printStackTrace();
                }

                if (editorNode != null) {
                    json = editorNode.toString();
                }
            }

            Model newModel = this.modelService.createModel(modelRepresentation, json, SecurityUtils.getCurrentUserObject());
            byte[] imageBytes = model.getThumbnail();
            newModel = this.modelService.saveModel(newModel, newModel.getModelEditorJson(), imageBytes, false, newModel.getComment(), SecurityUtils.getCurrentUserObject());
            return new ModelRepresentation(newModel);
        }
    }

    protected ObjectNode deleteEmbededReferencesFromBPMNModel(ObjectNode editorJsonNode) {
        try {
            this.internalDeleteNodeByNameFromBPMNModel(editorJsonNode, "formreference");
            this.internalDeleteNodeByNameFromBPMNModel(editorJsonNode, "subprocessreference");
            return editorJsonNode;
        } catch (Exception var3) {
            throw new InternalServerErrorException("Cannot delete the external references");
        }
    }

    protected ObjectNode deleteEmbededReferencesFromStepModel(ObjectNode editorJsonNode) {
        try {
            JsonNode startFormNode = editorJsonNode.get("startForm");
            if (startFormNode != null) {
                editorJsonNode.remove("startForm");
            }

            this.internalDeleteNodeByNameFromStepModel(editorJsonNode.get("steps"), "formDefinition");
            this.internalDeleteNodeByNameFromStepModel(editorJsonNode.get("steps"), "subProcessDefinition");
            return editorJsonNode;
        } catch (Exception var3) {
            throw new InternalServerErrorException("Cannot delete the external references");
        }
    }

    protected void internalDeleteNodeByNameFromBPMNModel(JsonNode editorJsonNode, String propertyName) {
        JsonNode childShapesNode = editorJsonNode.get("childShapes");
        if (childShapesNode != null && childShapesNode.isArray()) {
            ArrayNode childShapesArrayNode = (ArrayNode)childShapesNode;
            Iterator var5 = childShapesArrayNode.iterator();

            while(var5.hasNext()) {
                JsonNode childShapeNode = (JsonNode)var5.next();
                ObjectNode properties = (ObjectNode)childShapeNode.get("properties");
                if (properties != null && properties.has(propertyName)) {
                    JsonNode propertyNode = properties.get(propertyName);
                    if (propertyNode != null) {
                        properties.remove(propertyName);
                    }
                }

                if (childShapeNode.has("childShapes")) {
                    this.internalDeleteNodeByNameFromBPMNModel(childShapeNode, propertyName);
                }
            }
        }

    }

    private void internalDeleteNodeByNameFromStepModel(JsonNode stepsNode, String propertyName) {
        if (stepsNode != null && stepsNode.isArray()) {
            Iterator var3 = stepsNode.iterator();

            while(true) {
                ObjectNode stepNode;
                do {
                    if (!var3.hasNext()) {
                        return;
                    }

                    JsonNode jsonNode = (JsonNode)var3.next();
                    stepNode = (ObjectNode)jsonNode;
                    if (stepNode.has(propertyName)) {
                        JsonNode propertyNode = stepNode.get(propertyName);
                        if (propertyNode != null) {
                            stepNode.remove(propertyName);
                        }
                    }

                    if (stepNode.has("steps")) {
                        this.internalDeleteNodeByNameFromStepModel(stepNode.get("steps"), propertyName);
                    }

                    if (stepNode.has("overdueSteps")) {
                        this.internalDeleteNodeByNameFromStepModel(stepNode.get("overdueSteps"), propertyName);
                    }
                } while(!stepNode.has("choices"));

                ArrayNode choicesArrayNode = (ArrayNode)stepNode.get("choices");
                Iterator var7 = choicesArrayNode.iterator();

                while(var7.hasNext()) {
                    JsonNode choiceNode = (JsonNode)var7.next();
                    if (choiceNode.has("steps")) {
                        this.internalDeleteNodeByNameFromStepModel(choiceNode.get("steps"), propertyName);
                    }
                }
            }
        }
    }
}
