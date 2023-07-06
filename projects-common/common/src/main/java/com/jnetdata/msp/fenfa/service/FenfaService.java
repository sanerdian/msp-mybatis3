package com.jnetdata.msp.fenfa.service;

import com.jnetdata.msp.core.service.BaseService;
import com.jnetdata.msp.fenfa.model.Fenfa;
import org.thenicesys.mybatis.impl.PropertyWrapper;

import java.util.Map;

/**
 * <p>
 * 分发 服务类
 * </p>
 *
 * @author zyj
 * @since 2021-03-16
 */
public interface FenfaService extends BaseService<Fenfa> {
        PropertyWrapper<Fenfa> makeSearchWrapper(Map<String, Object> condition);
}
