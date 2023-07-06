package com.jnetdata.msp.core.model.util;

/**
 * 当前用户信息
 */
public interface ISessionUser {

    /**
     * 获取当前登录用户
     * 若果当前环境未有登录用户，抛出异常
     */
    IUser<Long> getCurrentUser();

    /**
     * 获取当前登录用户
     * 若果当前环境未有登录用户，返回null
     */
    IUser<Long> getCurrentUserWithoutException();

    /**
     * 获取当前登录主体接口
     * @return
     */
    ISubject getSubject();

}
