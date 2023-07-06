package com.jnetdata.msp.cs1.hytj.service.impl;

import com.jnetdata.msp.cs1.hytj.model.Hytj;
import com.jnetdata.msp.cs1.hytj.mapper.HytjMapper;
import com.jnetdata.msp.cs1.hytj.service.HytjService;
import com.jnetdata.msp.core.service.impl.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thenicesys.data.api.Pager;
import org.thenicesys.mybatis.impl.PropertyWrapper;
import com.baomidou.mybatisplus.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.thenicesys.mybatis.impl.PropertyWrapperUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.function.Consumer;


/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author zyj
 * @since 2023-05-31
 */
@Service
public class HytjServiceImpl extends BaseServiceImpl<HytjMapper, Hytj> implements HytjService {


    private String andOr = "and";
    @Override
    public PropertyWrapper<Hytj> makeSearchWrapper(Map<String, Object> condition) {
        if(condition.get("andOr")!=null) this.andOr = condition.get("andOr").toString();
        PropertyWrapperUtil<Hytj> pwu = createWrapperUtil(condition)
                .eq("hyid")
                .eq("yt")
                .eq("jb")
                .eq("id")
                .eq("docchannelid")
                .eq("docstatus")
                .eq("singletempkate")
                .eq("siteid")
                .eq("docvalid")
                .eq("docpubtime")
                .eq("opertime")
                .eq("docreltime")
                .eq("classinfoid")
                .eq("status")
                .eq("companyid")
                .eq("websiteid")
                .eq("seqencing")
                .eq("copyfromid")
                ;

        PropertyWrapper<Hytj> pw = setBasePw(pwu,condition);

        if (condition.get("columnid") != null) {
            pw.and(m -> m.eq("columnid",condition.get("columnid")).or().like("quotainfo",condition.get("columnid") + ":"));
        }
        andOr(condition,pw,"hymc");
        andOr(condition,pw,"fzry");
        andOr(condition,pw,"operuser");
        andOr(condition,pw,"doctitle");
        andOr(condition,pw,"docpuburl");
        andOr(condition,pw,"linkurl");
        andOr(condition,pw,"flowId");
        andOr(condition,pw,"flowUser");
        andOr(condition,pw,"quotainfo");
        andOr(condition,pw,"sj");
        andOr(condition,pw,"ry");

        if(condition.get("pubIds")!=null){
            List<Long> pubIds = (List<Long>)condition.get("pubIds");
            pw.in(!pubIds.isEmpty(),"id",pubIds);
        }

        return pw;
    }

    @Override
    public Hytj getEntityAndJoinsById(Long id){
        Hytj entity = getById(id);
        getJoin(entity, null);
        return entity;
    }

    public void getJoin(Hytj entity, Hytj vo) {
        if(vo == null) vo = new Hytj();
    }

    @Override
    public void getListJoin(List<Hytj> list, Hytj vo) {
        for (Hytj entity : list) {
            getJoin(entity, vo);
        }
    }

    public void andOr(Map<String,Object>condition,PropertyWrapper pw,String proName){
        if(condition.get(proName)!=null&&StringUtils.isNotEmpty(condition.get(proName).toString())){
            Consumer<QueryWrapper<Hytj>> queryWrapperConsumer = w -> {
                for(String str:condition.get(proName).toString().split(" ")){
                    if(StringUtils.isNotEmpty(str)){
                        if(andOr.equals("or" )) w.or();
                        w.like(pw.getColumn(proName),str);
                    }
                }
            };
            pw.and(queryWrapperConsumer);
        }
    }
}
