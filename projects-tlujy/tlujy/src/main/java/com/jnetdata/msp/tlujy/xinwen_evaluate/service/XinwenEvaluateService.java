package com.jnetdata.msp.tlujy.xinwen_evaluate.service;

import com.jnetdata.msp.tlujy.xinwen_evaluate.model.XinwenEvaluate;
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
public interface XinwenEvaluateService extends BaseService<XinwenEvaluate> {
        PropertyWrapper<XinwenEvaluate> makeSearchWrapper(Map<String, Object> condition);
        XinwenEvaluate getEntityAndJoinsById(Long id);
        void getListJoin(List<XinwenEvaluate> list, XinwenEvaluate vo);
}
