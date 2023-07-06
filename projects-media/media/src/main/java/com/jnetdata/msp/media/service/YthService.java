package com.jnetdata.msp.media.service;

import com.jnetdata.msp.core.service.BaseService;
import com.jnetdata.msp.media.model.JobApi;
import com.jnetdata.msp.media.vo.TreeVo;

import java.util.List;

public interface YthService extends BaseService<JobApi> {

    List<TreeVo> deptTree();

}
