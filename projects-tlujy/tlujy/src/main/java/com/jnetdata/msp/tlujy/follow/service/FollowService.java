package com.jnetdata.msp.tlujy.follow.service;

import com.jnetdata.msp.tlujy.follow.model.Follow;
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
 * @since 2020-12-18
 */
public interface FollowService extends BaseService<Follow> {
        PropertyWrapper<Follow> makeSearchWrapper(Map<String, Object> condition);
        Follow getEntityAndJoinsById(Long id);
        void getListJoin(List<Follow> list, Follow vo);
}
