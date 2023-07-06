package com.jnetdata.msp.metadata.dict.service;

import com.jnetdata.msp.metadata.dict.mapper.DictMapper;
import com.jnetdata.msp.metadata.dict.model.Dict;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.thenicesys.data.api.util.ConditionMap;
import com.jnetdata.msp.core.service.impl.BaseServiceImpl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional(propagation= Propagation.REQUIRED)
public class DictServiceImpl  extends BaseServiceImpl<DictMapper, Dict> implements DictService {

    @Value("${spring.datasource.jdbcDialect}")
    private String jdbcDialect;

    @Override
    public List<Dict> list(String type) {
        if(type.equals("db_type")) type += "_" +jdbcDialect;
        return list(createWrapper().eq("type",type));
    }

    @Override
    public Map<Integer, String> map(String type) {
        List<Dict> list = list(createWrapper().eq("type",type));
        Map<Integer,String> map = new HashMap<>();
        for(Dict obj : list){
            map.put(obj.getNo(),obj.getCode());
        }
        return map;
    }

    @Override
    public Dict get(String type, int no) {
        return get(createWrapper().eq("type",type).eq("no",no));
    }
}


