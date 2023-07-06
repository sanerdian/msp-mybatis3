package com.jnetdata.msp.flowable.service.common;

import com.alibaba.fastjson.JSONObject;
import com.jnetdata.msp.core.model.util.SessionUser;
import com.jnetdata.msp.core.util.MapUtil;
import com.jnetdata.msp.flowable.constants.FlowCons;
import com.jnetdata.msp.flowable.enums.AuditStatus;
import com.jnetdata.msp.flowable.enums.Logic;
import com.jnetdata.msp.flowable.mapper.FlowMetadataMapper;
import com.jnetdata.msp.flowable.mapper.ProcessDefiMapper;
import com.jnetdata.msp.flowable.model.FlowMetadata;
import com.jnetdata.msp.flowable.model.MetaPub;
import com.jnetdata.msp.flowable.model.ProcessDefi;
import com.jnetdata.msp.flowable.service.FlowMetadataService;
import com.jnetdata.msp.flowable.util.FlowUtil;
import com.jnetdata.msp.flowable.vo.ProcessDefiVo;
import com.jnetdata.msp.flowable.vo.ProcessStartVo;
import com.jnetdata.msp.flowable.vo.ProcessVo;
import com.jnetdata.msp.metadata.tableinfo.mapper.TableinfoMapper;
import com.jnetdata.msp.metadata.tableinfo.model.Tableinfo;
import com.jnetdata.msp.util.ObjectUtil;
import com.jnetdata.msp.visual.moduleform.mapper.ModuleFormMapper;
import com.jnetdata.msp.visual.moduleform.model.ModuleForm;
import com.jnetdata.msp.vo.BaseVo;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.flowable.bpmn.model.BpmnModel;
import org.flowable.common.engine.impl.identity.Authentication;
import org.flowable.engine.*;
import org.flowable.engine.history.HistoricActivityInstance;
import org.flowable.engine.history.HistoricProcessInstance;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.engine.repository.ProcessDefinitionQuery;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.image.ProcessDiagramGenerator;
import org.flowable.task.api.Task;
import org.flowable.ui.modeler.domain.AbstractModel;
import org.flowable.ui.modeler.domain.Model;
import org.flowable.ui.modeler.repository.ModelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.thenicesys.data.api.Pager;
import org.thenicesys.mybatis.impl.PropertyWrapper;
import org.thenicesys.web.JsonResult;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class FlowProcessService {

    @Autowired
    private HistoryService historyService;
    @Autowired
    private TaskService taskService;
    @Autowired
    private RepositoryService repositoryService;
    @Autowired
    private ProcessEngineConfiguration cfg;
    @Autowired
    private RuntimeService runtimeService;
    @Resource
    private FlowMetadataMapper flowMetadataMapper;
    @Resource
    private ProcessDefiMapper processDefiMapper;

    /**
     * 获取流程图
     * @param procInstId 流程实例id
     * @param formId 表单组件id
     */
    public void getProcessImage(String procInstId, String formId, HttpServletResponse response){
        try {
            if(!StringUtils.isEmpty(procInstId)){
                this.getByProcInstId(procInstId, response);
            }else if(!StringUtils.isEmpty(formId)){
                this.getByFormId(formId, response);
            }else {
                log.error("流程实例id和表单组件id同时为空");
            }
        }catch (Exception e){
            log.error("获取流程图异常：procInstId：{}, formId：{}, 错误信息：{}", procInstId, formId, e.getMessage());
        }
    }

    /**
     * 根据流程实例获取流程图
     * @param formId 表单组件id
     */
    private void getByFormId(String formId, HttpServletResponse response){
        List<ProcessDefi> procList = processDefiMapper.selectList(new PropertyWrapper<>(ProcessDefi.class)
                .eq("formId", formId).getWrapper());
        if(CollectionUtils.isEmpty(procList)){
            log.error("表单没有关联的流程定义");
        }
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
                .processDefinitionKey(procList.get(0).getProcessKey()).latestVersion().singleResult();

        //生成流程图
        this.getProcessImageExecute(processDefinition.getId(), new ArrayList<>(), new ArrayList<>(), response);
    }

    /**
     * 根据流程实例获取流程图
     * @param procInstId 流程实例id
     */
    private void getByProcInstId(String procInstId, HttpServletResponse response){
        //查询流程实例
        HistoricProcessInstance procInst = historyService.createHistoricProcessInstanceQuery().processInstanceId(procInstId).singleResult();
        if(ObjectUtils.isEmpty(procInst)){
            log.error("没有对应的流程实例");
            return;
        }

        //高亮显示的节点
        List<String> activityIds = new ArrayList<>();
        if(ObjectUtils.isEmpty(procInst.getEndTime())){
            //流程未结束，高亮显示任务节点
            List<Task> tasks = taskService.createTaskQuery().processInstanceId(procInstId).list();
            for (Task task : tasks) {
                activityIds.add(task.getTaskDefinitionKey());
            }
        }else{
            //流程已结束，高亮显示结束节点
            List<HistoricActivityInstance> historyProcess = historyService.createHistoricActivityInstanceQuery().processInstanceId(procInstId).orderByHistoricActivityInstanceStartTime().asc().list();
            for (HistoricActivityInstance hi : historyProcess) {
                String activityType = hi.getActivityType();
                if (activityType.equals("endEvent")) {
                    activityIds.add(hi.getActivityId());
                }
            }
        }

        //高亮显示流程经过的连线
        List<HistoricActivityInstance> historyProcess = historyService.createHistoricActivityInstanceQuery().processInstanceId(procInstId).list();
        List<String> flows = new ArrayList<>();
        for (HistoricActivityInstance hi : historyProcess) {
            String activityType = hi.getActivityType();
            if (activityType.equals("sequenceFlow")) {
                flows.add(hi.getActivityId());
            }
        }

        //生成流程图
        this.getProcessImageExecute(procInst.getProcessDefinitionId(), activityIds, flows, response);
    }

    /**
     * 生成流程图
     * @param procDefiId 流程定义id
     * @param activityIds 高亮显示的节点
     * @param flows 高亮显示流程经过的连线
     */
    private void getProcessImageExecute(String procDefiId, List<String> activityIds, List<String> flows, HttpServletResponse response){
        //获取流程图
        BpmnModel bpmnModel = repositoryService.getBpmnModel(procDefiId);

        //定义流程画布生成器
        ProcessDiagramGenerator processDiagramGenerator = cfg.getProcessDiagramGenerator();
        InputStream in = processDiagramGenerator.generateDiagram(bpmnModel, "gif", activityIds, flows,
                "宋体", "宋体", "宋体",
                cfg.getClassLoader(), 1.0, true);

        response.setContentType("image/Jpeg");
        try {
            ImageIO.write(ImageIO.read(in), "JPEG", response.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 查询流程定义列表
     */
    public JsonResult<List<ProcessDefiVo>> process(ProcessDefiVo vo) {
        try {
            //查询数据
            ProcessDefinitionQuery query = repositoryService.createProcessDefinitionQuery().latestVersion();
            if(!ObjectUtils.isEmpty(vo) && !StringUtils.isEmpty(vo.getName())){
                query = query.processDefinitionNameLike(vo.getName());
            }
            List<ProcessDefinition> procDefiList = query.list();
            //获取对象信息
            List<ProcessDefiVo> voList = new ArrayList<>();
            for(ProcessDefinition defi: procDefiList){
                voList.add(this.transferToVo(defi));
            }
            return JsonResult.success(voList);
        }catch (Exception e){
            log.error("查询流程定义列表异常：{}", e.getMessage());
        }
        return JsonResult.fail();
    }

    /**
     * 查询流程定义列表(分页)
     */
    public JsonResult<Pager<ProcessDefiVo>> procDefiList(BaseVo<ProcessDefiVo> vo) {
        try {
            //查询数据
            ProcessDefinitionQuery query =  repositoryService.createProcessDefinitionQuery().latestVersion();
            if(!StringUtils.isEmpty(vo.getEntity().getName())){
                query = query.processDefinitionNameLike(vo.getEntity().getName());
            }
            long count = query.count();
            List<ProcessDefinition> procDefiList = query.listPage((vo.getPager().getCurrent()-1)*vo.getPager().getSize(), vo.getPager().getSize());

            //获取对象信息
            List<ProcessDefiVo> records = new ArrayList<>();
            for(ProcessDefinition defi: procDefiList){
                records.add(this.transferToVo(defi));
            }

            //分页信息
            Pager pager = new Pager();
            pager.setRecords(records);
            pager.setTotal((int)count);
            pager.setCurrent(vo.getPager().getCurrent());
            pager.setSize(vo.getPager().getSize());
            return JsonResult.success(pager);
        }catch (Exception e){
            log.error("查询流程定义列表(分页)异常：{}", e.getMessage());
        }
        return JsonResult.fail();
    }

    /**
     * 获取对象信息
     */
    private ProcessDefiVo transferToVo(ProcessDefinition defi){
        ProcessDefiVo vo = new ProcessDefiVo();
        vo.setId(defi.getId());
        vo.setName(defi.getName());
        vo.setKey(defi.getKey());
        return vo;
    }

    /**
     * 根据流程key获取最新版本的流程信息
     */
    public JsonResult latestProc(String procKey){
        try{
            if(StringUtils.isEmpty(procKey)){
                return JsonResult.fail(String.valueOf(HttpStatus.BAD_REQUEST.value()), "procKey不能为空！");
            }
            ProcessDefinition procDefi = repositoryService.createProcessDefinitionQuery().processDefinitionKey(procKey).latestVersion().singleResult();
            Map<String, Object> map = MapUtil.toMapForFlowable(procDefi);
            return JsonResult.success(map);
        }catch (Exception e){
            log.error(e.getMessage());
            return JsonResult.fail();
        }
    }

    /**
     * 终止流程
     */
    public JsonResult terminate(ProcessVo vo){
        try{
            if(StringUtils.isEmpty(vo.getProcInstId())){
                return JsonResult.fail(String.valueOf(HttpStatus.BAD_REQUEST.value()), "流程实例id(procInstId)不能为空！");
            }
            //获取对应的流程实例信息
            HistoricProcessInstance procInst = historyService.createHistoricProcessInstanceQuery().processInstanceId(vo.getProcInstId()).singleResult();
            if(ObjectUtils.isEmpty(procInst)){
                return JsonResult.fail(String.valueOf(HttpStatus.BAD_REQUEST.value()), "没有对应的流程！");
            }else if(!ObjectUtils.isEmpty(procInst.getEndTime())){
                return JsonResult.fail(String.valueOf(HttpStatus.BAD_REQUEST.value()), "流程已结束，无需操作！");
            }
            //删除流程实例
            runtimeService.deleteProcessInstance(procInst.getId(), vo.getDeleteReason());

            //修改元数据
            FlowMetadata metadata = flowMetadataMapper.selectById(procInst.getId());
            Map map = FlowUtil.initMap(metadata.getMetadataTable(), metadata.getMetadataId());
            map.put("auditStatus", AuditStatus.TERMINATED.getCode());
            flowMetadataMapper.updateMetadata(map);

            return JsonResult.success();
        }catch (Exception e){
            log.error("终止流程异常：{}", e.getMessage());
            return JsonResult.fail();
        }
    }


}
