package com.jnetdata.msp.metadata.tstable.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jnetdata.msp.core.service.BaseService;
import com.jnetdata.msp.core.service.impl.BaseServiceImpl;
import com.jnetdata.msp.metadata.pushgroup.model.PushgroupModel;
import com.jnetdata.msp.metadata.treference.model.TreferenceModel;
import com.jnetdata.msp.metadata.tstable.mapper.TreferenceMapper;
import com.jnetdata.msp.metadata.tstable.mapper.TstableMapper;
import com.jnetdata.msp.metadata.tstable.model.TstableModel;
import com.jnetdata.msp.metadata.tstable.service.TreferenceService;
import com.jnetdata.msp.metadata.tstable.service.TstableService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.thenicesys.mybatis.impl.PropertyWrapper;

import java.util.List;
import java.util.Map;

@Service
@Transactional(propagation= Propagation.REQUIRED)
public class TreferenceServiceImpl extends BaseServiceImpl<TreferenceMapper, TreferenceModel> implements TreferenceService {
    @Override
    public boolean addlist(TreferenceModel entity) {
        Long xwid = entity.getXwid();
        Long nameid = entity.getNameid();
        QueryWrapper<TreferenceModel> addReferenceModelQueryWrapper = new QueryWrapper<>();
        addReferenceModelQueryWrapper.eq("xwid", xwid).eq("nameid", nameid).eq("lanmid",entity.getLanmid());
        TreferenceModel addReferenceModel = baseMapper.selectOne(addReferenceModelQueryWrapper);
        if(addReferenceModel!=null){
            return false;
        }
        boolean save = save(entity);
        return save ;
    }

    @Override
    public List<TreferenceModel> selectBay(Long columnid) {
        List<TreferenceModel> nameid = baseMapper.selectList(new QueryWrapper<TreferenceModel>().eq("nameid", columnid));
        return nameid;
    }

    @Override
    protected PropertyWrapper<TreferenceModel> makeSearchWrapper(Map<String, Object> condition) {
        return createWrapperUtil(condition)
                .like("classDesc")
                .like("className")
                .in("className")
                .eq("className")
                .eq("parentId")
                .eq("id")
                .getWrapper();
    }
}
