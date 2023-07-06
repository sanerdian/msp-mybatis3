package com.jnetdata.msp.tlujy.xinwen_comment.service;

import com.jnetdata.msp.tlujy.xinwen_comment.model.XinwenComment;
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
 * @since 2021-01-04
 */
public interface XinwenCommentService extends BaseService<XinwenComment> {
        PropertyWrapper<XinwenComment> makeSearchWrapper(Map<String, Object> condition);
        XinwenComment getEntityAndJoinsById(Long id);
        void getListJoin(List<XinwenComment> list, XinwenComment vo);
}
