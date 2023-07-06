package com.jnetdata.msp.swagger.service.impl;


import com.jnetdata.msp.swagger.mapper.SwaggerconfigurationMapper;
import com.jnetdata.msp.swagger.model.Swaggerconfiguration;
import com.jnetdata.msp.swagger.service.SwaggerconfigurationService;
import org.springframework.stereotype.Service;
import org.thenicesys.data.api.ConditionContainer;
import org.thenicesys.mybatis.impl.BaseServiceImpl;
import org.thenicesys.mybatis.impl.PropertyWrapper;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author zyj
 * @since 2019-09-10
 */
@Service
public class SwaggerconfigurationServiceImpl extends BaseServiceImpl<SwaggerconfigurationMapper, Swaggerconfiguration> implements SwaggerconfigurationService {
    @Override
    protected PropertyWrapper<Swaggerconfiguration> makeSearchWrapper(ConditionContainer condition) {
        return createWrapperUtil(condition)
                        .eq("basepackagename")
                                .eq("groupname")
                    .getWrapper();
    }
}
