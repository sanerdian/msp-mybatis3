package com.jnetdata.msp.flowable.service.common;

import com.jnetdata.msp.core.model.util.SessionUser;
import com.jnetdata.msp.flowable.enums.AuditStatus;
import com.jnetdata.msp.flowable.mapper.FlowMetadataMapper;
import com.jnetdata.msp.flowable.model.FlowMetadata;
import com.jnetdata.msp.flowable.util.FlowUtil;
import com.jnetdata.msp.flowable.vo.WorkVo;
import lombok.extern.slf4j.Slf4j;
import org.flowable.bpmn.model.*;
import org.flowable.common.engine.impl.identity.Authentication;
import org.flowable.engine.HistoryService;
import org.flowable.engine.IdentityService;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.TaskService;
import org.flowable.engine.history.HistoricProcessInstance;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.idm.api.User;
import org.flowable.task.api.Task;
import org.flowable.task.api.history.HistoricTaskInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.thenicesys.data.api.Pager;
import org.thenicesys.mybatis.impl.PropertyWrapper;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 工作流相关的公共方法
 */
@Slf4j
@Service
public class FlowCommonService {

    @Autowired
    private RepositoryService repositoryService;
    @Resource
    private FlowMetadataMapper flowMetadataMapper;
    @Autowired
    private TaskService taskService;
    @Autowired
    private FlowUserService flowUserService;
    @Autowired
    private HistoryService historyService;
    @Autowired
    private IdentityService identityService;

    /**
     * 判断当前任务是否第一个节点
     */
    public boolean isFirst(Task task){
        try {
            //模型对象
            BpmnModel bpmnModel = repositoryService.getBpmnModel(task.getProcessDefinitionId());
            //当前节点信息
            FlowNode flowNode = (FlowNode) bpmnModel.getFlowElement(task.getTaskDefinitionKey());
            //当前节点的输入连线
            List<SequenceFlow> inFlows = flowNode.getIncomingFlows();
            //遍历，如果某一个输入连线的源节点是开始事件，返回true
            for (SequenceFlow sequenceFlow : inFlows){
                FlowElement source = sequenceFlow.getSourceFlowElement();
                if(source  instanceof StartEvent){
                    return true;
                }
            }
        }catch (Exception e){
            log.error("判断当前任务是否第一个节点异常：{}");
        }
        return false;
    }

    /**
     * 完成任务后修改“任务id”、“当前处理人id”、“当前处理人姓名”、“审核时间”
     * @param procInstId 流程实例id
     * @param isAudit 是否设置审核时间（提交、撤回、转交时不需要设置审核时间）
     */
    public void updateAfterComplete(String procInstId, boolean isAudit){
        try {
            Task newTask = taskService.createTaskQuery().processInstanceId(procInstId).singleResult();

            FlowMetadata metadata = flowMetadataMapper.selectById(procInstId);
            Map map = FlowUtil.initMap(metadata.getMetadataTable(), metadata.getMetadataId());

            //正常审核，设置审核时间
            if(isAudit){
                map.put("auditTime", new Date());
            }

            //流程未结束，设置“任务id”、“当前处理人id”、“当前处理人姓名”
            String taskHandler = null;
            if(!ObjectUtils.isEmpty(newTask)){
                List<User> userList = flowUserService.getAuditor(newTask);
                List<String> users = this.getUserStr(userList);
                if(!CollectionUtils.isEmpty(userList) && userList.size() == 1){
                    taskService.setAssignee(newTask.getId(), userList.get(0).getId());
                }

                map.put("taskId", newTask.getId());
                map.put("auditorId", users.get(0));
                map.put("auditorName", users.get(1));
                map.put("procInstId", newTask.getProcessInstanceId());
                taskHandler = users.get(0);
            }

            flowMetadataMapper.updateMetadata(map);

            //更新关联信息中的当前处理人
            this.updateRelation(metadata, null, taskHandler);
        }catch (Exception e){
            log.error(e.getMessage());
        }
    }

    /**
     * 获取用户id和姓名连接的字符串
     */
    private List<String> getUserStr(List<User> userList){
        List<String> list = new ArrayList<>();
        if(CollectionUtils.isEmpty(userList)){
            list.add("");
            list.add("");
        }else{
            StringBuilder ids = new StringBuilder();
            StringBuilder names = new StringBuilder();
            for(User user: userList){
                ids.append(user.getId()).append(",");
                names.append(user.getDisplayName()).append(",");
            }
            if(ids.length() > 0){
                list.add(ids.substring(0, ids.length()-1));
            }else {
                list.add("");
            }
            if(names.length() > 0){
                list.add(names.substring(0, names.length()-1));
            }else {
                list.add("");
            }
        }
        return list;
    }



    /**
     * 更新关联信息中的审核状态和当前处理人
     * @param metadata 关联信息
     * @param auditStatus 审核状态
     * @param taskHandler 当前处理人
     */
    private void updateRelation(FlowMetadata metadata, Integer auditStatus, String taskHandler){
        try {
            FlowMetadata po = new FlowMetadata();
            po.setId(metadata.getId());
            if(!StringUtils.isEmpty(auditStatus)){
                po.setAuditStatus(auditStatus);
            }
            po.setTaskTandler(StringUtils.isEmpty(taskHandler) ? "" : taskHandler);
            flowMetadataMapper.updateById(po);
        }catch (Exception e){
            log.error("更新关联信息中的审核状态和当前处理人异常：{}", e.getMessage());
        }
    }

