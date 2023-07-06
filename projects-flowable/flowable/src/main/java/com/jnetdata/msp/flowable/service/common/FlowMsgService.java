package com.jnetdata.msp.flowable.service.common;

import com.jnetdata.msp.flowable.constants.FlowCons;
import com.jnetdata.msp.flowable.enums.MsgAttr;
import com.jnetdata.msp.flowable.mapper.FlowMetadataMapper;
import com.jnetdata.msp.flowable.model.FlowMetadata;
import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.*;
import org.flowable.engine.history.HistoricProcessInstance;
import org.flowable.idm.api.User;
import org.flowable.task.api.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 工作流相关的消息
 */
@Slf4j
@Service
public class FlowMsgService {

    @Resource
    private FlowMetadataMapper flowMetadataMapper;
    @Autowired
    private TaskService taskService;
    @Autowired
    private HistoryService historyService;
    @Autowired
    private FlowUserService flowUserService;

    /**
     * 消息通知
     * @param procInstId 流程实例id
     */
    public void addMsg(String procInstId){
        try{
            //获取任务信息、流程信息、元数据信息
            Task task = taskService.createTaskQuery().processInstanceId(procInstId).singleResult();;
            HistoricProcessInstance procInst = historyService.createHistoricProcessInstanceQuery().processInstanceId(procInstId).singleResult();
            FlowMetadata flowMetadata = flowMetadataMapper.selectById(procInstId);
            //任务id、元数据id
            String taskId = ObjectUtils.isEmpty(task) ? "" : task.getId();
            String metadataId = ObjectUtils.isEmpty(flowMetadata) ? "" : flowMetadata.getMetadataId();
            //通知发起人
            this.saveMsg(procInst.getStartUserId(), "您提交的流程有新的进展！", metadataId, taskId);
            if(!ObjectUtils.isEmpty(task)){
                List<User> userList = flowUserService.getAuditor(task);
                for(User user: userList){
                    //通知审核人
                    this.saveMsg(user.getId(), "有一个审核流程需要您处理！", metadataId, taskId);
                }
             }
        }catch (Exception e){
            log.error(e.getMessage());
        }

    }

    /**
     * 保存消息
     * @param receiverId 消息接收人id
     * @param msgContent 消息内容
     * @param metadataId 元数据id
     * @param flowId 任务id
     */
    private void saveMsg(String receiverId, String msgContent, String metadataId, String flowId){
        Map<String, Object> map = new HashMap<>();
        Date now = new Date();
        map.put(MsgAttr.MSG_TYPE.getName(), FlowCons.MSG_TYPE_AUDIT);
        map.put(MsgAttr.MSG_CONTENT.getName(), msgContent);
        map.put(MsgAttr.RECEIVER_ID.getName(), receiverId);
        map.put(MsgAttr.METADATA_ID.getName(), metadataId);
        map.put(MsgAttr.FLOW_ID.getName(), flowId);
        map.put(MsgAttr.CREATE_TIME.getName(), now);
        map.put(MsgAttr.MODIFY_TIME.getName(), now);
        flowMetadataMapper.addMsg(map);
    }
}
