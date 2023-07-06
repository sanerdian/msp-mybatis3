package com.jnetdata.msp.member.groupbusiness.service;

import com.jnetdata.msp.member.groupbusiness.model.GroupBusiness;
import com.jnetdata.msp.core.service.BaseService;
import org.thenicesys.mybatis.impl.PropertyWrapper;

import java.util.Map;
/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zyj
 * @since 2020-07-02
 */
public interface GroupBusinessService extends BaseService<GroupBusiness> {
        PropertyWrapper<GroupBusiness> makeSearchWrapper(Map<String, Object> condition);
}
