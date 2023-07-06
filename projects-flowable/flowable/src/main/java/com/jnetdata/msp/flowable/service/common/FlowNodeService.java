package com.jnetdata.msp.flowable.service.common;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jnetdata.msp.core.util.MapUtil;
import com.jnetdata.msp.flowable.enums.CommentType;
import com.jnetdata.msp.flowable.enums.Logic;
import com.jnetdata.msp.flowable.enums.NodeType;
import com.jnetdata.msp.flowable.vo.ProcessVo;
import com.jnetdata.msp.flowable.vo.StepVo;
import com.jnetdata.msp.vo.BaseVo;
import lombok.extern.slf4j.Slf4j;
import org.flowable.bpmn.model.*;
import org.flowable.bpmn.model.Process;
import org.flowable.engine.HistoryService;
import org.flowable.engine.IdentityService;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.TaskService;
import org.flowable.engine.history.HistoricProcessInstance;
import org.flowable.engine.task.Comment;
import org.flowable.idm.api.User;
import org.flowable.task.api.Task;
import org.flowable.task.api.history.HistoricTaskInstance;
import org.flowable.task.api.history.HistoricTaskInstanceQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.thenicesys.data.api.Pager;
import org.thenicesys.web.JsonResult;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 工作流节点服务
 */
@Slf4j
@Service
public class FlowNodeService {

    @Autowired
    private TaskService taskService;
    @Autowired
    private RepositoryService repositoryService;
    @Autowired
    private IdentityService identityService;
    @Autowired
    private HistoryService historyService;
    @Autowired
    private FlowCommonService flowCommonService;

    /**
     * 获取下一节点信息
     */
    public JsonResult getNextNode(String data) {
        try {
            JSONObject json = JSON.parseObject(data);
            String taskId = json.getString("taskId");
            //当前任务
            Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
            //模型对象
            BpmnModel bpmnModel = repositoryService.getBpmnModel(task.getProcessDefinitionId());
            //当前节点信息
            FlowNode flowNode = (FlowNode) bpmnModel.getFlowElement(task.getTaskDefinitionKey());
            // 当前节点输出连线
            List<SequenceFlow> outFlows = flowNode.getOutgoingFlows();
            for (SequenceFlow sequenceFlow : outFlows){
                //连线指向节点
                FlowElement targetFlow = sequenceFlow.getTargetFlowElement();
                if(targetFlow instanceof ExclusiveGateway){
                    return JsonResult.success(NodeType.EXCLUSIVE_GATEWAY.getCode());
                }else if(targetFlow instanceof EndEvent){
                    return JsonResult.success(NodeType.END_EVENT.getCode());
                }
            }
            return JsonResult.success("0");
        }catch (Exception e){
            log.error(e.getMessage());
            return JsonResult.fail();
        }
    }

    /**
     * 操作列表
     */
    public JsonResult operateList(BaseVo<ProcessVo> vo){
        try {
            //校验是否有对应的流程信息
            if(StringUtils.isEmpty(vo.getEntity().getProcInstId())){
                return JsonResult.fail(String.valueOf(HttpStatus.BAD_REQUEST.value()), "流程实例id(procInstId)不能为空！");
            }
            HistoricProcessInstance procInst = historyService.createHistoricProcessInstanceQuery().processInstanceId(vo.getEntity().getProcInstId()).singleResult();
            if(ObjectUtils.isEmpty(procInst)){
                return JsonResult.fail(String.valueOf(HttpStatus.BAD_REQUEST.value()),"没有对应的流程信息");
            }

            //筛选数据需要的类型列表
            List<String> typeList = new ArrayList<>();
            for(CommentType commentType: CommentType.values()){
                typeList.add(commentType.getCode());
            }

            //获取数据
            List<Comment> commentList = taskService.getProcessInstanceComments(procInst.getId());
            if(CollectionUtils.isEmpty(commentList)){
                commentList = new ArrayList<>();
            }
            //按创建时间排序
            Collections.sort(commentList, new Comparator<Comment>() {
                @Override
                public int compare(Comment o1, Comment o2) {
                    long diff = o1.getTime().getTime() - o2.getTime().getTime();
                    if(diff > 0){
                        return 1;
                    }else if(diff < 0){
                        return -1;
                    }else{
                        return 0;
                    }
                }
            });

            //筛选数据
            List<Map<String, Object>> maps = MapUtil.toListMapForFlowable(commentList);
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            for(Map<String, Object> map: maps){
                String type = (String)map.get("type");
                if(typeList.contains(type)){
                    map.put("typeName", CommentType.getName(type));
                    map.put("userName", flowCommonService.getNameById((String)map.get("userId")));
                    map.put("timeStr", format.format((Date)map.get("time")));
                }
            }

            Pager pager = new Pager();
            pager.setRecords(maps);
            pager.setTotal(maps.size());
            pager.setCurrent(vo.getPager().getCurrent());
            pager.setSize(vo.getPager().getSize());
            return JsonResult.success(pager);
        }catch (Exception e){
            log.error("操作列表异常：{}", e.getMessage());
            return JsonResult.fail();
        }
    }

