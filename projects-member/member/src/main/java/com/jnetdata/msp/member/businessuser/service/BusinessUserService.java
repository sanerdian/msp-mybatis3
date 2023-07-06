package com.jnetdata.msp.member.businessuser.service;

import com.jnetdata.msp.member.businessuser.model.BusinessUser;
import com.jnetdata.msp.core.service.BaseService;
import com.jnetdata.msp.member.user.model.User;
import com.jnetdata.msp.vo.BaseVo;
import org.thenicesys.data.api.Pager;
import org.thenicesys.mybatis.impl.PropertyWrapper;

import java.util.List;
import java.util.Map;
/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zyj
 * @since 2020-07-02
 */
public interface BusinessUserService extends BaseService<BusinessUser> {
        PropertyWrapper<BusinessUser> makeSearchWrapper(Map<String, Object> condition);

        boolean addUserGroup(String userIds, String organId);

        Pager<User> getUser(BaseVo<BusinessUser> vo);
}
