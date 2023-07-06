package com.jnetdata.msp.tlujy.xinwen020.service;

import com.jnetdata.msp.tlujy.xinwen020.model.Xinwen020;
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
 * @since 2020-12-11
 */
public interface Xinwen020Service extends BaseService<Xinwen020> {
        PropertyWrapper<Xinwen020> makeSearchWrapper(Map<String, Object> condition);
        Xinwen020 getEntityAndJoinsById(Long id);
        void getListJoin(List<Xinwen020> list, Xinwen020 vo);
}
