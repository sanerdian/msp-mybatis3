package com.jnetdata.msp.log.userlogin.service.impl;

import com.jnetdata.msp.core.model.util.SessionUser;
import com.jnetdata.msp.core.service.impl.BaseServiceImpl;
import com.jnetdata.msp.log.userlogin.mapper.UserLoginMapper;
import com.jnetdata.msp.log.userlogin.model.UserLoginLog;
import com.jnetdata.msp.log.userlogin.service.UserLoginLogService;
import com.jnetdata.msp.member.user.model.User;
import lombok.val;
import org.springframework.stereotype.Service;
import org.thenicesys.mybatis.impl.PropertyWrapper;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * Created by TF on 2019/3/13.
 */
@Service
public class UserLoginLogServiceImpl extends BaseServiceImpl<UserLoginMapper,UserLoginLog> implements UserLoginLogService {
    @Override
    protected PropertyWrapper<UserLoginLog> makeSearchWrapper(Map<String, Object> condition) {
        return createWrapperUtil(condition)
                .like("crUser")
                .like("address")
                .between("loginTime","endTime")
                .between("logoutTime","endTime1")
                .getWrapper();
    }

    @Override
    public void addLoginlog(HttpServletRequest request, User user) {
        UserLoginLog userLoginLog =new UserLoginLog();
        userLoginLog.setCrUser(user.getName());
        userLoginLog.setAddress(user.getLoginIp());
        userLoginLog.setLoginTime(user.getLoginTime());
        userLoginLog.setTimes(user.getTimes());
        super.insert(userLoginLog);
        request.getSession().setAttribute("loginLogId",userLoginLog.getId());
    }
}
