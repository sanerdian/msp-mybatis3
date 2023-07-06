package com.jnetdata.msp.core.dbsource.service.impl;

import com.jnetdata.msp.core.dbsource.model.DbSource;
import com.jnetdata.msp.core.dbsource.mapper.DbSourceMapper;
import com.jnetdata.msp.core.dbsource.service.DbSourceService;
import com.jnetdata.msp.core.service.impl.BaseServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.thenicesys.mybatis.impl.PropertyWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import java.util.Map;
import java.util.List;
import java.util.function.Consumer;


/**
 * <p>
 * dbSource 服务实现类
 * </p>
 *
 * @author zyj
 * @since 2022-12-06
 */
@Service
public class DbSourceServiceImpl extends BaseServiceImpl<DbSourceMapper, DbSource> implements DbSourceService {


    private String andOr = "and";
    @Override
    public PropertyWrapper<DbSource> makeSearchWrapper(Map<String, Object> condition) {
        if(condition.get("andOr")!=null) this.andOr = condition.get("andOr").toString();
        PropertyWrapper<DbSource> pw = createWrapperUtil(condition)
                .eq("name")
                .eq("ip")
                .eq("port")
                .eq("acount")
                .eq("pw")
                .eq("type")
        .eq("createBy")
        .getWrapper();
        if (condition.get("columnid") != null) {
            pw.and(m -> m.eq("columnid",condition.get("columnid")).or().like("quotainfo",condition.get("columnid") + ":"));
        }

        if(condition.get("pubIds")!=null){
            List<Long> pubIds = (List<Long>)condition.get("pubIds");
            pw.in(!pubIds.isEmpty(),"id",pubIds);
        }

        return pw;
    }

    /**
     * 根据id获取实体和联接
     * @param id
     * @author王树彬
     * @date 2022/12/13
     * @return
     */
    @Override
    public DbSource getEntityAndJoinsById(Long id){
        DbSource entity = getById(id);
        getJoin(entity, null);
        return entity;
    }

    public void getJoin(DbSource entity, DbSource vo) {
        if(vo == null) vo = new DbSource();
    }

    @Override
    public void getListJoin(List<DbSource> list, DbSource vo) {
        for (DbSource entity : list) {
            getJoin(entity, vo);
        }
    }

    @Override
    public List<DbSource> allByType(String type) {
        return this.list(new PropertyWrapper<>(DbSource.class).eq("type",type));
    }

    public void andOr(Map<String,Object>condition,PropertyWrapper pw,String proName){
        if(condition.get(proName)!=null&& StringUtils.isNotEmpty(condition.get(proName).toString())){
            Consumer<QueryWrapper<DbSource>> queryWrapperConsumer = w -> {
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
