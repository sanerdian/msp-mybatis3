package com.jnetdata.msp.flowable.service.common;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jnetdata.msp.core.model.util.SessionUser;
import com.jnetdata.msp.flowable.constants.FlowCons;
import com.jnetdata.msp.flowable.enums.AuditStatus;
import com.jnetdata.msp.flowable.enums.CommentType;
import com.jnetdata.msp.flowable.mapper.FlowMetadataMapper;
import com.jnetdata.msp.flowable.model.FlowMetadata;
import com.jnetdata.msp.flowable.service.FlowMetadataService;
import com.jnetdata.msp.flowable.service.common.FlowCommonService;
import com.jnetdata.msp.flowable.service.common.FlowMsgService;
import com.jnetdata.msp.flowable.util.FlowUtil;
import com.jnetdata.msp.flowable.vo.TaskVo;
import lombok.extern.slf4j.Slf4j;
import org.flowable.common.engine.impl.identity.Authentication;
import org.flowable.engine.HistoryService;
import org.flowable.engine.IdentityService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.idm.api.User;
import org.flowable.task.api.Task;
import org.flowable.task.api.history.HistoricTaskInstance;
import org.flowable.task.service.impl.persistence.entity.TaskEntityImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.thenicesys.web.JsonResult;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 工作流任务
 */
@Slf4j
@Service
public class FlowTaskService {

    @Autowired
    private TaskService taskService;
    @Autowired
    private FlowCommonService flowCommonService;
    @Autowired
    private FlowMsgService flowMsgService;
    @Autowired
    private RuntimeService runtimeService;
    @Resource
    private FlowMetadataMapper flowMetadataMapper;
    @Autowired
    private IdentityService identityService;
    @Autowired
    private HistoryService historyService;

    /**
     * 完成任务
     */
    public JsonResult completeTask(TaskVo vo) {
        try{
            //参数校验
            JSONObject json = this.getParams(vo);
            if(StringUtils.isEmpty(vo.getTaskIds())){
                return JsonResult.fail(String.valueOf(HttpStatus.BAD_REQUEST.value()), "任务id列表(taskIds)不能为空");
            }
            //执行
            Task task = taskService.createTaskQuery().taskId(vo.getTaskIds().get(0)).singleResult();
            if(ObjectUtils.isEmpty(task)){
                return JsonResult.fail(String.valueOf(HttpStatus.BAD_REQUEST.value()), "没有对应的任务");
            }else if(task.isSuspended()){
                return JsonResult.fail(String.valueOf(HttpStatus.BAD_REQUEST.value()), "流程被挂起");
            }
            this.completeTaskCommon(task, json);
            return JsonResult.success();
        }catch (Exception e){
            log.error("完成任务异常：{}", e.getMessage());
            return JsonResult.fail();
        }
    }

    /**
     * 批量完成任务
     */
    public JsonResult completeTaskBatch(TaskVo vo){
        try{
            //参数校验
            JSONObject json = this.getParams(vo);
            if(StringUtils.isEmpty(vo.getTaskIds())){
                return JsonResult.fail(String.valueOf(HttpStatus.BAD_REQUEST.value()), "任务id列表(taskIds)不能为空");
            }
            //批量执行
            for(String taskId : vo.getTaskIds()){
                Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
                if(ObjectUtils.isEmpty(task) || task.isSuspended()){
                    continue;//任务为空，或流程被挂起，忽略
                }
                this.completeTaskCommon(task, json);
            }
            return JsonResult.success();
        }catch (Exception e){
            log.error(e.getMessage());
            return JsonResult.fail();
        }
    }

    /**
     * 获取任务相关参数
     */
    private JSONObject getParams(TaskVo vo){
        JSONObject json = new JSONObject();
        if(!StringUtils.isEmpty(vo.getAuditOpinion())){
            json.put(FlowCons.AUDIT_OPINION, vo.getAuditOpinion());
        }
        if(!StringUtils.isEmpty(vo.getAuditParam())){
            json.put(FlowCons.AUDIT_PARAM, vo.getAuditParam());
        }
        if(!StringUtils.isEmpty(vo.getAuditUserId())){
            json.put(FlowCons.AUDIT_USER_ID, vo.getAuditUserId());
        }
        return json;
    }

