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
 * 流程终止监听器
 */
@Component
public class TerminateListener implements ExecutionListener, ApplicationContextAware {

    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext arg0) throws BeansException {
        applicationContext = arg0;
    }

    @Override
    public void notify(DelegateExecution execution) {
        ListenerService listenerService = (ListenerService) applicationContext.getBean("listenerService");
        listenerService.updateForEnd(execution.getProcessInstanceId(), AuditStatus.TERMINATED.getCode());
    }
}
