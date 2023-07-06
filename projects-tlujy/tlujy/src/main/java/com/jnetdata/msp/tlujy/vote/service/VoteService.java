package com.jnetdata.msp.tlujy.vote.service;

import com.jnetdata.msp.tlujy.vote.model.Vote;
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
 * @since 2020-11-12
 */
public interface VoteService extends BaseService<Vote> {
        PropertyWrapper<Vote> makeSearchWrapper(Map<String, Object> condition);
        Vote getEntityAndJoinsById(Long id);
        void getListJoin(List<Vote> list, Vote vo);
}
