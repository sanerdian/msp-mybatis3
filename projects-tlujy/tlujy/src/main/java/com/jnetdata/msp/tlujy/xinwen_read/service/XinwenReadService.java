package com.jnetdata.msp.tlujy.xinwen_read.service;

import com.jnetdata.msp.tlujy.xinwen_read.model.XinwenRead;
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
public interface XinwenReadService extends BaseService<XinwenRead> {
        PropertyWrapper<XinwenRead> makeSearchWrapper(Map<String, Object> condition);
        XinwenRead getEntityAndJoinsById(Long id);
        void getListJoin(List<XinwenRead> list, XinwenRead vo);
}
