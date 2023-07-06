package com.jnetdata.msp.flowable.service.common;

import com.alibaba.fastjson.JSONObject;
import com.jnetdata.msp.core.model.util.SessionUser;
import com.jnetdata.msp.flowable.constants.FlowCons;
import com.jnetdata.msp.flowable.enums.Logic;
import com.jnetdata.msp.flowable.mapper.FlowMetadataMapper;
import com.jnetdata.msp.flowable.mapper.ProcessDefiMapper;
import com.jnetdata.msp.flowable.model.FlowMetadata;
import com.jnetdata.msp.flowable.model.MetaPub;
import com.jnetdata.msp.flowable.model.ProcessDefi;
import com.jnetdata.msp.flowable.service.FlowMetadataService;
import com.jnetdata.msp.flowable.vo.ProcessStartVo;
import com.jnetdata.msp.metadata.tableinfo.mapper.TableinfoMapper;
import com.jnetdata.msp.metadata.tableinfo.model.Tableinfo;
import com.jnetdata.msp.visual.moduleform.mapper.ModuleFormMapper;
import com.jnetdata.msp.visual.moduleform.model.ModuleForm;
import lombok.extern.slf4j.Slf4j;
import org.flowable.common.engine.impl.identity.Authentication;
import org.flowable.engine.HistoryService;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.engine.repository.ProcessDefinitionQuery;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.thenicesys.mybatis.impl.PropertyWrapper;
import org.thenicesys.web.JsonResult;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;


/**
 * 启动流程
 */
@Slf4j
@Service
public class ProcessStartService {

    @Autowired
    private HistoryService historyService;
    @Autowired
    private TaskService taskService;
    @Autowired
    private RepositoryService repositoryService;
    @Autowired
    private FlowMetadataService flowMetadataService;
    @Autowired
    private FlowTaskService flowTaskService;
    @Autowired
    private RuntimeService runtimeService;
    @Resource
    private FlowMetadataMapper flowMetadataMapper;
    @Resource
    private ModuleFormMapper moduleFormMapper;
    @Resource
    private TableinfoMapper tableinfoMapper;
    @Resource
    private ProcessDefiMapper processDefiMapper;

    /**
     * 启动流程
     */
    public JsonResult<MetaPub> start(ProcessStartVo vo){
        try {

        }catch (Exception e){

        }
        return JsonResult.fail();
    }

    /**
     * 启动流程并提交
     */
    public JsonResult<MetaPub> startAndSubmit(ProcessStartVo vo){
        try {
            if(!StringUtils.isEmpty(vo.getFormId())){
                return this.startAndSubmitForm(vo);
            }else{
                return this.startAndSubmitTable(vo);
            }

        }catch (Exception e){
            log.error("启动流程并提交异常：{}", e.getMessage());
            return JsonResult.fail();
        }
    }

    /**
     * 使用元数据表启动流程
     */
    private JsonResult<MetaPub> startAndSubmitTable(ProcessStartVo vo){
        try{
            if(StringUtils.isEmpty(vo.getMetadataTable())){
                return JsonResult.fail(String.valueOf(HttpStatus.BAD_REQUEST.value()), "表单id和元数据表名不能同时为空");
            }

            return this.execute(vo);
        }catch (Exception e){
            log.error("使用元数据表启动流程异常：{}", e.getMessage());
            return JsonResult.fail();
        }
    }

    private JsonResult<MetaPub> execute(ProcessStartVo vo){
        //启动流程
        Map<String, String> map = this.startProcessCommon(vo);
        if(map.get("code").equals(Logic.NO.getCode())){
            return JsonResult.fail(String.valueOf(HttpStatus.BAD_REQUEST.value()),map.get("value"));
        }
        String procInstId = map.get("value");

        //处理业务数据
        if(!StringUtils.isEmpty(procInstId)){
            flowMetadataService.saveEntity(procInstId, vo);
        }

        //下一环节流程审核人
        JSONObject json = new JSONObject();
        if(!StringUtils.isEmpty(vo.getAuditUserId())){
            json.put(FlowCons.AUDIT_USER_ID, vo.getAuditUserId());
        }

        //当前任务
        Task task = taskService.createTaskQuery().processInstanceId(procInstId).singleResult();
        //设置当前用户为任务处理人
        taskService.setAssignee(task.getId(), SessionUser.getCurrentUser().getId().toString());
        //完成任务
        flowTaskService.completeTaskCommon(task, json);
        return JsonResult.success(flowMetadataService.getMetadata(task.getProcessInstanceId()));
    }

