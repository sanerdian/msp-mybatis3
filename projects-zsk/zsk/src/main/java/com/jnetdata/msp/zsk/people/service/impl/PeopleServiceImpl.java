package com.jnetdata.msp.zsk.people.service.impl;

import com.jnetdata.msp.zsk.people.model.People;
import com.jnetdata.msp.zsk.people.mapper.PeopleMapper;
import com.jnetdata.msp.zsk.people.service.PeopleService;
import com.jnetdata.msp.core.service.impl.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thenicesys.data.api.Pager;
import org.thenicesys.mybatis.impl.PropertyWrapper;
import com.baomidou.mybatisplus.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.function.Consumer;


/**
 * <p>
 * 主题词-人名库 服务实现类
 * </p>
 *
 * @author zyj
 * @since 2021-10-09
 */
@Service
public class PeopleServiceImpl extends BaseServiceImpl<PeopleMapper, People> implements PeopleService {


    private String andOr = "and";
    @Override
    public PropertyWrapper<People> makeSearchWrapper(Map<String, Object> condition) {
        if(condition.get("andOr")!=null) this.andOr = condition.get("andOr").toString();
        PropertyWrapper<People> pw = createWrapperUtil(condition)
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
                                                            .eq("bz")
                                                            .eq("pCategory")
                                                            .eq("sourceTerms")
                                                            .eq("filte")
                                                            .eq("searchorder")
                                                            .eq("recommend")
                                                            .eq("stopword")
                                                            .eq("cxfl")
                                                            .eq("ctfl")
                                                            .eq("tongyici")
                                                            .eq("entongyici")
                                                            .eq("suoxie")
                                                            .eq("pinyin")
                                                            .eq("tongyinci")
                                                            .eq("newtongyinci")
                                                            .eq("fenlei")
                                                                                                        .eq("currentposition")
                                                            .eq("currentemployer")
                                                            .eq("currentstart")
                                                            .eq("currentend")
                                                            .eq("historicalresume")
                                                            .eq("remarks")
                                .eq("createBy")
        .getWrapper();
        if (condition.get("columnid") != null) {
            pw.and(m -> m.eq("columnid",condition.get("columnid")).or().like("quotainfo",condition.get("columnid") + ":"));
        }
                                                                                                                                                                                                                                                                                                                                                                                        andOr(condition,pw,"operuser");
        andOr(condition,pw,"doctitle");
        andOr(condition,pw,"docpuburl");
        andOr(condition,pw,"linkurl");
        andOr(condition,pw,"flowId");
        andOr(condition,pw,"flowUser");
        andOr(condition,pw,"quotainfo");
        andOr(condition,pw,"wordname");

        if(condition.get("pubIds")!=null){
            List<Long> pubIds = (List<Long>)condition.get("pubIds");
            pw.in(!pubIds.isEmpty(),"id",pubIds);
        }

        return pw;
    }

    @Override
    public People getEntityAndJoinsById(Long id){
        People entity = getById(id);
        getJoin(entity, null);
        return entity;
    }

    public void getJoin(People entity, People vo) {
        if(vo == null) vo = new People();
    }

    @Override
    public void getListJoin(List<People> list, People vo) {
        for (People entity : list) {
            getJoin(entity, vo);
        }
    }

    public void andOr(Map<String,Object>condition,PropertyWrapper pw,String proName){
        if(condition.get(proName)!=null&&StringUtils.isNotEmpty(condition.get(proName).toString())){
            Consumer<QueryWrapper<People>> queryWrapperConsumer = w -> {
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
