package com.jnetdata.msp.resources.video.service.impl;

import com.jnetdata.msp.core.service.impl.BaseServiceImpl;
import com.jnetdata.msp.resources.video.mapper.VideoMapper;
import com.jnetdata.msp.resources.video.model.Video;
import com.jnetdata.msp.resources.video.service.VideoService;
import org.springframework.stereotype.Service;
import org.thenicesys.mybatis.impl.PropertyWrapper;

import java.util.Map;

@Service
public class VideoServiceImpl extends BaseServiceImpl<VideoMapper,Video> implements VideoService {
    @Override
    protected PropertyWrapper<Video> makeSearchWrapper(Map<String, Object> condition) {
        return createWrapperUtil(condition)
                .like("name")
                .eq("mark")
                .getWrapper();
    }
}
