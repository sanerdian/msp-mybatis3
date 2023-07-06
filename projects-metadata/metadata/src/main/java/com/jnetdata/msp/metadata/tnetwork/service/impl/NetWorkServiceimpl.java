package com.jnetdata.msp.metadata.tnetwork.service.impl;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jnetdata.msp.core.service.impl.BaseServiceImpl;
import com.jnetdata.msp.metadata.pushgroup.model.PushgroupModel;
import com.jnetdata.msp.metadata.tnetwork.mapper.NetWorkMapper;
import com.jnetdata.msp.metadata.tnetwork.model.NetWorkModel;
import com.jnetdata.msp.metadata.tnetwork.service.NetWorkService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.thenicesys.mybatis.impl.PropertyWrapper;

import java.util.Date;
import java.util.Map;

@Service
@Transactional(propagation = Propagation.REQUIRED)
public class NetWorkServiceimpl extends BaseServiceImpl<NetWorkMapper,NetWorkModel> implements NetWorkService {

    @Override
    protected PropertyWrapper<NetWorkModel> makeSearchWrapper(Map<String, Object> condition) {
       PropertyWrapper<NetWorkModel> pw =createWrapperUtil(condition)
                .like("name")
                .like("username")
                .like("tablename")
                .like("treename")
                .like("organizationname")
                .in("className")
                .eq("xwid")
                .eq("id")
                .eq("condition")
                .eq("ageone")
                .eq("tableid")
                .eq("organizationid")
                .eq("treeid")
                .getWrapper();
       if(condition.get("name")!=null){
           pw.and(m->m.eq("namee",condition.get("name")));
       }if(condition.get("age")!=null){
            pw.and(m->m.eq("age",condition.get("age")));
        }if(condition.get("education")!=null){
            pw.and(m->m.eq("education",condition.get("education")));
        }if(condition.get("politicsstatus")!=null){
            pw.and(m->m.eq("politicsstatus",condition.get("politicsstatus")));
        }if(condition.get("sex")!=null){
            pw.and(m->m.eq("sex",condition.get("sex")));
        }
       return pw;

    }




    @Override
    public int insertba(NetWorkModel netWorkModel){
        int insert = baseMapper.insert(netWorkModel);
        return insert;
    }
}
