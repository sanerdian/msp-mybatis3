package com.jnetdata.msp.member.limit.service.impl;

import com.jnetdata.msp.core.service.impl.BaseServiceImpl;
import com.jnetdata.msp.member.limit.mapper.LimitTypeMapper;
import com.jnetdata.msp.member.limit.model.LimitType;
import com.jnetdata.msp.member.limit.service.LimitTypeService;
import org.springframework.stereotype.Service;
import org.thenicesys.mybatis.impl.PropertyWrapper;

import java.util.List;
import java.util.Map;

/**
 * Created by WFJ on 2019/4/2.
 */
@Service
public class LimitTypeServiceImpl extends BaseServiceImpl<LimitTypeMapper, LimitType> implements LimitTypeService {
    @Override
    protected PropertyWrapper<LimitType> makeSearchWrapper(Map<String, Object> condition) {
        return createWrapperUtil(condition)
                .like("name")
                .eq("id")
                .eq("system")
                .getWrapper();
    }
}
