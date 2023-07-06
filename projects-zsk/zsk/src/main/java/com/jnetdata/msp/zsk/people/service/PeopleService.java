package com.jnetdata.msp.zsk.people.service;

import com.jnetdata.msp.zsk.people.model.People;
import com.jnetdata.msp.core.service.BaseService;
import org.thenicesys.mybatis.impl.PropertyWrapper;

import java.util.Map;
import java.util.List;
/**
 * <p>
 * 主题词-人名库 服务类
 * </p>
 *
 * @author zyj
 * @since 2021-10-09
 */
public interface PeopleService extends BaseService<People> {
        PropertyWrapper<People> makeSearchWrapper(Map<String, Object> condition);
        People getEntityAndJoinsById(Long id);
        void getListJoin(List<People> list, People vo);
}
