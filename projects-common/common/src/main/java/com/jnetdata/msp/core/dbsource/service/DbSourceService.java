package com.jnetdata.msp.core.dbsource.service;

import com.jnetdata.msp.core.dbsource.model.DbSource;
import com.jnetdata.msp.core.service.BaseService;
import org.thenicesys.mybatis.impl.PropertyWrapper;

import java.util.Map;
import java.util.List;
/**
 * <p>
 * dbSource 服务类
 * </p>
 *
 * @author zyj
 * @since 2022-12-06
 */
public interface DbSourceService extends BaseService<DbSource> {
        PropertyWrapper<DbSource> makeSearchWrapper(Map<String, Object> condition);
        /**
         * 根据id获取实体和联接
         * @param id
         * @author王树彬
         * @date 2022/12/13
         * @return
         */
        DbSource getEntityAndJoinsById(Long id);
        void getListJoin(List<DbSource> list, DbSource vo);
        List<DbSource> allByType(String type);
}