    /**
     * 使用表单启动流程
     */
    private JsonResult<MetaPub> startAndSubmitForm(ProcessStartVo vo){
        try{
            //获取表单关联的元数据信息
            ModuleForm form = moduleFormMapper.selectById(vo.getFormId());
            if(ObjectUtils.isEmpty(form)){
                return JsonResult.fail(String.valueOf(HttpStatus.BAD_REQUEST.value()), "没有对应的表单信息");
            }else if(StringUtils.isEmpty(form.getDbTableId())){
                return JsonResult.fail(String.valueOf(HttpStatus.BAD_REQUEST.value()), "表单没有关联元数据表");
            }
            Tableinfo tableinfo = tableinfoMapper.selectById(form.getDbTableId());
            if(ObjectUtils.isEmpty(tableinfo) || StringUtils.isEmpty(tableinfo.getTablename())){
                return JsonResult.fail(String.valueOf(HttpStatus.BAD_REQUEST.value()), "表单没有关联元数据表");
            }
            vo.setMetadataTable(tableinfo.getTablename());

            //获取表单关联的流程信息
            List<ProcessDefi> procList = processDefiMapper.selectList(new PropertyWrapper<>(ProcessDefi.class)
                    .eq("formId", vo.getFormId()).getWrapper());
            if(CollectionUtils.isEmpty(procList)){
                return JsonResult.fail(String.valueOf(HttpStatus.BAD_REQUEST.value()), "表单没有关联流程信息");
            }else if(procList.size() > 1){
                return JsonResult.fail(String.valueOf(HttpStatus.BAD_REQUEST.value()), "表单关联了多个流程信息");
            }
            vo.setProcessDefinitionKey(procList.get(0).getProcessKey());

            return this.execute(vo);
        }catch (Exception e){
            log.error("使用表单启动流程异常：{}", e.getMessage());
            return JsonResult.fail();
        }
    }


    /**
     * 启动流程
     */
    private Map<String, String> startProcessCommon(ProcessStartVo vo){
        if(StringUtils.isEmpty(vo.getMetadataId())){
            return fillMap(Logic.NO.getCode(), "元数据id不能为空");
        }

        //是否已经启动流程
        if(!StringUtils.isEmpty(vo.getMetadataTable()) && !StringUtils.isEmpty(vo.getMetadataId())){
            PropertyWrapper<FlowMetadata> wrapper = new PropertyWrapper<>(FlowMetadata.class)
                    .eq("metadataTable", vo.getMetadataTable().toLowerCase()).eq("metadataId", vo.getMetadataId());
            List<FlowMetadata> metadataList = flowMetadataMapper.selectList(wrapper.getWrapper());
            Set<String> procInstIds = metadataList.stream().map(m -> m.getId()).collect(Collectors.toSet());
            if(!CollectionUtils.isEmpty(procInstIds)){
                long count = historyService.createHistoricProcessInstanceQuery().unfinished().processInstanceIds(procInstIds).count();
                if(count > 0){
                    return fillMap(Logic.NO.getCode(), "该数据已经启动流程");
                }
            }
        }

        //获取流程定义，校验
        if(StringUtils.isEmpty(vo.getProcessDefinitionId()) && StringUtils.isEmpty(vo.getProcessDefinitionKey())){
            return fillMap(Logic.NO.getCode(), "流程定义ID和流程定义KEY不能同时为空");
        }
        ProcessDefinitionQuery query = repositoryService.createProcessDefinitionQuery();
        if(!StringUtils.isEmpty(vo.getProcessDefinitionId())){
            query.processDefinitionId(vo.getProcessDefinitionId());
        }else{
            query.processDefinitionKey(vo.getProcessDefinitionKey()).latestVersion();
        }
        ProcessDefinition definition = query.singleResult();
        if(ObjectUtils.isEmpty(definition)){
            return fillMap(Logic.NO.getCode(), "没有对应的流程定义");
        }else if(definition.isSuspended()){
            return fillMap(Logic.NO.getCode(), "流程已挂起");
        }

        //设置流程启动人
        Authentication.setAuthenticatedUserId(SessionUser.getCurrentUser().getId().toString());
        //启动流程
        ProcessInstance instance = runtimeService.startProcessInstanceById(definition.getId(), new JSONObject());
        log.info("根据流程定义ID启动流程成功，流程定义id：{}，流程实例id：{}", definition.getId(), instance.getId());
        Authentication.setAuthenticatedUserId(null);

        return fillMap(Logic.YES.getCode(),instance.getId());
    }

    private Map<String, String> fillMap(String code, String value){
        Map<String, String> map = new HashMap<>();
        map.put("code", code);
        map.put("value", value);
        return map;
    }
}
