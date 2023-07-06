package com.jnetdata.msp.cs1.middle.service;

import com.jnetdata.msp.cs1.middle.model.Middle;
import com.jnetdata.msp.core.service.BaseService;
import org.thenicesys.mybatis.impl.PropertyWrapper;

import java.util.Map;
import java.util.List;
/**
 * <p>
 * 中间表 服务类
 * </p>
 *
 * @author zyj
 * @since 2023-05-31
 */
public interface MiddleService extends BaseService<Middle> {
        PropertyWrapper<Middle> makeSearchWrapper(Map<String, Object> condition);
        Middle getEntityAndJoinsById(Long id);
        void getListJoin(List<Middle> list, Middle vo);
}
