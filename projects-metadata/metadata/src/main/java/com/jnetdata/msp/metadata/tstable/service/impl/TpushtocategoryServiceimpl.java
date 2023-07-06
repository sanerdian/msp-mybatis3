package com.jnetdata.msp.metadata.tstable.service.impl;

import com.jnetdata.msp.core.service.impl.BaseServiceImpl;
import com.jnetdata.msp.metadata.tciteadd.model.TciteaddModel;
import com.jnetdata.msp.metadata.tpushtocategory.model.TpushtocategoryModel;
import com.jnetdata.msp.metadata.tstable.mapper.TpushtocategoryMapper;
import com.jnetdata.msp.metadata.tstable.service.TpushtocategoryService;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Service;
import org.thenicesys.mybatis.impl.PropertyWrapper;

import java.util.Map;

@Service
public class TpushtocategoryServiceimpl extends BaseServiceImpl<TpushtocategoryMapper, TpushtocategoryModel> implements TpushtocategoryService {
    @Override
    protected PropertyWrapper<TpushtocategoryModel> makeSearchWrapper(Map<String, Object> condition) {
        return createWrapperUtil(condition)
                .like("pushname")
                .like("className")
                .like("tablename")
                .eq("tableid")
                .eq("classid")
                .eq("className")
                .eq("pushid")
                .eq("id")
                .getWrapper();
    }

}
