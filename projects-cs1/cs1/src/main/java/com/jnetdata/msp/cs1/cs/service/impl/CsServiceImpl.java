package com.jnetdata.msp.cs1.cs.service.impl;

import com.jnetdata.msp.cs1.cs.model.Cs;
import com.jnetdata.msp.cs1.cs.mapper.CsMapper;
import com.jnetdata.msp.cs1.cs.service.CsService;
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
 * 测试 服务实现类
 * </p>
 *
 * @author zyj
 * @since 2023-05-31
 */
@Service
public class CsServiceImpl extends BaseServiceImpl<CsMapper, Cs> implements CsService {


    private String andOr = "and";
    @Override
    public PropertyWrapper<Cs> makeSearchWrapper(Map<String, Object> condition) {
        if(condition.get("andOr")!=null) this.andOr = condition.get("andOr").toString();
        PropertyWrapperUtil<Cs> pwu = createWrapperUtil(condition)
                .ge("csdate")
                .eq("csclass")
                .ge("csdouble")
                .eq("testtp")
                .eq("testclass")
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

        PropertyWrapper<Cs> pw = setBasePw(pwu,condition);

        if (condition.get("columnid") != null) {
            pw.and(m -> m.eq("columnid",condition.get("columnid")).or().like("quotainfo",condition.get("columnid") + ":"));
        }
        pw.le(condition.get("csdateBT2")!=null && !condition.get("csdateBT2").equals(""),"csdate",condition.get("csdateBT2"));
        pw.le(condition.get("csdoubleBT2")!=null && !condition.get("csdoubleBT2").equals(""),"csdouble",condition.get("csdoubleBT2"));
        andOr(condition,pw,"cs1");
        andOr(condition,pw,"cstext");
        andOr(condition,pw,"cseditor");
        andOr(condition,pw,"wj");
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
    public Cs getEntityAndJoinsById(Long id){
        Cs entity = getById(id);
        getJoin(entity, null);
        return entity;
    }

    public void getJoin(Cs entity, Cs vo) {
        if(vo == null) vo = new Cs();
    }

    @Override
    public void getListJoin(List<Cs> list, Cs vo) {
        for (Cs entity : list) {
            getJoin(entity, vo);
        }
    }

    public void andOr(Map<String,Object>condition,PropertyWrapper pw,String proName){
        if(condition.get(proName)!=null&&StringUtils.isNotEmpty(condition.get(proName).toString())){
            Consumer<QueryWrapper<Cs>> queryWrapperConsumer = w -> {
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
