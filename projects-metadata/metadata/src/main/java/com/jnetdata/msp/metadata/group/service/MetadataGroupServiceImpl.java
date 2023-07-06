package com.jnetdata.msp.metadata.group.service;

import com.jnetdata.msp.core.service.impl.BaseServiceImpl;
import com.jnetdata.msp.metadata.group.mapper.MetadataGroupMapper;
import com.jnetdata.msp.metadata.group.model.MetadataGroup;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.thenicesys.mybatis.impl.PropertyWrapper;

import java.util.Map;

@Service
@Transactional(propagation= Propagation.REQUIRED)
public class MetadataGroupServiceImpl  extends BaseServiceImpl<MetadataGroupMapper, MetadataGroup> implements MetadataGroupService {
    @Override
    protected PropertyWrapper<MetadataGroup> makeSearchWrapper(Map<String, Object> condition) {
        PropertyWrapper<MetadataGroup> pw = createWrapperUtil(condition).like("name").eq("id").like("crUser").ge("createTime").like("groupdesc").eq("parentid").getWrapper();
        pw.le(condition.get("createTimeto") != null,"createTime","createTimeto");
        return pw;
    }

}


