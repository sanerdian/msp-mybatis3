package com.jnetdata.msp.member.groupbusiness.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jnetdata.msp.member.business.model.Business;
import com.jnetdata.msp.member.groupbusiness.model.GroupBusiness;
import com.jnetdata.msp.member.groupbusiness.mapper.GroupBusinessMapper;
import com.jnetdata.msp.member.groupbusiness.service.GroupBusinessService;
import com.jnetdata.msp.core.service.impl.BaseServiceImpl;
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
public class GroupBusinessServiceImpl extends BaseServiceImpl<GroupBusinessMapper, GroupBusiness> implements GroupBusinessService {
    private String andOr = "and";
    @Override
    public PropertyWrapper<GroupBusiness> makeSearchWrapper(Map<String, Object> condition) {
        if(condition.get("andOr")!=null) this.andOr = condition.get("andOr").toString();
        PropertyWrapper<GroupBusiness> pw = createWrapperUtil(condition)
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
        return pw;
    }

    public void andOr(Map<String,Object>condition,PropertyWrapper pw,String proName){
        if(condition.get(proName)!=null&&StringUtils.isNotEmpty(condition.get(proName).toString())){
            Consumer<QueryWrapper<GroupBusiness>> queryWrapperConsumer = w -> {
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
