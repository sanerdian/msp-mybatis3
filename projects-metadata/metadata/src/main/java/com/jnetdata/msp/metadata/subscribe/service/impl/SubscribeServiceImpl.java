package com.jnetdata.msp.metadata.subscribe.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jnetdata.msp.core.service.impl.BaseServiceImpl;
import com.jnetdata.msp.metadata.subscribe.mapper.SubscribeMapper;
import com.jnetdata.msp.metadata.subscribe.model.Subscribeinfo;
import com.jnetdata.msp.metadata.subscribe.service.SubscribeService;
import org.springframework.stereotype.Service;
import org.thenicesys.data.api.Pager;
import org.thenicesys.mybatis.impl.PropertyWrapper;
import org.thenicesys.web.vo.PageVo1;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class SubscribeServiceImpl extends BaseServiceImpl<SubscribeMapper, Subscribeinfo> implements SubscribeService {

    protected PropertyWrapper<Subscribeinfo> makeSearchWrapper(Map<String, Object> condition) {
        PropertyWrapper<Subscribeinfo> e = createWrapperUtil(condition).eq("classid").eq("userid").getWrapper();
        return e;
    }
    public Subscribeinfo selectidone(Subscribeinfo vo){
        Subscribeinfo subscribeinfo = baseMapper.selectOne(new QueryWrapper<Subscribeinfo>().eq("userid", vo.getUserid()).eq("classtreeid", vo.getClasstreeid()).eq("classid", vo.getClassid()));
        return subscribeinfo;
    }

    @Override
    public int selectall(List<Subscribeinfo> ids) {
        Subscribeinfo subscribeinfo = ids.get(1);
        Long userid = subscribeinfo.getUserid();
        Long classtreeid = subscribeinfo.getClasstreeid();
     /*   List<Subscribeinfo> listString1 = new ArrayList<>(ids);//深度克隆*/
        List<Subscribeinfo> subscribeinfos = baseMapper.selectList(new QueryWrapper<Subscribeinfo>().eq("userid", userid).eq("classtreeid", classtreeid));
     /*   //得到两个集合的并集
        ids.removeAll(subscribeinfos);
        ids.addAll(subscribeinfos);
        //深度克隆ids
        List<Subscribeinfo> listString2 = new ArrayList<>(ids);
        //然后进行
        listString2.removeAll(subscribeinfos);//得到要删除的数据*/
       /* if(listString2!=null){
            baseMapper.d
        }*/

        return 0;
    }

    @Override
    public List<Subscribeinfo> selectalluser(Long userid,Long classtreeid) {
        List<Subscribeinfo> subscribeinfos = baseMapper.selectList(new QueryWrapper<Subscribeinfo>().eq("userid", userid).eq("classtreeid", classtreeid));
        return subscribeinfos;
    }


    @Override
    public int selectclassid(Long s, Long userid,Long classtreeid) {
        QueryWrapper<Subscribeinfo> eqq = new QueryWrapper<Subscribeinfo>();
                eqq.eq("classid", s).eq("userid", userid).eq("classtreeid",classtreeid);
        Subscribeinfo subscribeinfo = baseMapper.selectOne(eqq);
        int i = baseMapper.deleteById(subscribeinfo.getId());
        return i;
    }

    @Override
    public Page<Subscribeinfo> selectPageall(Page pager, Long classid) {
        Page<Subscribeinfo> classid1 = baseMapper.selectPage(pager, new QueryWrapper<Subscribeinfo>().eq("classid", classid));
        return classid1;
    }
}
