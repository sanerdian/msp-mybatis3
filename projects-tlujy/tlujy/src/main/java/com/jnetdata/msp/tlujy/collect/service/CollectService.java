package com.jnetdata.msp.tlujy.collect.service;

import com.jnetdata.msp.tlujy.collect.model.Collect;
import com.jnetdata.msp.core.service.BaseService;
import org.thenicesys.mybatis.impl.PropertyWrapper;

import java.util.Map;
import java.util.List;
/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zyj
 * @since 2020-09-21
 */
public interface CollectService extends BaseService<Collect> {
        PropertyWrapper<Collect> makeSearchWrapper(Map<String, Object> condition);
        Collect getEntityAndJoinsById(Long id);
        void getListJoin(List<Collect> list, Collect vo);
}
