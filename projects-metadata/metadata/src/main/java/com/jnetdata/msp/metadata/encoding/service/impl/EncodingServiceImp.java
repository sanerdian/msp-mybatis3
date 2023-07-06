package com.jnetdata.msp.metadata.encoding.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jnetdata.msp.core.service.impl.BaseServiceImpl;
import com.jnetdata.msp.metadata.encoding.mapper.EncodingMapper;
import com.jnetdata.msp.metadata.encoding.model.Encodingmodel;
import com.jnetdata.msp.metadata.encoding.service.EncodingService;
import com.jnetdata.msp.metadata.subscribe.mapper.SubscribeMapper;
import com.jnetdata.msp.metadata.subscribe.model.Subscribeinfo;
import com.jnetdata.msp.metadata.subscribe.service.SubscribeService;
import org.apache.shiro.authc.AuthenticationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.thenicesys.data.api.Pager;
import org.thenicesys.mybatis.impl.PropertyWrapper;

import java.io.Serializable;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
@Transactional(propagation= Propagation.REQUIRED)
public class EncodingServiceImp extends BaseServiceImpl<EncodingMapper, Encodingmodel> implements EncodingService {


   /* @Override
    protected PropertyWrapper<Encodingmodel> makeSearchWrapper(Map<String, Object> condition) {
        return createWrapperUtil(condition)
                .eq("id")
                .eq("stutas")
                .like("bmname")
                .like("bmstyle")
                .eq("typee")
                .eq("cumulative")
                .eq("digit")
                .eq("nummber")
                .like("prefix")
                .like("cruser")
                .getWrapper();
    }*/


    protected PropertyWrapper<Encodingmodel> makeSearchWrapper(Map<String, Object> condition) {
        PropertyWrapper<Encodingmodel> e = createWrapperUtil(condition).eq("id").like("bmname").getWrapper();
        return e;
    }
    @Override
    public Boolean selectlist(Encodingmodel vo) {
        QueryWrapper<Encodingmodel> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("bmname",vo.getBmname());
        Encodingmodel encodingmodel = baseMapper.selectOne(queryWrapper);
        if(encodingmodel!=null){
            throw new AuthenticationException("该用户名已存在!");
        }
        //格式化
        //vo.setCrtime(new Date(System.currentTimeMillis()));
        vo.setCrtime(new Date());
        int insert = baseMapper.insert(vo);
        if(insert==0){
            throw new AuthenticationException("添加失败");
        }
        return true;
    }

    @Override
    public Boolean uplist(Encodingmodel vo) {
        QueryWrapper<Encodingmodel> encod = new QueryWrapper<>();
        encod.eq("id", vo.getId());
        Encodingmodel encodingmodel2 = baseMapper.selectOne(encod);
        if(!encodingmodel2.getBmname().equals(vo.getBmname())){
            QueryWrapper<Encodingmodel> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("bmname",vo.getBmname());
            Encodingmodel encodingmodel = baseMapper.selectOne(queryWrapper);
            if(encodingmodel!=null){
                throw new AuthenticationException("该用户名已存在!");
            }
        }
       /* QueryWrapper<Encodingmodel> queryWrapper2 = new QueryWrapper<>();
        queryWrapper2.eq("id",vo.getId());
        Encodingmodel encodingmodel1 = baseMapper.selectOne(queryWrapper2);
        if(encodingmodel1==null){
            throw new AuthenticationException("该用户名不存在!");
        }*/
        int i = baseMapper.updateById(vo);
        if(i==0){
            throw new AuthenticationException("修改失败");
        }
        return true;
    }

    @Override
    public Encodingmodel selectse(String vo) {
        QueryWrapper<Encodingmodel> encodingmodelQueryWrapper = new QueryWrapper<>();
        encodingmodelQueryWrapper.eq("bmname",vo);
        Encodingmodel encodingmodel = baseMapper.selectOne(encodingmodelQueryWrapper);
        return encodingmodel;
    }
}
