package com.jnetdata.msp.member.theclient.impl;

import com.jnetdata.msp.log.userlogin.model.UserLoginLog;
import com.jnetdata.msp.log.userlogin.service.UserLoginLogService;
import com.jnetdata.msp.member.theclient.UserLoginLogClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("UserLoginLogClientImpl")
public class UserLoginLogClientImpl implements UserLoginLogClient {

    @Autowired
    private UserLoginLogService userLoginLogService;

    @Override
    public void insert(UserLoginLog userLoginLog) {
        userLoginLogService.insert(userLoginLog);
    }

    @Override
    public void updateById(UserLoginLog userLoginLog) {
        userLoginLogService.updateById(userLoginLog);
    }

}
