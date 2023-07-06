package com.jnetdata.msp.tlujy.answer_user.service;

import com.jnetdata.msp.tlujy.answer_user.model.AnswerUser;
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
 * @since 2020-10-12
 */
public interface AnswerUserService extends BaseService<AnswerUser> {
        PropertyWrapper<AnswerUser> makeSearchWrapper(Map<String, Object> condition);
        AnswerUser getEntityAndJoinsById(Long id);
        void getListJoin(List<AnswerUser> list, AnswerUser vo);
}
