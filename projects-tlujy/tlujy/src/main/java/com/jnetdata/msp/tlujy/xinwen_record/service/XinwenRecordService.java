package com.jnetdata.msp.tlujy.xinwen_record.service;

import com.jnetdata.msp.tlujy.xinwen_record.model.XinwenRecord;
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
public interface XinwenRecordService extends BaseService<XinwenRecord> {
        PropertyWrapper<XinwenRecord> makeSearchWrapper(Map<String, Object> condition);
        XinwenRecord getEntityAndJoinsById(Long id);
        void getListJoin(List<XinwenRecord> list, XinwenRecord vo);
}
