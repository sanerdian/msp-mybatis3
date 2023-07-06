package com.jnetdata.msp.metadata.addreference.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jnetdata.msp.core.service.impl.BaseServiceImpl;
import com.jnetdata.msp.metadata.addreference.mapper.AddReferenceMapper;
import com.jnetdata.msp.metadata.addreference.model.AddReferenceModel;
import com.jnetdata.msp.metadata.addreference.service.AddReferenceService;
import com.jnetdata.msp.metadata.pushgroup.model.PushgroupModel;
import org.springframework.stereotype.Service;
import org.thenicesys.data.api.EntityId;
import org.thenicesys.mybatis.impl.PropertyWrapper;
import org.thenicesys.web.JsonResult;

import javax.management.Query;
import java.util.Map;

@Service
public class AddReferenceServiceImpl extends BaseServiceImpl<AddReferenceMapper, AddReferenceModel> implements AddReferenceService {

    @Override
    protected PropertyWrapper<AddReferenceModel> makeSearchWrapper(Map<String, Object> condition) {
        return createWrapperUtil(condition)
                .eq("id")
                .eq("nameid")
                .eq("xwid")
                .like("name")
                .getWrapper();
    }

    @Override
    public boolean addlist(AddReferenceModel entity) {
        Long xwid = entity.getXwid();
        Long nameid = entity.getNameid();
        QueryWrapper<AddReferenceModel> addReferenceModelQueryWrapper = new QueryWrapper<>();
        addReferenceModelQueryWrapper.eq("xwid", xwid).eq("nameid", nameid);
        AddReferenceModel addReferenceModel = baseMapper.selectOne(addReferenceModelQueryWrapper);
        if(addReferenceModel!=null){
            return false;
        }
        boolean save = save(entity);
        return save ;
    }

}
