package com.jnetdata.msp.cs1.test2_cy.service.impl;

import com.jnetdata.msp.cs1.test2_cy.model.Test2Cy;
import com.jnetdata.msp.cs1.test2_cy.mapper.Test2CyMapper;
import com.jnetdata.msp.cs1.test2_cy.service.Test2CyService;
import com.jnetdata.msp.core.service.impl.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thenicesys.data.api.Pager;
import org.thenicesys.mybatis.impl.PropertyWrapper;
import com.baomidou.mybatisplus.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.thenicesys.mybatis.impl.PropertyWrapperUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.function.Consumer;


/**
 * <p>
 * 测试表cy 服务实现类
 * </p>
 *
 * @author zyj
 * @since 2023-06-05
 */
@Service
public class Test2CyServiceImpl extends BaseServiceImpl<Test2CyMapper, Test2Cy> implements Test2CyService {


    private String andOr = "and";
    @Override
    public PropertyWrapper<Test2Cy> makeSearchWrapper(Map<String, Object> condition) {
        if(condition.get("andOr")!=null) this.andOr = condition.get("andOr").toString();
        PropertyWrapperUtil<Test2Cy> pwu = createWrapperUtil(condition)
                .eq("age")
                .eq("sex")
                .eq("haoma")
                .eq("birthday")
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
                ;

        PropertyWrapper<Test2Cy> pw = setBasePw(pwu,condition);

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
    public Test2Cy getEntityAndJoinsById(Long id){
        Test2Cy entity = getById(id);
        getJoin(entity, null);
        return entity;
    }

    public void getJoin(Test2Cy entity, Test2Cy vo) {
        if(vo == null) vo = new Test2Cy();
    }

    @Override
    public void getListJoin(List<Test2Cy> list, Test2Cy vo) {
        for (Test2Cy entity : list) {
            getJoin(entity, vo);
        }
    }

    public void andOr(Map<String,Object>condition,PropertyWrapper pw,String proName){
        if(condition.get(proName)!=null&&StringUtils.isNotEmpty(condition.get(proName).toString())){
            Consumer<QueryWrapper<Test2Cy>> queryWrapperConsumer = w -> {
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
