package com.jnetdata.msp.util.gmapnetcache.service.impl;

import com.jnetdata.msp.util.gmapnetcache.model.Gmapnetcache;
import com.jnetdata.msp.util.gmapnetcache.mapper.GmapnetcacheMapper;
import com.jnetdata.msp.util.gmapnetcache.service.GmapnetcacheService;
import com.jnetdata.msp.core.service.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;
import org.thenicesys.mybatis.impl.PropertyWrapper;
import com.baomidou.mybatisplus.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.thenicesys.mybatis.impl.PropertyWrapperUtil;

import java.util.Map;
import java.util.List;
import java.util.function.Consumer;


/**
 * <p>
 * Gmapnetcache 服务实现类
 * </p>
 *
 * @author zyj
 * @since 2023-04-12
 */
@Service
public class GmapnetcacheServiceImpl extends BaseServiceImpl<GmapnetcacheMapper, Gmapnetcache> implements GmapnetcacheService {


    private String andOr = "and";
    @Override
    public PropertyWrapper<Gmapnetcache> makeSearchWrapper(Map<String, Object> condition) {
        if(condition.get("andOr")!=null) this.andOr = condition.get("andOr").toString();
        PropertyWrapperUtil<Gmapnetcache> pwu = createWrapperUtil(condition)
                .eq("type")
                .eq("zoom")
                .eq("x")
                .eq("y")
                .eq("tile")
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

        PropertyWrapper<Gmapnetcache> pw = setBasePw(pwu,condition);

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
    public Gmapnetcache getEntityAndJoinsById(Long id){
        Gmapnetcache entity = getById(id);
        getJoin(entity, null);
        return entity;
    }

    public void getJoin(Gmapnetcache entity, Gmapnetcache vo) {
        if(vo == null) vo = new Gmapnetcache();
    }

    @Override
    public void getListJoin(List<Gmapnetcache> list, Gmapnetcache vo) {
        for (Gmapnetcache entity : list) {
            getJoin(entity, vo);
        }
    }

    public void andOr(Map<String,Object>condition,PropertyWrapper pw,String proName){
        if(condition.get(proName)!=null&&StringUtils.isNotEmpty(condition.get(proName).toString())){
            Consumer<QueryWrapper<Gmapnetcache>> queryWrapperConsumer = w -> {
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
