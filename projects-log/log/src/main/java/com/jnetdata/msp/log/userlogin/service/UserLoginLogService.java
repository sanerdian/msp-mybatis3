package com.jnetdata.msp.log.userlogin.service;

import com.jnetdata.msp.core.service.BaseService;
import com.jnetdata.msp.log.userlogin.model.UserLoginLog;
import com.jnetdata.msp.member.user.model.User;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * Created by TF on 2019/3/13.
 */
public interface UserLoginLogService extends BaseService<UserLoginLog> {

    void addLoginlog(HttpServletRequest request, User user);
}
