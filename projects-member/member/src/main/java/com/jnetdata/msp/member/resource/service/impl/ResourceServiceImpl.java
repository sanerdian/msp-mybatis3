package com.jnetdata.msp.member.resource.service.impl;

import com.jnetdata.msp.core.service.impl.BaseServiceImpl;
import com.jnetdata.msp.member.resource.mapper.ResourceMapper;
import com.jnetdata.msp.member.resource.model.Resource;
import com.jnetdata.msp.member.resource.service.ResourceService;
import org.springframework.stereotype.Service;
import org.thenicesys.data.api.ConditionContainer;
import org.thenicesys.mybatis.impl.PropertyWrapper;

@Service
public class ResourceServiceImpl extends BaseServiceImpl<ResourceMapper, Resource> implements ResourceService {

    @Override
    protected PropertyWrapper<Resource> makeSearchWrapper(ConditionContainer condition) {
        return createWrapperUtil(condition)
                .eq("id")
                .like("name")
                .like("naturalId")
                .getWrapper();
    }


}
