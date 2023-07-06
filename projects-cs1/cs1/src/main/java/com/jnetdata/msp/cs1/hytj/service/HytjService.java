package com.jnetdata.msp.cs1.hytj.service;

import com.jnetdata.msp.cs1.hytj.model.Hytj;
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
 * @since 2023-05-31
 */
public interface HytjService extends BaseService<Hytj> {
        PropertyWrapper<Hytj> makeSearchWrapper(Map<String, Object> condition);
        Hytj getEntityAndJoinsById(Long id);
        void getListJoin(List<Hytj> list, Hytj vo);
}
