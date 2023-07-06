package com.jnetdata.msp.tlujy.integral.service;

import com.jnetdata.msp.tlujy.integral.model.Integral;
import com.jnetdata.msp.core.service.BaseService;
import org.thenicesys.mybatis.impl.PropertyWrapper;

import java.util.Map;
import java.util.List;
/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zyj
 * @since 2020-09-10
 */
public interface IntegralService extends BaseService<Integral> {
        PropertyWrapper<Integral> makeSearchWrapper(Map<String, Object> condition);
        Integral getEntityAndJoinsById(Long id);
        void getListJoin(List<Integral> list, Integral vo);
}