    public void completeTaskCommon(Task task, JSONObject json){
        //保存意见信息
        Authentication.setAuthenticatedUserId(SessionUser.getCurrentUser().getId().toString());
        String auditOpinion = StringUtils.isEmpty(json.getString(FlowCons.AUDIT_OPINION)) ? "" : json.getString(FlowCons.AUDIT_OPINION);
        String commentType = flowCommonService.isFirst(task) ? CommentType.SUBMIT.getCode() : CommentType.AGREE.getCode();
        taskService.addComment(task.getId(), task.getProcessInstanceId(), commentType, auditOpinion);

        //完成任务
        if(StringUtils.isEmpty(task.getAssignee())){
            taskService.setAssignee(task.getId(), SessionUser.getCurrentUser().getId().toString());
        }
        taskService.complete(task.getId(), json);

        //设置流程发起人
        flowCommonService.setInitiator(task.getProcessInstanceId());

        //添加通知消息
        flowMsgService.addMsg(task.getProcessInstanceId());

        //更新数据
        flowCommonService.updateAfterComplete(task.getProcessInstanceId(), !flowCommonService.isFirst(task));
    }

    /**
     * 批量驳回任务到第一环节
     */
    public JsonResult toFirstBatch(TaskVo vo) {
        try {
            //参数校验
            JSONObject json = this.getParams(vo);
            if(StringUtils.isEmpty(vo.getTaskIds())){
                return JsonResult.fail(String.valueOf(HttpStatus.BAD_REQUEST.value()), "任务id列表(taskIds)不能为空");
            }
            //批量执行
            for(String taskId : vo.getTaskIds()){
                Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
                if(ObjectUtils.isEmpty(task)){continue;}
                this.toFirstCommon(task, json);
            }
            return JsonResult.success();
        }catch (Exception e){
            log.error("批量驳回任务到第一环节异常：{}");
        }
        return JsonResult.fail();
    }

    /**
     * 驳回任务到第一环节
     */
    public JsonResult toFirst(TaskVo vo) {
        //参数校验
        JSONObject json = this.getParams(vo);
        if(StringUtils.isEmpty(vo.getTaskIds())){
            return JsonResult.fail(String.valueOf(HttpStatus.BAD_REQUEST.value()), "任务id列表(taskIds)不能为空");
        }

        //执行
        Task task = taskService.createTaskQuery().taskId(vo.getTaskIds().get(0)).singleResult();
        if(ObjectUtils.isEmpty(task)){
            return JsonResult.fail(String.valueOf(HttpStatus.BAD_REQUEST.value()), "没有对应的任务");
        }
        this.toFirstCommon(task, json);
        return JsonResult.success();
    }


    private boolean toFirstCommon(Task task, JSONObject json) {
        try{
            //数据校验
            HistoricTaskInstance firstTask = flowCommonService.getFirstTask(task);
            if(ObjectUtils.isEmpty(firstTask)){
                log.error("后台数据异常, 任务id：{}", task.getId());
                return false;
            }else if(task.getTaskDefinitionKey().equals(firstTask.getTaskDefinitionKey())){
                log.info("已经回到编辑节点，无法驳回, 任务id：{}", task.getId());
                return false;
            }

            //保存审核意见信息
            Authentication.setAuthenticatedUserId(SessionUser.getCurrentUser().getId().toString());
            taskService.addComment(task.getId(), task.getProcessInstanceId(), CommentType.REJECT.getCode(), json.getString("auditOpinion"));

            //跳转流程节点
            this.runtimeService.createChangeActivityStateBuilder().processInstanceId(task.getProcessInstanceId())
                    .moveActivityIdTo(task.getTaskDefinitionKey(), firstTask.getTaskDefinitionKey()).changeState();

            //获取新任务并设置审核人
            Task newTask = taskService.createTaskQuery().processInstanceId(task.getProcessInstanceId()).singleResult();
            taskService.setAssignee(newTask.getId(), firstTask.getAssignee());

            //消息通知
            flowMsgService.addMsg(newTask.getProcessInstanceId());

            //更新元数据
            flowCommonService.updateWhenBack(task.getProcessInstanceId(), AuditStatus.REJECTED.getCode());
            return true;
        }catch (Exception e){
            log.error("驳回任务到第一环节异常：{}", e.getMessage());
            return false;
        }
    }