    /**
     * 节点列表
     */
    public JsonResult nodeList(ProcessVo vo){
        try {
            //校验是否有对应的流程信息
            if(StringUtils.isEmpty(vo.getProcInstId()) ){
                return JsonResult.fail(String.valueOf(HttpStatus.BAD_REQUEST.value()), "流程实例id(procInstId)不能为空！");
            }
            HistoricProcessInstance procInst = historyService.createHistoricProcessInstanceQuery().processInstanceId(vo.getProcInstId()).singleResult();
            if(ObjectUtils.isEmpty(procInst)){
                return JsonResult.fail(String.valueOf(HttpStatus.BAD_REQUEST.value()),"没有对应的流程信息");
            }

            //当前任务
            Task currentTask = taskService.createTaskQuery().processInstanceId(procInst.getId()).singleResult();
            //所有任务
            List<UserTask> taskList = this.getAllNode(procInst.getProcessDefinitionId());
            //已处理任务
            Map<String, String> auditors = this.getAuditors(procInst.getId());

            //数据匹配：已处理任务显示处理人、从开始到当前任务高点显示
            List<Map<String, String>> result = new ArrayList<>();
            //默认所有节点都没通过
            String passed = Logic.NO.getCode();
            //从后往前，获取节点名称、审核人、是否通过
            for(int i=(taskList.size()-1); i>=0; i--){
                Map<String, String> map = new HashMap<>();
                //流程未结束，当前节点和之前设置为已经过；否则，历史节点设置为已经过
                if(ObjectUtils.isEmpty(procInst.getEndTime())){
                    if(!ObjectUtils.isEmpty(currentTask) && taskList.get(i).getId().equals(currentTask.getTaskDefinitionKey())){
                        //当前节点和之前的接口设置为已通过
                        passed = Logic.YES.getCode();
                    }
                    if(auditors.containsKey(taskList.get(i).getId())){
                        map.put("nodeName", taskList.get(i).getName() + "：" + auditors.get(taskList.get(i).getId()));
                    }else{
                        map.put("nodeName", taskList.get(i).getName());
                    }
                    map.put("passed", passed);
                }else{
                    if(auditors.containsKey(taskList.get(i).getId())){
                        map.put("nodeName", taskList.get(i).getName() + "：" + auditors.get(taskList.get(i).getId()));
                        map.put("passed", Logic.YES.getCode());
                    }else{
                        map.put("nodeName", taskList.get(i).getName());
                        map.put("passed", Logic.NO.getCode());
                    }
                }
                result.add(map);
            }
            Collections.reverse(result);
            return JsonResult.success(result);
        }catch (Exception e){
            log.error(e.getMessage());
            return JsonResult.fail();
        }
    }


    /**
     * 获取全部用户任务
     * @param procDefiId 流程定义id
     */
    private List<UserTask> getAllNode(String procDefiId){
        //获取流程图各元素
        BpmnModel bpmnModel = repositoryService.getBpmnModel(procDefiId);
        List<Process> procList = bpmnModel.getProcesses();
        List<FlowElement> flowElementList = new ArrayList<>();
        for(Process process: procList){
            flowElementList.addAll(process.getFlowElements());
        }

        //获取开始事件、结束事件、用户任务列表
        Map<String, FlowElement> map = new HashMap<>();
        StartEvent startEvent = null;
        EndEvent endEvent = null;
        List<UserTask> taskList = new ArrayList<>();
        for(FlowElement flowElement: flowElementList){
            map.put(flowElement.getId(), flowElement);
            if(flowElement instanceof StartEvent){
                startEvent = (StartEvent)flowElement;
            }else if(flowElement instanceof EndEvent){
                endEvent = (EndEvent)flowElement;
            }else if(flowElement instanceof UserTask){
                taskList.add((UserTask)flowElement);
            }
        }

        //开始事件到非用户任务之间的用户任务列表（顺序排列）
        List<UserTask> ascList = new ArrayList<>();
        UserTask nextTask = this.getNextTask(startEvent.getId(), map);
        while (!ObjectUtils.isEmpty(nextTask)){
            ascList.add(nextTask);
            nextTask = this.getNextTask(nextTask.getId(), map);
        }
        if(ascList.size() == taskList.size()){return ascList;}
        if(taskList.size() - ascList.size() == 1){return this.getSumList(taskList, ascList, new ArrayList<>());}

        //非用户任务到结束事件之间的用户任务列表（倒序排列）
        List<UserTask> descList = new ArrayList<>();
        UserTask previousTask = this.getPreviousTask(endEvent.getId(), map);
        while (!ObjectUtils.isEmpty(previousTask)){
            descList.add(previousTask);
            previousTask = this.getPreviousTask(previousTask.getId(), map);
        }
        if(descList.size() == taskList.size()){return descList;}

        return this.getSumList(taskList, ascList, descList);
    }

