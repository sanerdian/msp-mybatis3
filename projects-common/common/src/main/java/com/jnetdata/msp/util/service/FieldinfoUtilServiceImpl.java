package com.jnetdata.msp.util.service;

import com.jnetdata.msp.core.service.impl.BaseServiceImpl;
import com.jnetdata.msp.util.mapper.FieldinfoUtilMapper;
import com.jnetdata.msp.util.model.Fieldinfo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.thenicesys.mybatis.impl.PropertyWrapper;

import java.util.Map;

@Service
@Transactional(propagation= Propagation.REQUIRED)
public class FieldinfoUtilServiceImpl  extends BaseServiceImpl<FieldinfoUtilMapper, Fieldinfo> implements FieldinfoUtilService {
    @Override
    protected PropertyWrapper<Fieldinfo> makeSearchWrapper(Map<String, Object> condition) {
        return createWrapperUtil(condition).eq("tableid").eq("fieldname").in("fieldname").like("anothername").eq("id").getWrapper();
    }
}


