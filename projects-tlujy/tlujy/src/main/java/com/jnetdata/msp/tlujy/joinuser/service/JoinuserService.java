package com.jnetdata.msp.tlujy.joinuser.service;

import com.jnetdata.msp.tlujy.joinuser.model.Joinuser;
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
 * @since 2020-10-16
 */
public interface JoinuserService extends BaseService<Joinuser> {
        PropertyWrapper<Joinuser> makeSearchWrapper(Map<String, Object> condition);
        Joinuser getEntityAndJoinsById(Long id);
        void getListJoin(List<Joinuser> list, Joinuser vo);
}