    /**
     * 任务被驳回或撤回任务时更新数据
     */
    public void updateWhenBack(String procInstId, String auditStatus){
        try {
            Task task = taskService.createTaskQuery().processInstanceId(procInstId).singleResult();
            FlowMetadata metadata = flowMetadataMapper.selectById(task.getProcessInstanceId());
            Map map = FlowUtil.initMap(metadata.getMetadataTable(), metadata.getMetadataId());
            map.put("auditStatus", auditStatus);
            List<User> userList = flowUserService.getAuditor(task);
            List<String> users = this.getUserStr(userList);
            map.put("taskId", task.getId());
            map.put("auditorId", users.get(0));
            map.put("auditorName", users.get(1));

            //任务被驳回时，需要记录审核时间
            if(AuditStatus.REJECTED.getCode().equals(auditStatus)){
                map.put("auditTime", new Date());
            }
            flowMetadataMapper.updateMetadata(map);

            //更新关联信息中的审核状态和当前处理人
            this.updateRelation(metadata, Integer.parseInt(auditStatus), users.get(0));
        }catch (Exception e){
            log.error("任务被驳回或撤回任务时更新数据异常：{}", e.getMessage());
        }
    }

    /**
     * 获取第一个用户任务（即编辑节点）
     */
    public HistoricTaskInstance getFirstTask(Task task){
        try {
            List<HistoricTaskInstance> taskList = historyService.createHistoricTaskInstanceQuery()
                    .processInstanceId(task.getProcessInstanceId()).orderByHistoricTaskInstanceStartTime().asc().list();
            if(!CollectionUtils.isEmpty(taskList)){
                return taskList.get(0);
            }
        }catch (Exception e){
            log.error("获取第一个用户任务（即编辑节点）异常：{}", e.getMessage());
        }
        return null;
    }

    /**
     * 设置流程发起人
     * 当任务分配用户为流程发起人时，变量名设置为对应的用户id
     * @param procInstId 流程实例id
     */
    public void setInitiator(String procInstId){
        try {
            //流程实例
            HistoricProcessInstance process = historyService.createHistoricProcessInstanceQuery().processInstanceId(procInstId).singleResult();

            //流程当前对应的任务
            List<Task> taskList = taskService.createTaskQuery().processInstanceId(procInstId).list();
            for(Task task: taskList){
                if("$INITIATOR".equals(task.getAssignee()) && !StringUtils.isEmpty(process.getStartUserId())){
                    taskService.setAssignee(task.getId(), process.getStartUserId());
                }
            }
        }catch (Exception e){
            log.error("设置流程发起人异常：{}", e.getMessage());
        }
    }

    /**
     * 根据流程定义id设置流程定义名称
     */
    public void setProcDefiName(List<WorkVo> voList){
        try {
            //获取流程定义id集合
            Set<String> procIds = new HashSet<>();
            for(WorkVo vo : voList){
                procIds.add(vo.getProcDefiId());
            }

            List<ProcessDefinition> defiList = repositoryService.createProcessDefinitionQuery().processDefinitionIds(procIds).list();
            Map<String, String> map = new HashMap<>();
            for(ProcessDefinition defi: defiList){
                map.put(defi.getId(), defi.getName());
            }

            for(WorkVo vo : voList){
                vo.setProcDefiName(map.get(vo.getProcDefiId()));
            }
        }catch (Exception e){
            log.error("根据流程定义id设置流程定义名称异常：{}", e.getMessage());
        }
    }

    /**
     * 根据流程发起人id设置流程发起人名称
     */
    public void setStartUserName(List<WorkVo> voList){
        try {
            //获取流程发起人id集合
            List<String> userIds = new ArrayList<>();
            for(WorkVo vo : voList){
                if(!userIds.contains(vo.getStartUserId())){
                    userIds.add(vo.getStartUserId());
                }
            }

            //获取列表并放到map中
            List<User> userList = identityService.createUserQuery().userIds(userIds).list();
            Map<String, String> map = new HashMap<>();
            for(User user: userList){
                map.put(user.getId(), user.getDisplayName());
            }

            //循环赋值
            for(WorkVo vo : voList){
                vo.setStartUserName(map.get(vo.getStartUserId()));
            }
        }catch (Exception e){
            log.error("根据流程发起人id设置流程发起人名称异常：{}", e.getMessage());
        }
    }

    /**
     * 配置分页信息
     * @param dataList 数据集合
     * @param count 数据总数
     * @param current 当前页
     * @param size 每页数据条数
     */
    public Pager getPager(List dataList, long count, Integer current, Integer size){
        //计算页数
        long remainder = count % size;
        long pages = remainder > 0 ? (count/size + 1) : count/size;

        Pager pager = new Pager();
        pager.setRecords(dataList);
        pager.setTotal((int)count);
        pager.setCurrent(current);
        pager.setSize(size);
        pager.setPages(new Long(pages).intValue());
        return pager;
    }

    /**
     * 根据id获取用户真实姓名
     * @param userId 用户id
     */
    public String getNameById(String userId){
        try{
            if(StringUtils.isEmpty(userId)){return "";}
            return identityService.createUserQuery().userId(userId).singleResult().getDisplayName();
        }catch (Exception e){
            log.error("根据id获取用户真实姓名异常：{}", e.getMessage());
            return "";
        }
    }
}
