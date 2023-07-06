package com.jnetdata.msp.tlujy.xinwen_user.service;

import com.jnetdata.msp.tlujy.xinwen_user.model.XinwenUser;
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
 * @since 2020-10-17
 */
public interface XinwenUserService extends BaseService<XinwenUser> {
        PropertyWrapper<XinwenUser> makeSearchWrapper(Map<String, Object> condition);
        XinwenUser getEntityAndJoinsById(Long id);
        void getListJoin(List<XinwenUser> list, XinwenUser vo);
}
