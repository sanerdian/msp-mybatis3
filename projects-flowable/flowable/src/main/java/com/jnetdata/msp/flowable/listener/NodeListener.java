package com.jnetdata.msp.flowable.listener;

import com.jnetdata.msp.flowable.enums.AuditStatus;
import com.jnetdata.msp.flowable.service.common.ListenerService;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.ExecutionListener;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * 节点监听器
 * 对于流程结束前状态均为“审核中”的流程节点
 */
@Component
public class NodeListener implements ExecutionListener, ApplicationContextAware {

    private static  ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext arg0) throws BeansException {
        applicationContext = arg0;
    }

    @Override
    public void notify(DelegateExecution execution) {
        ListenerService listenerService = (ListenerService) applicationContext.getBean("listenerService");
        listenerService.updateWhenStartNode(execution.getProcessInstanceId(), AuditStatus.AUDITING.getCode());
    }
}
