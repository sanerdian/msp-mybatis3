package com.jnetdata.msp.tlujy.yjfk_hhb.service;

import com.jnetdata.msp.tlujy.yjfk_hhb.model.YjfkHhb;
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
 * @since 2020-09-10
 */
public interface YjfkHhbService extends BaseService<YjfkHhb> {
        PropertyWrapper<YjfkHhb> makeSearchWrapper(Map<String, Object> condition);
        YjfkHhb getEntityAndJoinsById(Long id);
        void getListJoin(List<YjfkHhb> list, YjfkHhb vo);
}
