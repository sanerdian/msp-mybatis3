package com.jnetdata.msp.tlujy.vote_content.service;

import com.jnetdata.msp.tlujy.vote_content.model.VoteContent;
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
public interface VoteContentService extends BaseService<VoteContent> {
        PropertyWrapper<VoteContent> makeSearchWrapper(Map<String, Object> condition);
        VoteContent getEntityAndJoinsById(Long id);
        void getListJoin(List<VoteContent> list, VoteContent vo);
}
