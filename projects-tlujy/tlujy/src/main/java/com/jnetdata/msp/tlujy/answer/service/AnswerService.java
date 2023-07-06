package com.jnetdata.msp.tlujy.answer.service;

import com.jnetdata.msp.tlujy.answer.model.Answer;
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
 * @since 2020-11-24
 */
public interface AnswerService extends BaseService<Answer> {
        PropertyWrapper<Answer> makeSearchWrapper(Map<String, Object> condition);
        Answer getEntityAndJoinsById(Long id);
        void getListJoin(List<Answer> list, Answer vo);
}
