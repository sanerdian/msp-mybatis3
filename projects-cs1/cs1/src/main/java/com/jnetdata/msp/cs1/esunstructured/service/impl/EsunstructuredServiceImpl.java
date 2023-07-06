package com.jnetdata.msp.cs1.esunstructured.service.impl;

import com.baomidou.mybatisplus.toolkit.IdWorker;
import com.jnetdata.msp.core.util.MapUtil;
import com.jnetdata.msp.cs1.esunstructured.model.Esunstructured;
import com.jnetdata.msp.cs1.esunstructured.service.EsunstructuredService;
import com.jnetdata.msp.core.service.EsCommonService;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.index.query.AbstractQueryBuilder;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thenicesys.data.api.Pager;
import org.thenicesys.web.vo.PageVo1;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * testt 服务实现类
 * </p>
 *
 * @author zyj
 * @since 2021-03-12
 */
@Service
public class EsunstructuredServiceImpl implements EsunstructuredService{

    @Autowired
    EsCommonService esCommonService;

    @Override
    public Pager<Esunstructured> list(PageVo1 pager, Esunstructured entity) {
        BoolQueryBuilder boolQueryBuilder= QueryBuilders.boolQuery();

        setQueryBuilder(boolQueryBuilder, "filetype", entity.getFiletype(),"text");
        setQueryBuilder(boolQueryBuilder, "pageCount", entity.getPagecount(),"text");
        setQueryBuilder(boolQueryBuilder, "filelink", entity.getFilelink(),"text");
        setQueryBuilder(boolQueryBuilder, "year", entity.getYear(),"text");
        setQueryBuilder(boolQueryBuilder, "author", entity.getAuthor(),"text");
        setQueryBuilder(boolQueryBuilder, "sourceSystem", entity.getSourcesystem(),"text");
        setQueryBuilder(boolQueryBuilder, "description", entity.getDescription(),"text");
        setQueryBuilder(boolQueryBuilder, "filesize", entity.getFilesize(),"text");
        setQueryBuilder(boolQueryBuilder, "label", entity.getLabel(),"text");
        setQueryBuilder(boolQueryBuilder, "title", entity.getTitle(),"text");
        setQueryBuilder(boolQueryBuilder, "type", entity.getType(),"text");
        setQueryBuilder(boolQueryBuilder, "content", entity.getContent(),"text");
        setQueryBuilder(boolQueryBuilder, "fastdevid", entity.getFastdevid(),"long");
        setQueryBuilder(boolQueryBuilder, "department", entity.getDepartment(),"text");
        setQueryBuilder(boolQueryBuilder, "updatetime", entity.getUpdatetime(),"text");
        setQueryBuilder(boolQueryBuilder, "updateuser", entity.getUpdateuser(),"text");
        setCommonkeyQueryBuilder(boolQueryBuilder, entity.getSearchFields(), entity.getCommonKey(), entity.getSearchType());
        return esCommonService.search(1L,"esunstructured",boolQueryBuilder,pager,Esunstructured.class, entity.getSearchFields()!=null?Arrays.asList(entity.getSearchFields()):null);
    }

    public void setCommonkeyQueryBuilder(BoolQueryBuilder boolQueryBuilder, String[] searchFields,String value,Integer searchType){
        if(StringUtils.isNotEmpty(value)){
            String[] s = value.trim().split(" ");
            for (String s1 : s) {
                if(StringUtils.isEmpty(s1) || s1.equals(" ")){
                    continue;
                }
                BoolQueryBuilder bool = QueryBuilders.boolQuery();
                boolean flag = true;
                if(searchFields!=null){
                    for (String searchField : searchFields) {
                        AbstractQueryBuilder abstractQueryBuilder;
                        if(searchType == 0 ){
                            abstractQueryBuilder = QueryBuilders.matchPhraseQuery(searchField, s1);
                        }else {
                            abstractQueryBuilder = QueryBuilders.matchQuery(searchField, s1);
                        }
                        if(flag) abstractQueryBuilder.boost(3f);
                        else abstractQueryBuilder.boost(0.1f);
                        flag = false;
                        bool.should(abstractQueryBuilder);
                    }
                }else {
                    QueryBuilders.queryStringQuery(value);
                }
                boolQueryBuilder.must(bool);
            }
        }
    }

    public void setQueryBuilder(BoolQueryBuilder boolQueryBuilder, String fieldName,String value,String type){
        if(StringUtils.isEmpty(value)) return;
        if(type.equals("text")){
            boolQueryBuilder.must(QueryBuilders.matchQuery(fieldName, value));
        }else if(type.equals("keyword")){
            BoolQueryBuilder bool = QueryBuilders.boolQuery();
            String[] s = value.trim().split(" ");
            for (String s1 : s) {
                bool.should(QueryBuilders.wildcardQuery(fieldName, "*"+s1+"*"));
            }
            boolQueryBuilder.must(bool);
        }else{
            BoolQueryBuilder bool = QueryBuilders.boolQuery();
            String[] s = value.trim().split(" ");
            for (String s1 : s) {
                bool.should(QueryBuilders.termQuery(fieldName, s1));
            }
            boolQueryBuilder.must(bool);
        }

    }

    public void setNotTagsQueryBuilder(BoolQueryBuilder boolQueryBuilder, String fieldName, List<String> value){
        if(value == null || value.isEmpty()) return;
        BoolQueryBuilder bool = QueryBuilders.boolQuery();
        for (String s : value) {
            bool.should(QueryBuilders.matchPhraseQuery(fieldName,s));
        }
        boolQueryBuilder.mustNot(bool);
    }

    @Override
    public String insert(Esunstructured entity) {
        esCommonService.addData(1L,"esunstructured", entity.getEsMap());
        return entity.getId();
    }

    @Override
    public boolean delete(String id) {
        return esCommonService.delData(1L,"esunstructured" , id);
    }

    @Override
    public Esunstructured getById(String id) {
        Map<String, Object> map = esCommonService.getById(1L,"esunstructured", id);
        return map==null?null:new Esunstructured(map);
    }

    @Override
    public void updateById(String id,Esunstructured entity) {
        esCommonService.updateData(1L,"esunstructured",id,entity.getEsMap());
    }

    @Override
    public boolean deleteBatch(String[] ids) {
        for (String id : ids) {
            esCommonService.delData(1L,"esunstructured",id);
        }
        return true;
    }
}
