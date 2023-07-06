package com.jnetdata.msp.tlujy.yjfklsjl.service;

import com.jnetdata.msp.tlujy.yjfklsjl.model.Yjfklsjl;
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
 * @since 2020-08-14
 */
public interface YjfklsjlService extends BaseService<Yjfklsjl> {
        PropertyWrapper<Yjfklsjl> makeSearchWrapper(Map<String, Object> condition);
        Yjfklsjl getEntityAndJoinsById(Long id);
        void getListJoin(List<Yjfklsjl> list);
}
