package com.jnetdata.msp.flowable.service.common;

import com.jnetdata.msp.flowable.vo.ModelVo;
import com.jnetdata.msp.vo.BaseVo;
import lombok.extern.slf4j.Slf4j;
import org.flowable.bpmn.converter.BpmnXMLConverter;
import org.flowable.bpmn.model.BpmnModel;
import org.flowable.engine.RepositoryService;
import org.flowable.ui.modeler.domain.AbstractModel;
import org.flowable.ui.modeler.domain.Model;
import org.flowable.ui.modeler.repository.ModelRepository;
import org.flowable.ui.modeler.serviceapi.ModelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.thenicesys.web.JsonResult;

import java.util.ArrayList;
import java.util.List;

/**
 * 工作流模型服务
 */
@Slf4j
@Service
public class FlowModelService {

    @Autowired
    protected ModelRepository modelRepository;
    @Autowired
    private ModelService modelService;
    @Autowired
    private RepositoryService repositoryService;

    /**
     * 流程模型列表
     */
    public JsonResult<List<ModelVo>> procModelList(BaseVo<ModelVo> vo){
        try {
            //参数
            List<Model> modelList = null;
            String filterText = "";
            if(!ObjectUtils.isEmpty(vo) && !ObjectUtils.isEmpty(vo.getEntity()) && !ObjectUtils.isEmpty(vo.getEntity().getName())){
                filterText = "%" + vo.getEntity().getName() + "%";
            }
            //获取数据
            if(filterText.length() > 0){
                modelList = modelRepository.findByModelTypeAndFilter(AbstractModel.MODEL_TYPE_BPMN, filterText, "modifiedDesc");
            }else{
                modelList = modelRepository.findByModelType(AbstractModel.MODEL_TYPE_BPMN, "modifiedDesc");
            }

            //获取对象具体信息
            List<ModelVo> voList = new ArrayList<>();
            if(!CollectionUtils.isEmpty(modelList)){
                for(Model model: modelList){
                    ModelVo modelVo = new ModelVo();
                    modelVo.setId(model.getId());
                    modelVo.setName(model.getName());
                    modelVo.setCreateTime(model.getCreated());
                    modelVo.setUpdateTime(model.getLastUpdated());
                    modelVo.setKey(model.getKey());
                    voList.add(modelVo);
                }
            }
            return JsonResult.success(voList);
        }catch (Exception e){
            log.error(e.getMessage());
        }
        return JsonResult.fail();
    }

    public JsonResult deploy(ModelVo vo) {
        try {
            Model model = modelService.getModel(vo.getId());
            if(ObjectUtils.isEmpty(model)){
                return JsonResult.fail("没有对应的流程模型");
            }
            byte[] bytes = modelService.getBpmnXML(model);
            if(bytes==null){
                return JsonResult.fail("模型数据为空，请先设计流程并成功保存，再进行发布。");
            }

            BpmnModel bpmnModel = modelService.getBpmnModel(model);
            if(bpmnModel.getProcesses().size()==0){
                return JsonResult.fail("数据模型不符要求，请至少设计一条主线流程。");
            }
            byte[] bpmnBytes = new BpmnXMLConverter().convertToXML(bpmnModel);
            String processName = model.getName()+".bpmn20.xml";
            repositoryService.createDeployment()
                    .name(model.getName())
                    .addBytes(processName,bpmnBytes)
                    .deploy();
            return JsonResult.success("部署流程成功");
        }catch (Exception e){
            log.error(e.getMessage());
        }
        return JsonResult.fail("部署流程失败");
    }
}
