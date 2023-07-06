package com.jnetdata.msp.flowable.service.feiyi;

import com.jnetdata.msp.core.model.util.SessionUser;
import com.jnetdata.msp.flowable.mapper.FlowMetadataMapper;
import com.jnetdata.msp.flowable.mapper.MetadataConfigMapper;
import com.jnetdata.msp.flowable.model.MetadataConfig;
import com.jnetdata.msp.flowable.service.FlowMetadataService;
import com.jnetdata.msp.flowable.service.common.FlowCommonService;
import com.jnetdata.msp.flowable.vo.feiyi.HistoryVo;
import com.jnetdata.msp.flowable.vo.feiyi.SubmitVo;
import com.jnetdata.msp.flowable.vo.feiyi.TodoVo;
import com.jnetdata.msp.vo.BaseVo;
import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.HistoryService;
import org.flowable.engine.TaskService;
import org.flowable.engine.history.HistoricProcessInstance;
import org.flowable.engine.history.HistoricProcessInstanceQuery;
import org.flowable.task.api.Task;
import org.flowable.task.api.TaskQuery;
import org.flowable.task.api.history.HistoricTaskInstance;
import org.flowable.task.api.history.HistoricTaskInstanceQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.thenicesys.data.api.Pager;
import org.thenicesys.mybatis.impl.PropertyWrapper;
import org.thenicesys.web.JsonResult;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 工作台
 * 我的待办、我的已办、我的提交
 */
@Slf4j
@Service
public class WorkbenchService {

    @Autowired
    private FlowMetadataService flowMetadataService;
    @Autowired
    private HistoryService historyService;
    @Autowired
    private TaskService taskService;
    @Resource
    private MetadataConfigMapper metadataConfigMapper;
    @Autowired
    private FlowCommonService flowCommonService;

    /**
     * 我的待办
     */
    public JsonResult<Pager<TodoVo>> taskList(BaseVo<TodoVo> vo){
        try {
            //获取任务信息
            TaskQuery taskQuery = taskService.createTaskQuery().taskCandidateOrAssigned(SessionUser.getCurrentUser().getId().toString());
            if(!StringUtils.isEmpty(vo.getEntity().getProcDefiId())){
                taskQuery = taskQuery.processDefinitionId(vo.getEntity().getProcDefiId());
            }
            if(!StringUtils.isEmpty(vo.getEntity().getMetadataType())){
                //查询对应的流程定义key
                List<String> keyList = this.getKeyList(vo.getEntity().getMetadataType());
                if(!CollectionUtils.isEmpty(keyList)){
                    taskQuery.processDefinitionKeyIn(keyList);
                }
            }
            taskQuery = taskQuery.orderByTaskCreateTime().desc();
            List<Task> taskList = taskQuery.listPage((vo.getPager().getCurrent() - 1) * vo.getPager().getSize(), vo.getPager().getSize());

            //查询总数
            long count = taskQuery.count();

            //填充数据
            List<TodoVo> todoList = flowMetadataService.fillTodoVo(taskList);

            Pager pager = flowCommonService.getPager(todoList, count, vo.getPager().getCurrent(), vo.getPager().getSize());
            return JsonResult.success(pager);
        }catch (Exception e){
            log.error("我的待办异常:{}", e.getMessage());
            return JsonResult.fail();
        }

    }

    /**
     * 我的已办
     */
    public JsonResult<Pager<HistoryVo>> historyList(BaseVo<HistoryVo> vo) {
        try {
            //获取历史任务信息
            HistoricTaskInstanceQuery historicTaskInstanceQuery = historyService.createHistoricTaskInstanceQuery().finished().taskAssignee(SessionUser.getCurrentUser().getId().toString());
            if(!StringUtils.isEmpty(vo.getEntity().getProcDefiId())){
                historicTaskInstanceQuery = historicTaskInstanceQuery.processDefinitionId(vo.getEntity().getProcDefiId());
            }
            if(!StringUtils.isEmpty(vo.getEntity().getMetadataType())){
                //查询对应的流程定义key
                List<String> keyList = this.getKeyList(vo.getEntity().getMetadataType());
                if(!CollectionUtils.isEmpty(keyList)){
                    historicTaskInstanceQuery.processDefinitionKeyIn(keyList);
                }
            }
            historicTaskInstanceQuery = historicTaskInstanceQuery.orderByHistoricTaskInstanceStartTime().desc();
            List<HistoricTaskInstance> taskList = historicTaskInstanceQuery.listPage((vo.getPager().getCurrent() - 1) * vo.getPager().getSize(), vo.getPager().getSize());

            //查询总数
            long count = historicTaskInstanceQuery.count();

            //填充数据
            List<HistoryVo> historyList = flowMetadataService.fillHistoryVo(taskList);

            Pager pager = flowCommonService.getPager(historyList, count, vo.getPager().getCurrent(), vo.getPager().getSize());
            return JsonResult.success(pager);
        }catch (Exception e){
            log.error("我的已办异常:{}", e.getMessage());
            return JsonResult.fail();
        }
    }

    /**
     * 我的提交
     */
    public JsonResult<Pager<SubmitVo>> submitList(@RequestBody BaseVo<SubmitVo> vo) {
        try {
            HistoricProcessInstanceQuery query = historyService.createHistoricProcessInstanceQuery().startedBy(SessionUser.getCurrentUser().getId().toString());
            if(!StringUtils.isEmpty(vo.getEntity().getProcDefiId())){
                query = query.processDefinitionId(vo.getEntity().getProcDefiId());
            }
            if(!StringUtils.isEmpty(vo.getEntity().getMetadataType())){
                //查询对应的流程定义key
                List<String> keyList = this.getKeyList(vo.getEntity().getMetadataType());
                if(!CollectionUtils.isEmpty(keyList)){
                    query.processDefinitionKeyIn(keyList);
                }
            }
            query = query.orderByProcessInstanceStartTime().desc();
            List<HistoricProcessInstance> instList = query.listPage((vo.getPager().getCurrent()-1)*vo.getPager().getSize(), vo.getPager().getSize());

            //查询总数
            long count = query.count();

            //填充数据
            List<SubmitVo> submitList = flowMetadataService.fillSubmitVo(instList);

            Pager pager = flowCommonService.getPager(submitList, count, vo.getPager().getCurrent(), vo.getPager().getSize());
            return JsonResult.success(pager);
        }catch (Exception e){
            log.error("我的提交异常:{}", e.getMessage());
            return JsonResult.fail();
        }
    }

    /**
     * 根据元数据类型查询对应的流程key列表
     */
    private List<String> getKeyList(String metadataType){
        List<String> keyList = new ArrayList<>();
        try {
            List<MetadataConfig> configList = metadataConfigMapper.selectList(new PropertyWrapper<>(MetadataConfig.class).eq("metadataType", metadataType).getWrapper());
            if(!CollectionUtils.isEmpty(configList)){
                for(MetadataConfig config: configList){
                    if(!keyList.contains(config.getProcessKey())){
                        keyList.add(config.getProcessKey());
                    }
                }

            }
        }catch (Exception e){
            log.error("根据元数据类型查询对应的流程key列表异常：{}", e.getMessage());
        }
        return keyList;
    }

    /**
     * 获取审核状态对应的流程实例id
     * @param auditStatus 审核状态
     */
    private List<String> getProcInstList(String auditStatus){
        try {
            List<String> list = new ArrayList<>();

        }catch (Exception e){
            log.error("获取审核状态对应的流程实例id异常：{}", e.getMessage());
        }
        return null;
    }

}
