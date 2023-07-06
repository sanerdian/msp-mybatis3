package com.jnetdata.msp.tlujy.jmetavotecontent.service.impl;

import com.jnetdata.msp.tlujy.jmetavotecontent.model.JmetaVoteContent;
import com.jnetdata.msp.tlujy.jmetavotecontent.mapper.JmetaVoteContentMapper;
import com.jnetdata.msp.tlujy.jmetavotecontent.service.JmetaVoteContentService;
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
 * VIEW 服务实现类
 * </p>
 *
 * @author zyj
 * @since 2020-08-29
 */
@Service
public class JmetaVoteContentServiceImpl extends BaseServiceImpl<JmetaVoteContentMapper, JmetaVoteContent> implements JmetaVoteContentService {


    private String andOr = "and";
    @Override
    public PropertyWrapper<JmetaVoteContent> makeSearchWrapper(Map<String, Object> condition) {
        if(condition.get("andOr")!=null) {
            this.andOr = condition.get("andOr").toString();
        }
        PropertyWrapper<JmetaVoteContent> pw = createWrapperUtil(condition)
                                                                                                                                                                                                                                        .eq("createBy")
        .getWrapper();
        andOr(condition,pw,"voteid");
        andOr(condition,pw,"title");
        andOr(condition,pw,"remarks");
        andOr(condition,pw,"photo");
        andOr(condition,pw,"photos");
        return pw;
    }

    @Override
    public JmetaVoteContent getEntityAndJoinsById(Long id){
        JmetaVoteContent entity = getById(id);
        getJoin(entity, null);
        return entity;
    }

    public void getJoin(JmetaVoteContent entity, JmetaVoteContent vo) {
        if(vo == null) {
            vo = new JmetaVoteContent();
        }
    }

    @Override
    public void getListJoin(List<JmetaVoteContent> list, JmetaVoteContent vo) {
        for (JmetaVoteContent entity : list) {
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
