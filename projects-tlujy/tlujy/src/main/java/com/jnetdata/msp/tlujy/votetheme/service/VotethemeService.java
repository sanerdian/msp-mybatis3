package com.jnetdata.msp.tlujy.votetheme.service;

import com.jnetdata.msp.tlujy.votetheme.model.Votetheme;
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
 * @since 2020-10-18
 */
public interface VotethemeService extends BaseService<Votetheme> {
        PropertyWrapper<Votetheme> makeSearchWrapper(Map<String, Object> condition);
        Votetheme getEntityAndJoinsById(Long id);
        void getListJoin(List<Votetheme> list, Votetheme vo);
}
