package com.jnetdata.msp.cs1.test2_cy.service;

import com.jnetdata.msp.cs1.test2_cy.model.Test2Cy;
import com.jnetdata.msp.core.service.BaseService;
import org.thenicesys.mybatis.impl.PropertyWrapper;

import java.util.Map;
import java.util.List;
/**
 * <p>
 * 测试表cy 服务类
 * </p>
 *
 * @author zyj
 * @since 2023-06-05
 */
public interface Test2CyService extends BaseService<Test2Cy> {
        PropertyWrapper<Test2Cy> makeSearchWrapper(Map<String, Object> condition);
        Test2Cy getEntityAndJoinsById(Long id);
        void getListJoin(List<Test2Cy> list, Test2Cy vo);
}
