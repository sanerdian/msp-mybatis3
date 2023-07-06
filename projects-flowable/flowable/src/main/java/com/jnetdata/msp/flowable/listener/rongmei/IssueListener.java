package com.jnetdata.msp.flowable.listener.rongmei;

import com.jnetdata.msp.flowable.enums.NewsStatus;
import com.jnetdata.msp.flowable.service.common.ListenerService;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.ExecutionListener;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * 已发环节监听器
 */
@Component
public class IssueListener implements ExecutionListener, ApplicationContextAware {

    private static  ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext arg0) throws BeansException {
        applicationContext = arg0;
    }

    @Override
    public void notify(DelegateExecution execution) {
        ListenerService listenerService = (ListenerService) applicationContext.getBean("listenerService");
        //由于项目需求为定时发布，所以状态改为待发布，具体发布时间由其它
        listenerService.updateWhenStartNode(execution.getProcessInstanceId(), NewsStatus.TO_ISSUE.getCode());
    }
}
