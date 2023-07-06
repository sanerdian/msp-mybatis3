package com.jnetdata.msp.media.service.impl;

import com.jnetdata.msp.core.service.impl.BaseServiceImpl;
import com.jnetdata.msp.media.mapper.JobApiMapper;
import com.jnetdata.msp.media.mapper.YthMapper;
import com.jnetdata.msp.media.model.JobApi;
import com.jnetdata.msp.media.service.ContentService;
import com.jnetdata.msp.media.service.YthService;
import com.jnetdata.msp.media.vo.TreeVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class YthServiceImpl extends BaseServiceImpl<JobApiMapper, JobApi> implements YthService {

    @Autowired
    private YthMapper ythMapper;

    @Override
    public List<TreeVo> deptTree() {
        return ythMapper.deptTree();
    }
}
