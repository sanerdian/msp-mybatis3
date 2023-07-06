package com.jnetdata.msp.tlujy.pushgroup.service;

import com.jnetdata.msp.tlujy.pushgroup.model.Pushgroup;
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
 * @since 2020-10-14
 */
public interface PushgroupService extends BaseService<Pushgroup> {
        PropertyWrapper<Pushgroup> makeSearchWrapper(Map<String, Object> condition);
        Pushgroup getEntityAndJoinsById(Long id);
        void getListJoin(List<Pushgroup> list, Pushgroup vo);
}