    /**
     * 获取下一个用户任务
     */
    private UserTask getNextTask(String userTaskId, Map<String, FlowElement> map){
        for(Map.Entry<String, FlowElement> entity: map.entrySet()){
            //连线
            if(entity.getValue() instanceof SequenceFlow){
                SequenceFlow sequenceFlow = (SequenceFlow)entity.getValue();
                //连线的起点是用户任务，获取连线的终点
                if(sequenceFlow.getSourceRef().equals(userTaskId)){
                    FlowElement next = sequenceFlow.getTargetFlowElement();
                    //连线终点是用户任务，返回元素；否则，返回null
                    if(next instanceof UserTask){
                        return (UserTask)next;
                    }
                }
            }
        }
        return null;
    }

    /**
     * 获取上一个用户任务
     */
    private UserTask getPreviousTask(String userTaskId, Map<String, FlowElement> map){
        for(Map.Entry<String, FlowElement> entity: map.entrySet()){
            //连线
            if(entity.getValue() instanceof SequenceFlow){
                SequenceFlow sequenceFlow = (SequenceFlow)entity.getValue();
                //连线的终点是用户任务，获取连线的起点
                if(sequenceFlow.getTargetRef().equals(userTaskId)){
                    FlowElement previous = sequenceFlow.getSourceFlowElement();
                    //连线起点是用户任务，返回元素；否则，返回null
                    if(previous instanceof UserTask){
                        return (UserTask)previous;
                    }
                }
            }
        }
        return null;
    }

    /**
     * 按顺序整理用户任务列表
     * @param taskList 用户任务列表
     * @param ascList 开始事件到非用户任务之间的用户任务列表（顺序排列）
     * @param descList 非用户任务到结束事件之间的用户任务列表（倒序排列）
     * @return
     */
    private List<UserTask> getSumList(List<UserTask> taskList, List<UserTask> ascList, List<UserTask> descList){
        List<UserTask> sumList = new ArrayList<>();
        if(taskList.size() - ascList.size() - descList.size() != 1){return null;}
        List<String> idList = new ArrayList<>();
        for(UserTask task: ascList){
            idList.add(task.getId());
        }
        for(UserTask task: descList){
            idList.add(task.getId());
        }
        sumList.addAll(ascList);
        for(UserTask task: taskList){
            if(!idList.contains(task.getId())){
                sumList.add(task);
                break;
            }
        }
        for(int i=(descList.size()-1); i>=0; i--){
            sumList.add(descList.get(i));
        }
        return sumList;
    }

    /**
     * 获取节点处理人列表
     * @param procInstId
     * @return
     */
    private Map<String, String> getAuditors(String procInstId){
        Map<String, String> map = new HashMap<>();
        List<HistoricTaskInstance> taskList = historyService.createHistoricTaskInstanceQuery()
                .processInstanceId(procInstId).orderByHistoricTaskInstanceStartTime().desc().list();

        for(HistoricTaskInstance task: taskList){
            if(!map.containsKey(task.getTaskDefinitionKey()) && !StringUtils.isEmpty(task.getAssignee())){
                User user = identityService.createUserQuery().userId(task.getAssignee()).singleResult();
                map.put(task.getTaskDefinitionKey(), ObjectUtils.isEmpty(user) ? "" : user.getDisplayName());
            }
        }
        return map;
    }

    /**
     * 流程步骤列表
     */
    public JsonResult<Pager<StepVo>> stepList(BaseVo<StepVo> vo){
        try {
            if(StringUtils.isEmpty(vo.getEntity().getProcInstId())){
                return JsonResult.fail(String.valueOf(HttpStatus.BAD_REQUEST.value()), "流程实例id(procInstId)不能为空");
            }

            //获取流程关联的任务
            HistoricTaskInstanceQuery query = historyService.createHistoricTaskInstanceQuery()
                    .processInstanceId(vo.getEntity().getProcInstId()).orderByTaskCreateTime().asc();
            List<HistoricTaskInstance> taskList = query.listPage((vo.getPager().getCurrent() - 1) * vo.getPager().getSize(), vo.getPager().getSize());
            long count = query.count();

            //获取属性
            List<StepVo> stepList = new ArrayList<>();
            long now = System.currentTimeMillis();
            for(HistoricTaskInstance task: taskList){
                StepVo stepVo = new StepVo();
                stepVo.setStepName(task.getName());
                stepVo.setUserName(flowCommonService.getNameById(task.getAssignee()));
                stepVo.setStepStatus(ObjectUtils.isEmpty(task.getEndTime()) ? "办理中" : "已办结");
                stepVo.setDuration(ObjectUtils.isEmpty(task.getEndTime()) ? (now - task.getCreateTime().getTime()) : task.getDurationInMillis());
                stepVo.setReceiveTime(task.getCreateTime());
                stepVo.setEndTime(task.getEndTime());
                stepList.add(stepVo);
            }

            //返回
            Pager<StepVo> pager = flowCommonService.getPager(stepList, count, vo.getPager().getCurrent(), vo.getPager().getSize());
            return JsonResult.success(pager);
        }catch (Exception e){
            log.error("流程步骤列表异常:{}", e.getMessage());
        }
        return JsonResult.fail();
    }
}
