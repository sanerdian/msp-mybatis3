package com.jnetdata.msp.member.business.service;

import com.jnetdata.msp.member.business.model.Business;
import com.jnetdata.msp.core.service.BaseService;
import org.thenicesys.mybatis.impl.PropertyWrapper;

import java.util.Map;
/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zyj
 * @since 2020-07-02
 */
public interface BusinessService extends BaseService<Business> {

    PropertyWrapper<Business> makeSearchWrapper(Map<String, Object> condition);
    void add(Business business);
}
