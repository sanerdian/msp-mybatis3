package com.jnetdata.msp.tlujy.yjfklsjl.service.impl;

import com.jnetdata.msp.tlujy.yjfklsjl.model.Yjfklsjl;
import com.jnetdata.msp.tlujy.yjfklsjl.mapper.YjfklsjlMapper;
import com.jnetdata.msp.tlujy.yjfklsjl.service.YjfklsjlService;
import com.jnetdata.msp.core.service.impl.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thenicesys.mybatis.impl.PropertyWrapper;
import com.baomidou.mybatisplus.toolkit.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.List;


/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author zyj
 * @since 2020-08-14
 */
@Service
public class YjfklsjlServiceImpl extends BaseServiceImpl<YjfklsjlMapper, Yjfklsjl> implements YjfklsjlService {


    private String andOr = "and";
    @Override
    public PropertyWrapper<Yjfklsjl> makeSearchWrapper(Map<String, Object> condition) {
        if(condition.get("andOr")!=null) {
            this.andOr = condition.get("andOr").toString();
        }
        PropertyWrapper<Yjfklsjl> pw = createWrapperUtil(condition)
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
                .eq("companyid")
                .eq("websiteid")
                .eq("columnid")
                .eq("seqencing")
                .eq("yjfkcon")
                .eq("time")
        .eq("createBy")
        .getWrapper();
        andOr(condition,pw,"operuser");
        andOr(condition,pw,"doctitle");
        andOr(condition,pw,"docpuburl");
        andOr(condition,pw,"linkurl");
        andOr(condition,pw,"status");
        andOr(condition,pw,"modifyUser");
        andOr(condition,pw,"flowId");
        andOr(condition,pw,"flowUser");
        return pw;
    }

    @Override
    public Yjfklsjl getEntityAndJoinsById(Long id){
        Yjfklsjl entity = getById(id);
        getJoin(entity);
        return entity;
    }

    public void getJoin(Yjfklsjl entity) {
    }

    @Override
    public void getListJoin(List<Yjfklsjl> list) {
        for (Yjfklsjl entity : list) {
            getJoin(entity);
        }
    }

    public void andOr(Map<String,Object>condition,PropertyWrapper pw,String proName){
        if(condition.get(proName)!=null&&StringUtils.isNotEmpty(condition.get(proName).toString())){
            pw.andNew().where(andOr.equals("and")?"1=1":"1=2",null);
            for(String str:condition.get(proName).toString().split(" " )){
                if(StringUtils.isNotEmpty(str)){
                    if(andOr.equals("or" )){
                        pw.or().like(proName,str);
                    }else{
                        pw.and().like(proName,str);
                    }
                }
            }
        }
    }
}
