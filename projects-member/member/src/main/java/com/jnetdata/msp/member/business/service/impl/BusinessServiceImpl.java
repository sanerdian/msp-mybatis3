package com.jnetdata.msp.member.business.service.impl;

import com.baomidou.mybatisplus.core.conditions.interfaces.Func;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jnetdata.msp.member.business.model.Business;
import com.jnetdata.msp.member.business.mapper.BusinessMapper;
import com.jnetdata.msp.member.business.service.BusinessService;
import com.jnetdata.msp.core.service.impl.BaseServiceImpl;
import com.jnetdata.msp.member.group.model.Groupinfo;
import org.springframework.stereotype.Service;
import org.thenicesys.mybatis.impl.PropertyWrapper;
import com.baomidou.mybatisplus.toolkit.StringUtils;

import java.util.Map;
import java.util.function.Consumer;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author zyj
 * @since 2020-07-02
 */
@Service
public class BusinessServiceImpl extends BaseServiceImpl<BusinessMapper, Business> implements BusinessService {
    private String andOr = "and";
    @Override
    public PropertyWrapper<Business> makeSearchWrapper(Map<String, Object> condition) {
        if(condition.get("andOr")!=null) this.andOr = condition.get("andOr").toString();
        PropertyWrapper<Business> pw = createWrapperUtil(condition)
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
                                                                                                                .eq("registeredCapital")
                                                                                                                                                                                                                    .eq("staffSize")
                                .eq("groupid")
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
        andOr(condition,pw,"name");
        andOr(condition,pw,"legalRepresentative");
        andOr(condition,pw,"dateOfEstablishment");
        andOr(condition,pw,"tel");
        andOr(condition,pw,"email");
        andOr(condition,pw,"address");
        andOr(condition,pw,"officialWebsite");
        andOr(condition,pw,"formerName");
        andOr(condition,pw,"brand");
        andOr(condition,pw,"product");
        andOr(condition,pw,"enName");
        return pw;
    }

    @Override
    public void add(Business business) {

        super.insert(business);
    }

    public void andOr(Map<String,Object>condition,PropertyWrapper pw,String proName){
        if(condition.get(proName)!=null&&StringUtils.isNotEmpty(condition.get(proName).toString())){
            Consumer<QueryWrapper<Business>> queryWrapperConsumer = w -> {
                for(String str:condition.get(proName).toString().split(" " )){
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
