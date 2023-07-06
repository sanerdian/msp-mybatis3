package com.jnetdata.msp.tlujy.jmetavotecontent.service;

import com.jnetdata.msp.tlujy.jmetavotecontent.model.JmetaVoteContent;
import com.jnetdata.msp.core.service.BaseService;
import org.thenicesys.mybatis.impl.PropertyWrapper;

import java.util.Map;
import java.util.List;
/**
 * <p>
 * VIEW 服务类
 * </p>
 *
 * @author zyj
 * @since 2020-08-29
 */
public interface JmetaVoteContentService extends BaseService<JmetaVoteContent> {
        PropertyWrapper<JmetaVoteContent> makeSearchWrapper(Map<String, Object> condition);
        JmetaVoteContent getEntityAndJoinsById(Long id);
        void getListJoin(List<JmetaVoteContent> list, JmetaVoteContent vo);
}
