package com.jnetdata.msp.util.gmapnetcache.service;

import com.jnetdata.msp.util.gmapnetcache.model.Gmapnetcache;
import com.jnetdata.msp.core.service.BaseService;
import org.thenicesys.mybatis.impl.PropertyWrapper;

import java.util.Map;
import java.util.List;
/**
 * <p>
 * Gmapnetcache 服务类
 * </p>
 *
 * @author zyj
 * @since 2023-04-12
 */
public interface GmapnetcacheService extends BaseService<Gmapnetcache> {
        PropertyWrapper<Gmapnetcache> makeSearchWrapper(Map<String, Object> condition);
        Gmapnetcache getEntityAndJoinsById(Long id);
        void getListJoin(List<Gmapnetcache> list, Gmapnetcache vo);
}
