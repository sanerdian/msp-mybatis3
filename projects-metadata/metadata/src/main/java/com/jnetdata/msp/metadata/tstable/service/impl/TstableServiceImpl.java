package com.jnetdata.msp.metadata.tstable.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jnetdata.msp.core.service.impl.BaseServiceImpl;
import com.jnetdata.msp.metadata.addreference.model.AddReferenceModel;
import com.jnetdata.msp.metadata.group.mapper.MetadataGroupMapper;
import com.jnetdata.msp.metadata.group.model.MetadataGroup;
import com.jnetdata.msp.metadata.group.service.MetadataGroupService;
import com.jnetdata.msp.metadata.tableinfo.service.TableinfoService;
import com.jnetdata.msp.metadata.tstable.mapper.TstableMapper;
import com.jnetdata.msp.metadata.tstable.model.TstableModel;
import com.jnetdata.msp.metadata.tstable.service.TstableService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.thenicesys.data.api.util.ConditionMap;

import javax.swing.table.TableModel;
import java.util.List;

@Service
@Transactional(propagation= Propagation.REQUIRED)
public class TstableServiceImpl extends BaseServiceImpl<TstableMapper, TstableModel> implements TstableService {
    @Override
    public boolean addlist(TstableModel entity) {
        Long xwid = entity.getXwid();
        Long nameid = entity.getPushid();
       /* QueryWrapper<TstableModel> addReferenceModelQueryWrapper = new QueryWrapper<>();
        addReferenceModelQueryWrapper.eq("xwid", xwid).eq("pushid", nameid);
        TstableModel addReferenceModel = baseMapper.selectOne(addReferenceModelQueryWrapper);
        if(addReferenceModel!=null){
            return false;
        }*/
        boolean save = save(entity);
        return save ;
    }
}