    /**
     * 认领任务
     */
    public JsonResult takeWork(TaskVo vo) {
        try {
            String id = vo.getTaskIds().get(0);
            TaskEntityImpl task = (TaskEntityImpl)taskService.createTaskQuery().taskId(id).singleResult();
            if(!ObjectUtils.isEmpty(task)){
                taskService.claim(id, SessionUser.getCurrentUser().getId().toString());
                return JsonResult.success("认领任务成功");
            }else{
                return JsonResult.fail("没有对应的任务");
            }
        }catch (Exception e){
            log.error(e.getMessage());
        }
        return JsonResult.fail("认领任务失败");
    }

    /**
     * 转办任务
     */
    public JsonResult transferTask(String data) {
        try {
            //参数校验：任务id和用户id不能为空
            JSONObject json = JSON.parseObject(data);
            String taskId = json.getString("taskId");
            String userId = json.getString("userId");
            if(StringUtils.isEmpty(taskId) || StringUtils.isEmpty(userId)){
                return JsonResult.fail(String.valueOf(HttpStatus.BAD_REQUEST.value()),"任务id和用户id不能为空");
            }

            //任务信息
            Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
            //保存意见信息
            Authentication.setAuthenticatedUserId(SessionUser.getCurrentUser().getId().toString());
            String auditOpinion = StringUtils.isEmpty(json.getString(FlowCons.AUDIT_OPINION)) ? "" : json.getString(FlowCons.AUDIT_OPINION);
            taskService.addComment(task.getId(), task.getProcessInstanceId(), CommentType.TRANSFER.getCode(), auditOpinion);

            //设置任务办理人
            taskService.setAssignee(taskId, userId);
            taskService.setOwner(taskId, SessionUser.getCurrentUser().getId().toString());

            //添加通知消息
            flowMsgService.addMsg(task.getProcessInstanceId());

            //更新业务数据
            FlowMetadata metadata = flowMetadataMapper.selectById(task.getProcessInstanceId());
            Map map = FlowUtil.initMap(metadata.getMetadataTable(), metadata.getMetadataId());
            User user = identityService.createUserQuery().userId(userId).singleResult();
            map.put("taskId", task.getId());
            map.put("auditorId", user.getId());
            map.put("auditorName", user.getDisplayName());
            flowMetadataMapper.updateMetadata(map);

            return JsonResult.success();
        }catch (Exception e){
            log.error(e.getMessage());
            return JsonResult.fail();
        }
    }


