package com.jnetdata.msp.cs1.test_test1_test2.service;

import com.jnetdata.msp.cs1.test_test1_test2.model.TestTest1Test2;
import com.jnetdata.msp.core.service.BaseService;
import org.thenicesys.mybatis.impl.PropertyWrapper;

import java.util.Map;
import java.util.List;
/**
 * <p>
 * TEST_TEST1_TEST2 服务类
 * </p>
 *
 * @author zyj
 * @since 2023-03-08
 */
public interface TestTest1Test2Service extends BaseService<TestTest1Test2> {
        PropertyWrapper<TestTest1Test2> makeSearchWrapper(Map<String, Object> condition);
        TestTest1Test2 getEntityAndJoinsById(Long id);
        void getListJoin(List<TestTest1Test2> list, TestTest1Test2 vo);
}
