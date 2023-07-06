package com.jnetdata.msp.verifycodesaver.service;

import org.apache.shiro.session.Session;

import java.io.Serializable;

/**
 * @author Administrator
 */
public interface VerifyCodeSaverService {

    /**
     * 保存session属性
     * @param session
     * @param key
     * @param value
     * @param expireTime 内容有效时间(毫秒)
     * @param maxGetTimes 最多访问次数
     */
    void set(Session session, String key, Serializable value, Long expireTime, Integer maxGetTimes);

    /**
     * 获取key, 如果访问次数超过设定自动从session中移除
     * @param session
     * @param key
     * @return
     */
    Object get(Session session, String key);

}
