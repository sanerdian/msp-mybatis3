package com.jnetdata.msp.cs1.test_cy.service;


import com.jnetdata.msp.core.service.BaseService;
import com.jnetdata.msp.cs1.test_cy.TestCy;
import com.jnetdata.msp.vo.BaseVo;
import org.thenicesys.data.api.Pager;
import org.thenicesys.mybatis.impl.PropertyWrapper;

import java.util.Map;

public interface TestCyService extends BaseService<TestCy> {
    PropertyWrapper<TestCy> makeSearchWrapper(Map<String, Object> condition);
}
