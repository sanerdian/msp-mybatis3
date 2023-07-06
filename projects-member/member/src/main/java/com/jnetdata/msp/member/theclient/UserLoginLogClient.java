package com.jnetdata.msp.member.theclient;

import com.jnetdata.msp.log.userlogin.model.UserLoginLog;

import javax.servlet.http.HttpServletRequest;

public interface UserLoginLogClient {

    void insert(UserLoginLog userLoginLog);

    void updateById(UserLoginLog userLoginLog);

}
