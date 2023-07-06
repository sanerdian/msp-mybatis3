package com.jnetdata.msp.tlujy.xinwen_evaluate.service.impl;

import com.jnetdata.msp.tlujy.xinwen_evaluate.model.XinwenEvaluate;
import com.jnetdata.msp.tlujy.xinwen_evaluate.mapper.XinwenEvaluateMapper;
import com.jnetdata.msp.tlujy.xinwen_evaluate.service.XinwenEvaluateService;
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
public class XinwenEvaluateServiceImpl extends BaseServiceImpl<XinwenEvaluateMapper, XinwenEvaluate> implements XinwenEvaluateService {


    private String andOr = "and";
    @Override
    public PropertyWrapper<XinwenEvaluate> makeSearchWrapper(Map<String, Object> condition) {
        if(condition.get("andOr")!=null) {
            this.andOr = condition.get("andOr").toString();
        }
        PropertyWrapper<XinwenEvaluate> pw = createWrapperUtil(condition)
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
                                                                                                                                                    .eq("evaluateobject")
                                                            .eq("idvalue")
                                                            .eq("evaluatetype")
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
    public XinwenEvaluate getEntityAndJoinsById(Long id){
        XinwenEvaluate entity = getById(id);
        getJoin(entity, null);
        return entity;
    }

    public void getJoin(XinwenEvaluate entity, XinwenEvaluate vo) {
        if(vo == null) {
            vo = new XinwenEvaluate();
        }
    }

    @Override
    public void getListJoin(List<XinwenEvaluate> list, XinwenEvaluate vo) {
        for (XinwenEvaluate entity : list) {
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
