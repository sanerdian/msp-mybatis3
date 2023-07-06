package com.jnetdata.msp.flowable.listener.zhengys;

import com.jnetdata.msp.flowable.enums.AuditStatus;
import com.jnetdata.msp.flowable.service.zhengys.ZhengysListenerService;
import org.flowable.bpmn.model.UserTask;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.ExecutionListener;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * 本部门的节点监听器
 * 处室数据资源管理员节点、处长节点
 * （政研社项目）
 */
@Component
public class SelfDeptListener implements ExecutionListener, ApplicationContextAware {

    private static  ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext arg0) throws BeansException {
        applicationContext = arg0;
    }

    public void notify(DelegateExecution execution) {
        //获取节点设置的用户组id，即角色id
        UserTask userTask = (UserTask)execution.getCurrentFlowElement();
        String groupId = userTask.getCandidateGroups().get(0);
        ZhengysListenerService listenerService = (ZhengysListenerService) applicationContext.getBean("zhengysListenerService");
        listenerService.selfDeptHandle(execution.getProcessInstanceId(), AuditStatus.AUDITING.getCode(), groupId);
    }
}