    /**
     * 驳回任务到上一个节点
     * 流程返回到上一个节点，适用于串行和排他网关，不适用并行网关和子流程
     */
    public JsonResult rollbackTask(String data){
        try{
            //任务信息
            JSONObject json = JSON.parseObject(data);
            String taskId = json.getString("taskId");
            if(StringUtils.isEmpty(taskId)){
                return JsonResult.fail(String.valueOf(HttpStatus.BAD_REQUEST.value()),"任务id不能为空");
            }
            Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
            if(ObjectUtils.isEmpty(task)){
                return JsonResult.fail(String.valueOf(HttpStatus.BAD_REQUEST.value()),"没有对应的任务信息");
            }

            //上一个用户任务
            HistoricTaskInstance prevTask = this.getPrevTask(task);

            //保存意见信息
            Authentication.setAuthenticatedUserId(SessionUser.getCurrentUser().getId().toString());
            String auditOpinion = StringUtils.isEmpty(json.getString(FlowCons.AUDIT_OPINION)) ? "" : json.getString(FlowCons.AUDIT_OPINION);
            taskService.addComment(task.getId(), task.getProcessInstanceId(), CommentType.REJECT.getCode(), auditOpinion);

            //跳转
            runtimeService.createChangeActivityStateBuilder().processInstanceId(task.getProcessInstanceId())
                    .moveActivityIdTo(task.getTaskDefinitionKey(), prevTask.getTaskDefinitionKey()).changeState();

            //设置任务处理人
            Task newTask = taskService.createTaskQuery().processInstanceId(task.getProcessInstanceId()).singleResult();
            taskService.setAssignee(newTask.getId(), prevTask.getAssignee());

            //添加通知消息
            flowMsgService.addMsg(newTask.getProcessInstanceId());

            //更新元数据
            flowCommonService.updateWhenBack(task.getProcessInstanceId(), AuditStatus.REJECTED.getCode());

            return JsonResult.success();
        }catch (Exception e){
            log.error("驳回任务异常：{}", e.getMessage());
            return JsonResult.fail();
        }
    }

    /**
     * 获取上一个用户任务
     */
    private HistoricTaskInstance getPrevTask(Task task){
        try{
            List<HistoricTaskInstance> taskList = historyService.createHistoricTaskInstanceQuery()
                    .processInstanceId(task.getProcessInstanceId()).orderByHistoricTaskInstanceStartTime().asc().list();
            for(int i=0; i<taskList.size(); i++){
                if(task.getTaskDefinitionKey().equals(taskList.get(i).getTaskDefinitionKey())){
                    return taskList.get(i-1);
                }
            }
        }catch (Exception e){
            log.error("获取上一个用户任务异常：{}", e.getMessage());
        }
        return null;
    }


    /**
     * 撤回任务
     * 发起人使流程返回到编辑节点
     * @param status 回到编辑节点后的状态
     */
    public JsonResult recallTask(String data, String status){
        try {
            //任务信息
            JSONObject json = JSON.parseObject(data);
            String taskId = json.getString("taskId");
            if(StringUtils.isEmpty(taskId)){
                return JsonResult.fail(String.valueOf(HttpStatus.BAD_REQUEST.value()),"任务id不能为空");
            }
            Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
            if(ObjectUtils.isEmpty(task)){
                return JsonResult.fail(String.valueOf(HttpStatus.BAD_REQUEST.value()),"没有对应的任务信息");
            }

            //获取第一个用户任务
            HistoricTaskInstance prevTask = flowCommonService.getFirstTask(task);

            //保存意见信息
            Authentication.setAuthenticatedUserId(SessionUser.getCurrentUser().getId().toString());
            String auditOpinion = StringUtils.isEmpty(json.getString(FlowCons.AUDIT_OPINION)) ? "" : json.getString(FlowCons.AUDIT_OPINION);
            taskService.addComment(task.getId(), task.getProcessInstanceId(), CommentType.RECALL.getCode(), auditOpinion);

            //跳转
            runtimeService.createChangeActivityStateBuilder().processInstanceId(task.getProcessInstanceId())
                    .moveActivityIdTo(task.getTaskDefinitionKey(), prevTask.getTaskDefinitionKey()).changeState();

            //设置任务处理人
            Task newTask = taskService.createTaskQuery().processInstanceId(task.getProcessInstanceId()).singleResult();
            taskService.setAssignee(newTask.getId(), prevTask.getAssignee());

            //添加通知消息
            flowMsgService.addMsg(newTask.getProcessInstanceId());

            //更新元数据
            flowCommonService.updateWhenBack(newTask.getProcessInstanceId(), status);

            return JsonResult.success();
        }catch (Exception e){
            log.error("撤回任务异常：{}", e.getMessage());
            return JsonResult.fail();
        }
    }
}
