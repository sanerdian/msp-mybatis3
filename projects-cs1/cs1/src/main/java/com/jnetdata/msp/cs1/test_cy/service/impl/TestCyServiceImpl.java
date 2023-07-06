package com.jnetdata.msp.cs1.test_cy.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jnetdata.msp.core.service.impl.BaseServiceImpl;
import com.jnetdata.msp.cs1.test_cy.TestCy;
import com.jnetdata.msp.cs1.test_cy.mapper.TestCyMapper;
import com.jnetdata.msp.cs1.test_cy.service.TestCyService;
import com.jnetdata.msp.vo.BaseVo;
import org.springframework.stereotype.Service;
import org.thenicesys.data.api.Pager;
import org.thenicesys.mybatis.impl.PropertyWrapper;

import java.util.Map;

/**
 * @ClassName TestCyServiceImpl
 * @Description 陈闫测试实现类
 * @Author chenyan
 * @Date 2023/6/3 12:58
 */
@Service
public class TestCyServiceImpl extends BaseServiceImpl<TestCyMapper, TestCy> implements TestCyService {


    @Override
    public PropertyWrapper<TestCy> makeSearchWrapper(Map<String, Object> condition) {
        return createWrapperUtil(condition)
                .like("englishName")
                .like("chineseName")
                .eq("uniformNumber")
                .getWrapper();
    }
}
