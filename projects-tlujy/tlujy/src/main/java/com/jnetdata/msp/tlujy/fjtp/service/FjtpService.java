package com.jnetdata.msp.tlujy.fjtp.service;

import com.jnetdata.msp.tlujy.fjtp.model.Fjtp;
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
public interface FjtpService extends BaseService<Fjtp> {
        PropertyWrapper<Fjtp> makeSearchWrapper(Map<String, Object> condition);
        Fjtp getEntityAndJoinsById(Long id);
        void getListJoin(List<Fjtp> list, Fjtp vo);
}
