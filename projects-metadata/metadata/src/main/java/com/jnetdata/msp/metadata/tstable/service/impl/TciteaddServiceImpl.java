package com.jnetdata.msp.metadata.tstable.service.impl;

import com.jnetdata.msp.core.service.impl.BaseServiceImpl;
import com.jnetdata.msp.metadata.tciteadd.model.TciteaddModel;
import com.jnetdata.msp.metadata.treference.model.TreferenceModel;
import com.jnetdata.msp.metadata.tstable.mapper.TciteaddMapper;
import com.jnetdata.msp.metadata.tstable.service.TciteaddService;
import org.thenicesys.mybatis.impl.PropertyWrapper;

import java.util.Map;

public class TciteaddServiceImpl extends BaseServiceImpl<TciteaddMapper,TciteaddModel> implements TciteaddService {

    @Override
    protected PropertyWrapper<TciteaddModel> makeSearchWrapper(Map<String, Object> condition) {
        return createWrapperUtil(condition)
                .like("classDesc")
                .like("className")
                .in("className")
                .eq("className")
                .eq("parentId")
                .eq("id")
                .getWrapper();
    }

    @Override
    public boolean addlist(TciteaddModel entity) {
        return false;
    }
}
