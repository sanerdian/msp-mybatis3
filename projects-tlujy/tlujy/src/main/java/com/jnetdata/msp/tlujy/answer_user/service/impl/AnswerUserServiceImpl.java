package com.jnetdata.msp.tlujy.answer_user.service.impl;

import com.jnetdata.msp.tlujy.answer_user.model.AnswerUser;
import com.jnetdata.msp.tlujy.answer_user.mapper.AnswerUserMapper;
import com.jnetdata.msp.tlujy.answer_user.service.AnswerUserService;
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
 * @since 2020-10-12
 */
@Service
public class AnswerUserServiceImpl extends BaseServiceImpl<AnswerUserMapper, AnswerUser> implements AnswerUserService {


    private String andOr = "and";
    @Override
    public PropertyWrapper<AnswerUser> makeSearchWrapper(Map<String, Object> condition) {
        if(condition.get("andOr")!=null) {
            this.andOr = condition.get("andOr").toString();
        }
        PropertyWrapper<AnswerUser> pw = createWrapperUtil(condition)
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
                                                            .eq("topicid")
                                                            .eq("answerchoose")
                                                            .eq("answertext")
                                                                                                                                                    .eq("recoverstatus")
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
    public AnswerUser getEntityAndJoinsById(Long id){
        AnswerUser entity = getById(id);
        getJoin(entity, null);
        return entity;
    }

    public void getJoin(AnswerUser entity, AnswerUser vo) {
        if(vo == null) {
            vo = new AnswerUser();
        }
    }

    @Override
    public void getListJoin(List<AnswerUser> list, AnswerUser vo) {
        for (AnswerUser entity : list) {
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
