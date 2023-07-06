package com.jnetdata.msp.metadata.moduleinfo.service.impl;

import com.jnetdata.msp.core.service.impl.BaseServiceImpl;
import com.jnetdata.msp.metadata.moduleinfo.mapper.ModuleViewMapper;
import com.jnetdata.msp.metadata.moduleinfo.model.ModuleView;
import com.jnetdata.msp.metadata.moduleinfo.service.ModuleViewService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.thenicesys.mybatis.impl.PropertyWrapper;

import java.util.Map;

@Service
@Transactional(propagation = Propagation.REQUIRED)
public class ModuleViewServiceImpl extends BaseServiceImpl<ModuleViewMapper, ModuleView> implements ModuleViewService {

    @Override
    protected PropertyWrapper<ModuleView> makeSearchWrapper(Map<String, Object> condition) {
        return createWrapperUtil(condition).eq("moduleinfoid").eq("modulename").getWrapper();
    }

}
