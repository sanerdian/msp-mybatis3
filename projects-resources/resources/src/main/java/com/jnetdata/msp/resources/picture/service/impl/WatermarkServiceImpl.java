package com.jnetdata.msp.resources.picture.service.impl;

import com.jnetdata.msp.core.service.impl.BaseServiceImpl;
import com.jnetdata.msp.resources.picture.mapper.WatermarkMapper;
import com.jnetdata.msp.resources.picture.model.Watermark;
import com.jnetdata.msp.resources.picture.service.WatermarkService;
import org.springframework.stereotype.Service;
import org.thenicesys.mybatis.impl.PropertyWrapper;

import java.util.Map;

/**
 * Created by Administrator on 2019/9/26.
 */
@Service
public class WatermarkServiceImpl extends BaseServiceImpl<WatermarkMapper, Watermark> implements WatermarkService {
    @Override
    protected PropertyWrapper<Watermark> makeSearchWrapper(Map<String, Object> condition) {
        return createWrapperUtil(condition)
                .like("name")
                .eq("wmkSign")
                .eq("isCompress")
                .eq("isCopyright")
                .getWrapper();
    }
}
