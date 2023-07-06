package com.jnetdata.msp.zsk.article_number1.service.impl;

import com.jnetdata.msp.zsk.article_number1.model.ArticleNumber1;
import com.jnetdata.msp.zsk.article_number1.mapper.ArticleNumber1Mapper;
import com.jnetdata.msp.zsk.article_number1.service.ArticleNumber1Service;
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
 * 文章文号库 服务实现类
 * </p>
 *
 * @author zyj
 * @since 2021-09-16
 */
@Service
public class ArticleNumber1ServiceImpl extends BaseServiceImpl<ArticleNumber1Mapper, ArticleNumber1> implements ArticleNumber1Service {


    private String andOr = "and";
    @Override
    public PropertyWrapper<ArticleNumber1> makeSearchWrapper(Map<String, Object> condition) {
        if(condition.get("andOr")!=null) this.andOr = condition.get("andOr").toString();
        PropertyWrapper<ArticleNumber1> pw = createWrapperUtil(condition)
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
                                                            .eq("documentnumber")
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
    public ArticleNumber1 getEntityAndJoinsById(Long id){
        ArticleNumber1 entity = getById(id);
        getJoin(entity, null);
        return entity;
    }

    public void getJoin(ArticleNumber1 entity, ArticleNumber1 vo) {
        if(vo == null) vo = new ArticleNumber1();
    }

    @Override
    public void getListJoin(List<ArticleNumber1> list, ArticleNumber1 vo) {
        for (ArticleNumber1 entity : list) {
            getJoin(entity, vo);
        }
    }

    public void andOr(Map<String,Object>condition,PropertyWrapper pw,String proName){
        if(condition.get(proName)!=null&&StringUtils.isNotEmpty(condition.get(proName).toString())){
            Consumer<QueryWrapper<ArticleNumber1>> queryWrapperConsumer = w -> {
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
