package com.jnetdata.msp.cs1.cs.service;

import com.jnetdata.msp.cs1.cs.model.Cs;
import com.jnetdata.msp.core.service.BaseService;
import org.thenicesys.mybatis.impl.PropertyWrapper;

import java.util.Map;
import java.util.List;
/**
 * <p>
 * 测试 服务类
 * </p>
 *
 * @author zyj
 * @since 2023-05-31
 */
public interface CsService extends BaseService<Cs> {
        PropertyWrapper<Cs> makeSearchWrapper(Map<String, Object> condition);
        Cs getEntityAndJoinsById(Long id);
        void getListJoin(List<Cs> list, Cs vo);
}
