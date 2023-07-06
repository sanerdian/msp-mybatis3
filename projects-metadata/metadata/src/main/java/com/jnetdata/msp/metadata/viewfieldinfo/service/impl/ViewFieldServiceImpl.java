package com.jnetdata.msp.metadata.viewfieldinfo.service.impl;

import com.jnetdata.msp.core.service.impl.BaseServiceImpl;
import com.jnetdata.msp.metadata.viewfieldinfo.mapper.ViewFieldMapper;
import com.jnetdata.msp.metadata.viewfieldinfo.model.ViewField;
import com.jnetdata.msp.metadata.viewfieldinfo.service.ViewFieldService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.thenicesys.mybatis.impl.PropertyWrapper;

import java.util.Map;

@Service
@Transactional(propagation= Propagation.REQUIRED)
public class ViewFieldServiceImpl extends BaseServiceImpl<ViewFieldMapper,ViewField> implements ViewFieldService {
    @Override
    protected PropertyWrapper<ViewField> makeSearchWrapper(Map<String, Object> condition) {
        return createWrapperUtil(condition).eq("viewid").eq("id").getWrapper();
    }

    @Override
    public Long[] getVFId(Long id) {

        return baseMapper.getVFId(id);
    }

    @Override
    public void updateViewGen(Long id) {
        baseMapper.updateViewGen(id);
    }

    @Override
    public void deleteByViewId(Long id) {
        delete(createWrapper().eq("viewid",id));
    }
}
