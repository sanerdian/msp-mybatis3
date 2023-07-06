package com.jnetdata.msp.zdff.publishdistribution.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.toolkit.StringUtils;
import com.jnetdata.msp.core.service.impl.BaseServiceImpl;
import com.jnetdata.msp.zdff.publishdistribution.mapper.PublishdistriButionMapper;
import com.jnetdata.msp.zdff.publishdistribution.model.PublishdistriBution;
import com.jnetdata.msp.zdff.publishdistribution.service.PublishdistriButionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thenicesys.mybatis.impl.PropertyWrapper;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

@Service
public class PublishdistriButionServiceImpl extends BaseServiceImpl<PublishdistriButionMapper, PublishdistriBution> implements PublishdistriButionService {

    @Autowired
    private PublishdistriButionMapper pb;

    private String andOr = "and";
    @Override
    public PropertyWrapper<PublishdistriBution> makeSearchWrapper(Map<String, Object> condition) {
        if(condition.get("andOr")!=null) this.andOr = condition.get("andOr").toString();
        PropertyWrapper<PublishdistriBution> pw = createWrapperUtil(condition)
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
                .eq("createBy")
                .getWrapper();
        if (condition.get("columnid") != null) {
            pw.and(m -> m.eq("columnid",condition.get("columnid")).or().like("quotainfo",condition.get("columnid") + ":"));
        }
        andOr(condition,pw,"fenzumc");
        andOr(condition,pw,"operuser");
        andOr(condition,pw,"doctitle");
        andOr(condition,pw,"docpuburl");
        andOr(condition,pw,"linkurl");
        andOr(condition,pw,"flowId");
        andOr(condition,pw,"flowUser");
        andOr(condition,pw,"quotainfo");

        if(condition.get("pubIds")!=null){
            List<Long> pubIds = (List<Long>)condition.get("pubIds");
            pw.in(!pubIds.isEmpty(),"id",pubIds);
        }

        return pw;
    }

    /*站点分发实体*/
    @Override
    public PublishdistriBution getEntityAndJoinsById(Long id){
        PublishdistriBution entity = getById(id);
        getJoin(entity, null);
        return entity;
    }

    public void getJoin(PublishdistriBution entity, PublishdistriBution vo) {
        if(vo == null) vo = new PublishdistriBution();
    }

    @Override
    public void getListJoin(List<PublishdistriBution> list, PublishdistriBution vo) {
        for (PublishdistriBution entity : list) {
            getJoin(entity, vo);
        }
    }

    /*站点分发按站点统计*/
    @Override
    public List<PublishdistriBution> findPublishdistriBution(Long siteId) {
        List<PublishdistriBution> list = pb.findPublishdistriBution(siteId);
        return list;

    }

    public void andOr(Map<String,Object>condition,PropertyWrapper pw,String proName){
        if(condition.get(proName)!=null&& StringUtils.isNotEmpty(condition.get(proName).toString())){
            Consumer<QueryWrapper<PublishdistriBution>> queryWrapperConsumer = w -> {
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
