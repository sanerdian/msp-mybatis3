package com.jnetdata.msp.zsk.unit.service;

import com.jnetdata.msp.zsk.unit.model.Unit;
import com.jnetdata.msp.core.service.BaseService;
import org.thenicesys.mybatis.impl.PropertyWrapper;

import java.util.Map;
import java.util.List;
/**
 * <p>
 * 主题词-机构名录库 服务类
 * </p>
 *
 * @author zyj
 * @since 2021-09-13
 */
public interface UnitService extends BaseService<Unit> {
        PropertyWrapper<Unit> makeSearchWrapper(Map<String, Object> condition);
        Unit getEntityAndJoinsById(Long id);
        void getListJoin(List<Unit> list, Unit vo);
}
