package com.jnetdata.msp.zsk.unit.service.impl;

import com.jnetdata.msp.zsk.unit.model.Unit;
import com.jnetdata.msp.zsk.unit.mapper.UnitMapper;
import com.jnetdata.msp.zsk.unit.service.UnitService;
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
 * 主题词-机构名录库 服务实现类
 * </p>
 *
 * @author zyj
 * @since 2021-09-13
 */
@Service
public class UnitServiceImpl extends BaseServiceImpl<UnitMapper, Unit> implements UnitService {


    private String andOr = "and";
    @Override
    public PropertyWrapper<Unit> makeSearchWrapper(Map<String, Object> condition) {
        if(condition.get("andOr")!=null) this.andOr = condition.get("andOr").toString();
        PropertyWrapper<Unit> pw = createWrapperUtil(condition)
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
                                                            .eq("tongyici")
                                                            .eq("entongyici")
                                                            .eq("suoxie")
                                                            .eq("pinyin")
                                                            .eq("tongyinci")
                                                            .eq("newtongyinci")
                                                            .eq("fenlei")
                                .eq("createBy")
        .getWrapper();
        if (condition.get("columnid") != null) {
            pw.and(m -> m.eq("columnid",condition.get("columnid")).or().like("quotainfo",condition.get("columnid") + ":"));
        }
                                                                                                                                                                                                                                                                                                                                andOr(condition,pw,"operuser");
        andOr(condition,pw,"doctitle");
        andOr(condition,pw,"wordname");
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

    @Override
    public Unit getEntityAndJoinsById(Long id){
        Unit entity = getById(id);
        getJoin(entity, null);
        return entity;
    }

    public void getJoin(Unit entity, Unit vo) {
        if(vo == null) vo = new Unit();
    }

    @Override
    public void getListJoin(List<Unit> list, Unit vo) {
        for (Unit entity : list) {
            getJoin(entity, vo);
        }
    }

    public void andOr(Map<String,Object>condition,PropertyWrapper pw,String proName){
        if(condition.get(proName)!=null&&StringUtils.isNotEmpty(condition.get(proName).toString())){
            Consumer<QueryWrapper<Unit>> queryWrapperConsumer = w -> {
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
