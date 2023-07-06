package com.jnetdata.msp.flowable.service.common;

import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.HistoryService;
import org.flowable.engine.IdentityService;
import org.flowable.engine.TaskService;
import org.flowable.identitylink.api.history.HistoricIdentityLink;
import org.flowable.idm.api.User;
import org.flowable.idm.engine.impl.persistence.entity.UserEntityImpl;
import org.flowable.task.api.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class FlowUserService {
    @Autowired
    private TaskService taskService;
    @Autowired
    private IdentityService identityService;
    @Autowired
    private HistoryService historyService;

    /**
     * 获取当前审核人列表
     */
    public List<User> getAuditor(Task task){
        List<User> userList = new ArrayList<>();
        try {
            if(ObjectUtils.isEmpty(task)){
                log.error("任务信息为空，无法获取审核人信息");
                return null;
            }
            if(!StringUtils.isEmpty(task.getAssignee())){
                //指定用户
                User user = identityService.createUserQuery().userId(task.getAssignee()).singleResult();
                userList.add(user);
            }else{
                //候选用户、用户组
                List<HistoricIdentityLink> linkList = historyService.getHistoricIdentityLinksForTask(task.getId());
                for(HistoricIdentityLink link: linkList){
                    if(!StringUtils.isEmpty(link.getUserId())){
                        User user = identityService.createUserQuery().userId(link.getUserId()).singleResult();
                        userList.add(user);
                    }else if(!StringUtils.isEmpty(link.getGroupId())){
                        String sql = "select t1.* from act_id_user t1, act_id_membership t2 where t1.id_ = t2.user_id_ and t2.group_id_ = #{groupId}";
                        List<User> tempList = identityService.createNativeUserQuery().sql(sql).parameter("groupId", link.getGroupId()).listPage(0, 100);
                        if(CollectionUtils.isEmpty(tempList)){continue;}
                        userList.addAll(tempList);
                    }
                }
            }
        }catch (Exception e){
            log.error("获取当前审核人异常，taskId:{}, procInstId:{}, 异常信息:{}", task.getId(), task.getProcessInstanceId(), e.getMessage());
            e.printStackTrace();
        }
        return userList;
    }
}
