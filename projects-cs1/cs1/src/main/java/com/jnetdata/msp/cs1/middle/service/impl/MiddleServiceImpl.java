package com.jnetdata.msp.cs1.middle.service.impl;

import com.jnetdata.msp.cs1.middle.model.Middle;
import com.jnetdata.msp.cs1.middle.mapper.MiddleMapper;
import com.jnetdata.msp.cs1.middle.service.MiddleService;
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
 * 中间表 服务实现类
 * </p>
 *
 * @author zyj
 * @since 2023-05-31
 */
@Service
public class MiddleServiceImpl extends BaseServiceImpl<MiddleMapper, Middle> implements MiddleService {


    private String andOr = "and";
    @Override
    public PropertyWrapper<Middle> makeSearchWrapper(Map<String, Object> condition) {
        if(condition.get("andOr")!=null) this.andOr = condition.get("andOr").toString();
        PropertyWrapperUtil<Middle> pwu = createWrapperUtil(condition)
                .eq("keyWords")
                .eq("imageUrl")
                .eq("fllj")
                .eq("dataSource")
                .eq("publishTime")
                .eq("crawlerTime")
                .eq("readNum")
                .eq("commentsNum")
                .eq("abstract2")
                .eq("publishTimeOld")
                .eq("pdfurl")
                .eq("pdfname")
                .eq("yuzhong")
                .eq("tag")
                .eq("neirong")
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

        PropertyWrapper<Middle> pw = setBasePw(pwu,condition);

        if (condition.get("columnid") != null) {
            pw.and(m -> m.eq("columnid",condition.get("columnid")).or().like("quotainfo",condition.get("columnid") + ":"));
        }
        andOr(condition,pw,"title");
        andOr(condition,pw,"xinwenneirong2");
        andOr(condition,pw,"url");
        andOr(condition,pw,"neirongHtml2");
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
    public Middle getEntityAndJoinsById(Long id){
        Middle entity = getById(id);
        getJoin(entity, null);
        return entity;
    }

    public void getJoin(Middle entity, Middle vo) {
        if(vo == null) vo = new Middle();
    }

    @Override
    public void getListJoin(List<Middle> list, Middle vo) {
        for (Middle entity : list) {
            getJoin(entity, vo);
        }
    }

    public void andOr(Map<String,Object>condition,PropertyWrapper pw,String proName){
        if(condition.get(proName)!=null&&StringUtils.isNotEmpty(condition.get(proName).toString())){
            Consumer<QueryWrapper<Middle>> queryWrapperConsumer = w -> {
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
