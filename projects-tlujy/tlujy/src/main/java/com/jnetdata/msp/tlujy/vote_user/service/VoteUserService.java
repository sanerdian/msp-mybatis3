package com.jnetdata.msp.tlujy.vote_user.service;

import com.jnetdata.msp.tlujy.vote_user.model.VoteUser;
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
public interface VoteUserService extends BaseService<VoteUser> {
        PropertyWrapper<VoteUser> makeSearchWrapper(Map<String, Object> condition);
        VoteUser getEntityAndJoinsById(Long id);
        void getListJoin(List<VoteUser> list, VoteUser vo);
}
