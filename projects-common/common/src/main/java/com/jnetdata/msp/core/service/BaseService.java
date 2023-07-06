package com.jnetdata.msp.core.service;

import org.thenicesys.data.api.util.ConditionMap;

import java.util.List;

/**
 *
 * @author Administrator
 * @date 2018/8/28
 */
public interface BaseService<T> extends org.thenicesys.data.api.BaseService<T> {
    List<T> list(ConditionMap entity);
    List<T> list(T entity);
}
