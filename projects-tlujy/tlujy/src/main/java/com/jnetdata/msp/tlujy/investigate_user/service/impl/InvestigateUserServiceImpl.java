package com.jnetdata.msp.tlujy.investigate_user.service.impl;

import com.jnetdata.msp.tlujy.investigate_user.model.InvestigateUser;
import com.jnetdata.msp.tlujy.investigate_user.mapper.InvestigateUserMapper;
import com.jnetdata.msp.tlujy.investigate_user.service.InvestigateUserService;
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
 * @since 2020-10-17
 */
@Service
public class InvestigateUserServiceImpl extends BaseServiceImpl<InvestigateUserMapper, InvestigateUser> implements InvestigateUserService {


    private String andOr = "and";
    @Override
    public PropertyWrapper<InvestigateUser> makeSearchWrapper(Map<String, Object> condition) {
        if(condition.get("andOr")!=null) {
            this.andOr = condition.get("andOr").toString();
        }
        PropertyWrapper<InvestigateUser> pw = createWrapperUtil(condition)
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
                                                                                                                                                    .eq("investigateid")
                                                            .eq("userid")
                                                            .eq("username")
                                                            .eq("groupid")
                                                            .eq("groupname")
                                                            .eq("recoverstatus")
                                                            .eq("answerednum")
                                                            .eq("secondrest")
                                                            .eq("ispush")
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
    public InvestigateUser getEntityAndJoinsById(Long id){
        InvestigateUser entity = getById(id);
        getJoin(entity, null);
        return entity;
    }

    public void getJoin(InvestigateUser entity, InvestigateUser vo) {
        if(vo == null) {
            vo = new InvestigateUser();
        }
    }

    @Override
    public void getListJoin(List<InvestigateUser> list, InvestigateUser vo) {
        for (InvestigateUser entity : list) {
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
