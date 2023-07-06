package com.jnetdata.msp.metadata.Push.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jnetdata.msp.core.service.impl.BaseServiceImpl;
import com.jnetdata.msp.metadata.Push.mapper.PushMapper;
import com.jnetdata.msp.metadata.Push.service.PushService;
import com.jnetdata.msp.metadata.push.model.PushModel;
import com.jnetdata.msp.metadata.subscribe.model.Subscribeinfo;
import org.springframework.stereotype.Service;
import org.thenicesys.mybatis.impl.PropertyWrapper;

import java.util.List;
import java.util.Map;

@Service
public class PushServiceImpl extends BaseServiceImpl<PushMapper, PushModel>  implements PushService {
    @Override
    public PushModel selectpush(PushModel vo) {
        QueryWrapper<PushModel> pushModelQueryWrapper = new QueryWrapper<>();
        pushModelQueryWrapper.eq("pushid",vo.getId()).eq("classid",vo.getClassid());
        PushModel pushModel = baseMapper.selectOne(pushModelQueryWrapper);
        return pushModel;
    }

   /* @Override
    public Long selectlist(Long pushid, Long nameid) {
        PushModel pushModel = baseMapper.selectOne(new QueryWrapper<PushModel>().eq("", pushid).eq("", nameid));
        Long pushid1 = pushModel.getPushid();
        return pushid1;
    }*/

    protected PropertyWrapper<PushModel> makeSearchWrapper(Map<String, Object> condition) {
        PropertyWrapper<PushModel> e = createWrapperUtil(condition).eq("id").eq("pushid").getWrapper();
        return e;
    }
}
