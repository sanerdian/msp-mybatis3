package com.jnetdata.msp.metadata.pic.service;

import com.jnetdata.msp.core.service.impl.BaseServiceImpl;
import com.jnetdata.msp.metadata.pic.mapper.MetadataPicMapper;
import com.jnetdata.msp.metadata.pic.model.MetadataPic;
import org.springframework.stereotype.Service;
import org.thenicesys.mybatis.impl.PropertyWrapper;

import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author zyj
 * @since 2019-10-23
 */
@Service
public class MetadataPicServiceImpl extends BaseServiceImpl<MetadataPicMapper, MetadataPic> implements MetadataPicService {
    @Override
    protected PropertyWrapper<MetadataPic> makeSearchWrapper(Map<String, Object> condition) {
        return createWrapperUtil(condition)
                .eq("id")
                .getWrapper();
    }
}


