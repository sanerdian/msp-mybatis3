package com.jnetdata.msp.core.model.util;

import org.apache.shiro.util.Assert;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

/**
 * @author Administrator
 */
@Service
public class SessionUser implements ApplicationContextAware {

    public static IUser<Long> getCurrentUser(){
        return getSessionUser().getCurrentUser();
    }

    public static ISubject getSubject() {
        return getSessionUser().getSubject();
    }

    /*
    * 获取当前登录用户
    * 若当前没有登录的用户则返回异常
    * @author hongshou
    * @date 2020/5/26
    * */
    public static IUser<Long> getCurrentUserWithoutException() {
        return getSessionUser().getCurrentUserWithoutException();//获取当前登录用户
    }

    private static ISessionUser getSessionUser() {
        ISessionUser bean = context.getBean(ISessionUser.class);
        Assert.notNull(bean, "不能找到类型为 "+ISessionUser.class.getName()+" 的bean");
        return bean;
    }

    private static ApplicationContext context;
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        context = applicationContext;
    }

}
