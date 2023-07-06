package com.jnetdata.msp.tlujy.investigate_user.service;

import com.jnetdata.msp.tlujy.investigate_user.model.InvestigateUser;
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
public interface InvestigateUserService extends BaseService<InvestigateUser> {
        PropertyWrapper<InvestigateUser> makeSearchWrapper(Map<String, Object> condition);
        InvestigateUser getEntityAndJoinsById(Long id);
        void getListJoin(List<InvestigateUser> list, InvestigateUser vo);
}
