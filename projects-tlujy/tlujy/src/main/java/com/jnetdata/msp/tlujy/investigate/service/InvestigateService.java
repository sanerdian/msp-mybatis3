package com.jnetdata.msp.tlujy.investigate.service;

import com.jnetdata.msp.tlujy.investigate.model.Investigate;
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
 * @since 2020-11-12
 */
public interface InvestigateService extends BaseService<Investigate> {
        PropertyWrapper<Investigate> makeSearchWrapper(Map<String, Object> condition);
        Investigate getEntityAndJoinsById(Long id);
        void getListJoin(List<Investigate> list, Investigate vo);
}
