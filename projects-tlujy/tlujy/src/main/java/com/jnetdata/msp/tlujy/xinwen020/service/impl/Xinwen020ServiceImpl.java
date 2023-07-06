package com.jnetdata.msp.tlujy.xinwen020.service.impl;

import com.jnetdata.msp.tlujy.xinwen020.model.Xinwen020;
import com.jnetdata.msp.tlujy.xinwen020.mapper.Xinwen020Mapper;
import com.jnetdata.msp.tlujy.xinwen020.service.Xinwen020Service;
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
 * @since 2020-12-11
 */
@Service
public class Xinwen020ServiceImpl extends BaseServiceImpl<Xinwen020Mapper, Xinwen020> implements Xinwen020Service {


    private String andOr = "and";
    @Override
    public PropertyWrapper<Xinwen020> makeSearchWrapper(Map<String, Object> condition) {
        if(condition.get("andOr")!=null) {
            this.andOr = condition.get("andOr").toString();
        }
        PropertyWrapper<Xinwen020> pw = createWrapperUtil(condition)
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
                                                            .like("title")
                                                            .eq("puser")
                                                            .eq("ptime")
                                                            .eq("zhengwen")
                                                            .eq("summary")
                                                            .eq("hometitle")
                                                            .eq("subtitle")
                                                            .eq("docsource")
                                                            .eq("docauthor")
                                                            .eq("isprohibitpublish")
                                                            .eq("copyflag")
                                                            .eq("isrotation")
                                                            .eq("state")
                                                            .eq("dianpingshu")
                                                            .eq("tuijianzhe")
                                                            .eq("logotu")
                                                            .eq("keywords")
                                                            .eq("scstate")
                                                            .eq("pushtorange")
                                                            .eq("commentallowed")
                                                            .eq("commentchecked")
                                                            .eq("columnname")
                                                            .eq("pushtogroup")
                                                            .eq("pushtouser")
                                                            .eq("pushtocondition")
                                                            .eq("frozentype")
                                                            .gt("writingtime")
                                                            .eq("sourceurl")
                                                            .eq("cdntype")
                                                            .eq("pushsql")
                                                            .eq("toppingflag")
                                                            .eq("toppingorder")
                                                            .eq("rotationorder")
                                                            .eq("xm")
                                                            .eq("sex")
                                                            .eq("age")
                                                            .eq("xwhcdhz")
                                                            .eq("sxzy")
                                                            .eq("zzmm")
                                                            .eq("zwmc")
                                                            .eq("xjszwmc")
                                                            .eq("jsdj")
                                                            .eq("szbz")
                                                            .eq("bzzlx")
                                                            .eq("gz")
                                                            .eq("dwbsm")
                                                            .eq("bmbm")
                                                            .eq("status")
                                .eq("createBy")
        .getWrapper();
        andOr(condition,pw,"operuser");
        andOr(condition,pw,"doctitle");
        andOr(condition,pw,"docpuburl");
        andOr(condition,pw,"linkurl");
        andOr(condition,pw,"modifyUser");
        andOr(condition,pw,"flowId");
        andOr(condition,pw,"flowUser");
pw.lt(condition.get("writingtimeBT2")!=null && condition.get("writingtimeBT2") != "","writingtime",condition.get("writingtimeBT2"));
        return pw;
    }

    @Override
    public Xinwen020 getEntityAndJoinsById(Long id){
        Xinwen020 entity = getById(id);
        getJoin(entity, null);
        return entity;
    }

    public void getJoin(Xinwen020 entity, Xinwen020 vo) {
        if(vo == null) {
            vo = new Xinwen020();
        }
    }

    @Override
    public void getListJoin(List<Xinwen020> list, Xinwen020 vo) {
        for (Xinwen020 entity : list) {
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
