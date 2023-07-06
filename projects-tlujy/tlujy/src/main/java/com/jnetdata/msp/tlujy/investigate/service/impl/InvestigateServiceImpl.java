package com.jnetdata.msp.tlujy.investigate.service.impl;

import com.jnetdata.msp.tlujy.investigate.model.Investigate;
import com.jnetdata.msp.tlujy.investigate.mapper.InvestigateMapper;
import com.jnetdata.msp.tlujy.investigate.service.InvestigateService;
import com.jnetdata.msp.core.service.impl.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thenicesys.data.api.Pager;
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
 * @since 2020-11-12
 */
@Service
public class InvestigateServiceImpl extends BaseServiceImpl<InvestigateMapper, Investigate> implements InvestigateService {


    private String andOr = "and";
    @Override
    public PropertyWrapper<Investigate> makeSearchWrapper(Map<String, Object> condition) {
        if(condition.get("andOr")!=null) {
            this.andOr = condition.get("andOr").toString();
        }
        PropertyWrapper<Investigate> pw = createWrapperUtil(condition)
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
                                                            .eq("modifyBy")
                                                                                                        .eq("seqencing")
                                                            .eq("modifyTime")
                                                                                                        .eq("infotype")
                                                            .eq("limitflag")
                                                            .eq("limittype")
                                                            .eq("limitcount")
                                                            .eq("showresulttype")
                                                            .eq("hsstartdate")
                                                            .eq("hsenddate")
                                                            .eq("remarks")
                                                            .eq("explains")
                                                                                                                                                    .eq("pushtogroup")
                                                            .eq("pushtouser")
                                                                                                        .eq("pushtorange")
                                                            .eq("tuijianzhe")
                                                            .eq("logotu")
                                                            .eq("timelimitflag")
                                                            .eq("timelimitsecond")
                                                            .eq("isallowedleavemidway")
                                                            .eq("pagetype")
                                                            .eq("pushsql")
                                .eq("createBy")
        .getWrapper();
        andOr(condition,pw,"operuser");
        andOr(condition,pw,"doctitle");
        andOr(condition,pw,"docpuburl");
        andOr(condition,pw,"linkurl");
        andOr(condition,pw,"status");
        andOr(condition,pw,"modifyUser");
        andOr(condition,pw,"title");
        andOr(condition,pw,"flowId");
        andOr(condition,pw,"flowUser");
        andOr(condition,pw,"pushtocondition");
        return pw;
    }

    @Override
    public Investigate getEntityAndJoinsById(Long id){
        Investigate entity = getById(id);
        getJoin(entity, null);
        return entity;
    }

    public void getJoin(Investigate entity, Investigate vo) {
        if(vo == null) {
            vo = new Investigate();
        }
    }

    @Override
    public void getListJoin(List<Investigate> list, Investigate vo) {
        for (Investigate entity : list) {
            getJoin(entity, vo);
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
