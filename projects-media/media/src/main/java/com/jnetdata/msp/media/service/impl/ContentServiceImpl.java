package com.jnetdata.msp.media.service.impl;

import com.jnetdata.msp.core.service.impl.BaseServiceImpl;
import com.jnetdata.msp.media.mapper.ContentMapper;
import com.jnetdata.msp.media.mapper.JobApiMapper;
import com.jnetdata.msp.media.model.JobApi;
import com.jnetdata.msp.media.service.ContentService;
import com.jnetdata.msp.media.vo.OptionsVo;
import com.jnetdata.msp.tlujy.xinwen020.mapper.Xinwen020Mapper;
import com.jnetdata.msp.tlujy.xinwen020.model.Xinwen020;
import com.jnetdata.msp.tlujy.xinwen_record.mapper.XinwenRecordMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thenicesys.mybatis.impl.PropertyWrapper;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ContentServiceImpl extends BaseServiceImpl<JobApiMapper, JobApi> implements ContentService {

    @Autowired
    private ContentMapper contentMapper;
    @Autowired
    private Xinwen020Mapper xinwen020Mapper;
    @Autowired
    private XinwenRecordMapper xinwenRecordMapper;

    @Override
    public void updateXiwentopping(Xinwen020 xinwen020) {

        Xinwen020 xinwen = xinwen020Mapper.selectById(xinwen020.getId());

        //如果修改前和修改后都是，不轮播，直接返回
        if((xinwen.getToppingflag()==null||xinwen.getToppingflag()!=1L)&&(xinwen020.getToppingflag()==null||xinwen020.getToppingflag()!=1L)){
            return ;
        }

        //获取当前栏目下，需要轮播的新闻的个数
        int count =  xinwen020Mapper.selectCount(new PropertyWrapper<>(Xinwen020.class)
                .eq("columnid",xinwen020.getColumnid())
                .eq("toppingflag",1L)
                .getWrapper());

        //要设置为不轮播
        if(xinwen020.getToppingflag()!=1L){

            contentMapper.updateXiwenrotationMin((int)xinwen.getToppingorder().longValue(),count);

//            xinwen.setToppingflag(null);
//            xinwen.setToppingorder(null);
//            xinwen.setModifyTime(new Date());
//
//            xinwen020Mapper.updateById(xinwen);
            contentMapper.removeTopping(xinwen020.getId());
            return ;
        }


        //如果新旧值相等则不变
        if(xinwen.getToppingorder()!=null&&xinwen020.getToppingorder().longValue()==xinwen.getToppingorder().longValue()){
            return ;
        }

        xinwen020.setColumnid(null);

        //新加入轮播
        if((xinwen.getToppingflag()==null||xinwen.getToppingflag()!=1L)&&xinwen020.getToppingorder()!=null){
            contentMapper.updateXiwentoppingMax((int)xinwen020.getToppingorder().longValue(),count);
        }else if(xinwen020.getToppingorder()>=xinwen.getToppingorder()){//新的大于旧的，就是往后移动，中间，减一
            contentMapper.updateXiwentoppingMin((int)xinwen.getToppingorder().longValue(),(int)xinwen020.getToppingorder().longValue());
        }else if(xinwen.getToppingorder()>=xinwen020.getToppingorder()){//旧的大于新的，是往前移动，中间加一
            contentMapper.updateXiwentoppingMax((int)xinwen020.getToppingorder().longValue(),(int)xinwen.getToppingorder().longValue());
        }

        xinwen020Mapper.updateById(xinwen020);

    }



    @Override
    public List<OptionsVo> listXiwentopping(Long lmid) {
        //获取当前栏目下，需要置顶的新闻
        List<Xinwen020> list =  xinwen020Mapper.selectList(new PropertyWrapper<>(Xinwen020.class)
                .eq("columnid",lmid)
                .eq("toppingflag",1L)
                .getWrapper().orderBy(true,true,"toppingorder"));


        List<OptionsVo> volist = new ArrayList<>();

        OptionsVo v= new OptionsVo();
        v.setId(1L);
        v.setName("最上面");
        volist.add(v);

        for (Xinwen020 x : list) {

            OptionsVo vo = new OptionsVo();
            vo.setId(x.getToppingorder()+1);
            vo.setName(x.getTitle()+"下一位");

            volist.add(vo);
        }

        return volist;
    }

    @Override
    public void updateXiwenrotation(Xinwen020 xinwen020) {

        Xinwen020 xinwen = xinwen020Mapper.selectById(xinwen020.getId());

        //如果修改前和修改后都是，不轮播，直接返回
        if((xinwen.getIsrotation()==null||xinwen.getIsrotation()!=1L)&&(xinwen020.getIsrotation()==null||xinwen020.getIsrotation()!=1L)){
            return ;
        }

        //获取当前栏目下，需要轮播的新闻的个数
        int count =  xinwen020Mapper.selectCount(new PropertyWrapper<>(Xinwen020.class)
                .where("(columnid='"+xinwen020.getColumnid()+"' or (select count(1) from channelinfo c where c.channelid=columnid and c.siteid ='"+xinwen020.getColumnid()+"')>0 or " +
                        " (select count(1) from channelinfo c where c.channelid=columnid and c.parentid='"+xinwen020.getColumnid()+"')>0)")
                .eq("isrotation",1L)
                .eq("isrotation",1L)
                .getWrapper());

        //要设置为不轮播
        if(xinwen020.getIsrotation()!=1L){

            contentMapper.updateXiwenrotationMin((int)xinwen.getRotationorder().longValue(),count);

            xinwen.setIsrotation(null);
            xinwen.setRotationorder(null);
            xinwen.setModifyTime(new Date());

            xinwen020Mapper.updateById(xinwen);

            return ;
        }


        //如果新旧值相等则不变
        if(xinwen.getRotationorder()!=null&&xinwen020.getRotationorder().longValue()==xinwen.getRotationorder().longValue()){
            return ;
        }

        xinwen020.setColumnid(null);

        //新加入轮播
        if((xinwen.getIsrotation()==null||xinwen.getIsrotation()!=1L)&&xinwen020.getRotationorder()!=null){
            contentMapper.updateXiwenrotationMax((int)xinwen020.getRotationorder().longValue(),count);
        }else if(xinwen020.getRotationorder()>xinwen.getRotationorder()){//新的大于旧的，就是往后移动，中间，减一
            contentMapper.updateXiwenrotationMin((int)xinwen.getRotationorder().longValue(),(int)xinwen020.getRotationorder().longValue());
        }else if(xinwen.getRotationorder()>xinwen020.getRotationorder()){//旧的大于新的，是往前移动，中间加一
            contentMapper.updateXiwenrotationMax((int)xinwen020.getRotationorder().longValue(),(int)xinwen.getRotationorder().longValue());
        }

        xinwen020Mapper.updateById(xinwen020);

    }

    @Override
    public List<OptionsVo> listXiwenrotation(Long lmid) {
        //获取当前栏目下，需要轮播的新闻
        List<Xinwen020> list =  xinwen020Mapper.selectList(new PropertyWrapper<>(Xinwen020.class)
                .where("(columnid='"+lmid+"' or (select count(1) from channelinfo c where c.channelid=columnid and c.siteid ='"+lmid+"')>0 or " +
                        " (select count(1) from channelinfo c where c.channelid=columnid and c.parentid='"+lmid+"')>0)")
                .eq("isrotation",1L)
                .getWrapper().orderBy(true,true,"rotationorder"));


        List<OptionsVo> volist = new ArrayList<>();

        OptionsVo v= new OptionsVo();
        v.setId(1L);
        v.setName("最上面");
        volist.add(v);

        for (int i = 0; i < list.size(); i++) {

            Xinwen020 x = list.get(i);

            OptionsVo vo = new OptionsVo();
            vo.setId((long)i+2);
            vo.setName(x.getTitle()+"下一位");

            volist.add(vo);
        }

        return volist;
    }



}
