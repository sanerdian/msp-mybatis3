package com.jnetdata.msp.tlujy.yjfk.service;

import com.jnetdata.msp.tlujy.yjfk.model.Yjfk;
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
 * @since 2020-11-05
 */
public interface YjfkService extends BaseService<Yjfk> {
        PropertyWrapper<Yjfk> makeSearchWrapper(Map<String, Object> condition);
        Yjfk getEntityAndJoinsById(Long id);
        void getListJoin(List<Yjfk> list, Yjfk vo);
}
