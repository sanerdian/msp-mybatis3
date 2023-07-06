package com.jnetdata.msp.media.service.impl;

import com.jnetdata.msp.core.service.impl.BaseServiceImpl;
import com.jnetdata.msp.media.mapper.JobApiMapper;
import com.jnetdata.msp.media.model.JobApi;
import com.jnetdata.msp.media.service.JobApiService;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.thenicesys.mybatis.impl.PropertyWrapper;
import org.thenicesys.mybatis.impl.PropertyWrapperUtil;

import java.util.Arrays;
import java.util.Map;

@Service
public class JobApiServiceImpl extends BaseServiceImpl<JobApiMapper, JobApi> implements JobApiService {

    @Override
    protected PropertyWrapper<JobApi> makeSearchWrapper(Map<String, Object> condition) {
        PropertyWrapperUtil<JobApi> wrapper = createWrapperUtil(condition)
                .eq("type" )
                .eq("siteid" )
                .eq("columnid" )
                .eq("columnid" )
                .eq("docid" )
                .ge("jobtime")
//                .between("jobtime" , "jobtimeBT2" )
                .eq("status" );
        if(!ObjectUtils.isEmpty(condition.get("doctitle"))||!ObjectUtils.isEmpty(condition.get("result"))){
            wrapper.andNew().like("doctitle").or().like("result");
        }
        wrapper.orderBy(Arrays.asList(new String[]{"jobtime"}),false);
        return  wrapper.getWrapper();
    }
}
