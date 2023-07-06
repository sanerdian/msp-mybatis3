package com.jnetdata.msp.resources.audio.service.impl;

import com.jnetdata.msp.core.service.impl.BaseServiceImpl;
import com.jnetdata.msp.resources.audio.mapper.AudioMapper;
import com.jnetdata.msp.resources.audio.model.Audio;
import com.jnetdata.msp.resources.audio.service.AudioService;
import org.springframework.stereotype.Service;
import org.thenicesys.mybatis.impl.PropertyWrapper;

import java.util.Map;

@Service
public class AudioServiceImpl extends BaseServiceImpl<AudioMapper,Audio> implements AudioService {
    @Override
    protected PropertyWrapper<Audio> makeSearchWrapper(Map<String, Object> condition) {
        return createWrapperUtil(condition)
                .like("name")
                .eq("mark")
                .getWrapper();
    }
}
