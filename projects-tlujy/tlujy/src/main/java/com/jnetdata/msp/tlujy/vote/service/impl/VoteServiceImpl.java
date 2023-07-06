package com.jnetdata.msp.tlujy.vote.service.impl;

import com.jnetdata.msp.tlujy.vote.model.Vote;
import com.jnetdata.msp.tlujy.vote.mapper.VoteMapper;
import com.jnetdata.msp.tlujy.vote.service.VoteService;
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
public class VoteServiceImpl extends BaseServiceImpl<VoteMapper, Vote> implements VoteService {


    private String andOr = "and";
    @Override
    public PropertyWrapper<Vote> makeSearchWrapper(Map<String, Object> condition) {
        if(condition.get("andOr")!=null) {
            this.andOr = condition.get("andOr").toString();
        }
        PropertyWrapper<Vote> pw = createWrapperUtil(condition)
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
                                                            .eq("title")
                                                            .eq("startdate")
                                                            .eq("enddate")
                                                            .eq("limittype")
                                                            .eq("limitcount")
                                                            .eq("remarks")
                                                            .eq("photo")
                                                            .eq("state")
                                                                                                                                                    .eq("pushtouser")
                                                            .eq("pushtocondition")
                                                            .eq("pushtorange")
                                                            .eq("pushtogroup")
                                                            .eq("tuijianzhe")
                                                            .eq("votethemeid")
                                                            .eq("pushsql")
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
    public Vote getEntityAndJoinsById(Long id){
        Vote entity = getById(id);
        getJoin(entity, null);
        return entity;
    }

    public void getJoin(Vote entity, Vote vo) {
        if(vo == null) {
            vo = new Vote();
        }
    }

    @Override
    public void getListJoin(List<Vote> list, Vote vo) {
        for (Vote entity : list) {
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
