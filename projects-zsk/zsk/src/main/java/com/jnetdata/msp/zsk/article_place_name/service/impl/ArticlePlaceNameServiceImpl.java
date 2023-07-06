package com.jnetdata.msp.zsk.article_place_name.service.impl;

import com.jnetdata.msp.zsk.article_place_name.model.ArticlePlaceName;
import com.jnetdata.msp.zsk.article_place_name.mapper.ArticlePlaceNameMapper;
import com.jnetdata.msp.zsk.article_place_name.service.ArticlePlaceNameService;
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
 * 文章地名库 服务实现类
 * </p>
 *
 * @author zyj
 * @since 2021-09-16
 */
@Service
public class ArticlePlaceNameServiceImpl extends BaseServiceImpl<ArticlePlaceNameMapper, ArticlePlaceName> implements ArticlePlaceNameService {


    private String andOr = "and";
    @Override
    public PropertyWrapper<ArticlePlaceName> makeSearchWrapper(Map<String, Object> condition) {
        if(condition.get("andOr")!=null) this.andOr = condition.get("andOr").toString();
        PropertyWrapper<ArticlePlaceName> pw = createWrapperUtil(condition)
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
                                                            .eq("placename")
                                                            .eq("warehousingtime")
                                                            .eq("articleid")
                                                            .eq("system1")
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

        if(condition.get("pubIds")!=null){
            List<Long> pubIds = (List<Long>)condition.get("pubIds");
            pw.in(!pubIds.isEmpty(),"id",pubIds);
        }

        return pw;
    }

    @Override
    public ArticlePlaceName getEntityAndJoinsById(Long id){
        ArticlePlaceName entity = getById(id);
        getJoin(entity, null);
        return entity;
    }

    public void getJoin(ArticlePlaceName entity, ArticlePlaceName vo) {
        if(vo == null) vo = new ArticlePlaceName();
    }

    @Override
    public void getListJoin(List<ArticlePlaceName> list, ArticlePlaceName vo) {
        for (ArticlePlaceName entity : list) {
            getJoin(entity, vo);
        }
    }

    public void andOr(Map<String,Object>condition,PropertyWrapper pw,String proName){
        if(condition.get(proName)!=null&&StringUtils.isNotEmpty(condition.get(proName).toString())){
            Consumer<QueryWrapper<ArticlePlaceName>> queryWrapperConsumer = w -> {
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
