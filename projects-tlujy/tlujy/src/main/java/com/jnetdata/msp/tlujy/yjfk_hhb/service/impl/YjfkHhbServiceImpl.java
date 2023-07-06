package com.jnetdata.msp.tlujy.yjfk_hhb.service.impl;

import com.jnetdata.msp.tlujy.yjfk_hhb.model.YjfkHhb;
import com.jnetdata.msp.tlujy.yjfk_hhb.mapper.YjfkHhbMapper;
import com.jnetdata.msp.tlujy.yjfk_hhb.service.YjfkHhbService;
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
 * @since 2020-09-10
 */
@Service
public class YjfkHhbServiceImpl extends BaseServiceImpl<YjfkHhbMapper, YjfkHhb> implements YjfkHhbService {


    private String andOr = "and";
    @Override
    public PropertyWrapper<YjfkHhb> makeSearchWrapper(Map<String, Object> condition) {
        if(condition.get("andOr")!=null) {
            this.andOr = condition.get("andOr").toString();
        }
        PropertyWrapper<YjfkHhb> pw = createWrapperUtil(condition)
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
                                                                                                                                                    .eq("yjfkid")
                                                            .eq("dfnr")
                                                            .eq("dfdx")
                                                            .eq("state")
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
    public YjfkHhb getEntityAndJoinsById(Long id){
        YjfkHhb entity = getById(id);
        getJoin(entity, null);
        return entity;
    }

    public void getJoin(YjfkHhb entity, YjfkHhb vo) {
        if(vo == null) {
            vo = new YjfkHhb();
        }
    }

    @Override
    public void getListJoin(List<YjfkHhb> list, YjfkHhb vo) {
        for (YjfkHhb entity : list) {
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
