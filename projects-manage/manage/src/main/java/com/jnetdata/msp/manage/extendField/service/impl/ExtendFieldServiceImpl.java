package com.jnetdata.msp.manage.extendField.service.impl;

import com.jnetdata.msp.core.service.impl.BaseServiceImpl;
import com.jnetdata.msp.manage.extendField.mapper.ExtendFieldMapper;
import com.jnetdata.msp.manage.extendField.model.ExtendField;
import com.jnetdata.msp.manage.extendField.service.ExtendFieldService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.thenicesys.mybatis.impl.PropertyWrapper;

import java.util.Map;

@Service
@Transactional(propagation = Propagation.REQUIRED)
public class ExtendFieldServiceImpl extends BaseServiceImpl<ExtendFieldMapper,ExtendField> implements ExtendFieldService {
    @Override
    protected PropertyWrapper<ExtendField> makeSearchWrapper(Map<String, Object> condition) {
        return createWrapperUtil(condition).like("fieldname").getWrapper();
    }
}
