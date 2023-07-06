package com.jnetdata.msp.fenfa.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jnetdata.msp.core.service.impl.BaseServiceImpl;
import com.jnetdata.msp.fenfa.mapper.FenfaMapper;
import com.jnetdata.msp.fenfa.model.Fenfa;
import com.jnetdata.msp.fenfa.service.FenfaService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.thenicesys.mybatis.impl.PropertyWrapper;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;


/**
 * <p>
 * 分发 服务实现类
 * </p>
 *
 * @author zyj
 * @since 2021-03-16
 */
@Service
public class FenfaServiceImpl extends BaseServiceImpl<FenfaMapper, Fenfa> implements FenfaService {


    private String andOr = "and";
    @Override
    public PropertyWrapper<Fenfa> makeSearchWrapper(Map<String, Object> condition) {
        if(condition.get("andOr")!=null) this.andOr = condition.get("andOr").toString();
        PropertyWrapper<Fenfa> pw = createWrapperUtil(condition)
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
                                                            .eq("columnid")
                                                            .eq("modifyBy")
                                                                                                        .eq("seqencing")
                                                            .eq("modifyTime")
                                                                                                                                                                                                                                            .eq("fromnames")
                                                            .ge("starttime")
                                                            .ge("endtime")
                                                            .eq("syncwhile")
                                                                                                        .eq("synctype")
                                .eq("createBy")
        .getWrapper();
                                                                                                                            pw.le(condition.get("starttimeBT2")!=null && !condition.get("starttimeBT2").equals(""),"starttime",condition.get("starttimeBT2"));
                    pw.le(condition.get("endtimeBT2")!=null && !condition.get("endtimeBT2").equals(""),"endtime",condition.get("endtimeBT2"));
                        andOr(condition,pw,"operuser");
        andOr(condition,pw,"doctitle");
        andOr(condition,pw,"docpuburl");
        andOr(condition,pw,"linkurl");
        andOr(condition,pw,"modifyUser");
        andOr(condition,pw,"flowId");
        andOr(condition,pw,"flowUser");
        andOr(condition,pw,"columnname");
        andOr(condition,pw,"fromids");
        andOr(condition,pw,"sqlstr");

        if(condition.get("pubIds")!=null){
            List<Long> pubIds = (List<Long>)condition.get("pubIds");
            pw.in(!pubIds.isEmpty(),"id",pubIds);
        }

        return pw;
    }

    public void getJoin(Fenfa entity, Fenfa vo) {
        if(vo == null) vo = new Fenfa();
    }

    public void andOr(Map<String,Object>condition,PropertyWrapper pw,String proName){
        if(condition.get(proName)!=null&& StringUtils.isNotEmpty(condition.get(proName).toString())){
            Consumer<QueryWrapper<Fenfa>> queryWrapperConsumer = w -> {
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
