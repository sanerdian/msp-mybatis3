package com.jnetdata.msp.generator.controller.component.service.impl;

import com.jnetdata.msp.core.service.impl.BaseServiceImpl;
import com.jnetdata.msp.generator.controller.component.mapper.ComponentMapper;
import com.jnetdata.msp.generator.controller.component.model.Component;
import com.jnetdata.msp.generator.controller.component.service.ComponentService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.thenicesys.mybatis.impl.PropertyWrapper;

import java.util.List;
import java.util.Map;

@Service
@Transactional(propagation = Propagation.REQUIRED)
public class ComponentServiceImpl extends BaseServiceImpl<ComponentMapper, Component> implements ComponentService {
    @Override
    protected PropertyWrapper<Component> makeSearchWrapper(Map<String, Object> condition) {
        return createWrapperUtil(condition).eq("id").eq("componentdpath").eq("componentcpath")
                .eq("componenturl").eq("moduleinfoid").eq("osname").getWrapper();
    }

    @Override
    public List<Component> listByModuleId(Long moduleinfoid, String osname) {
        return super.list(new PropertyWrapper<>(Component.class).eq("moduleinfoid",moduleinfoid).eq("osname",osname));
    }
}
