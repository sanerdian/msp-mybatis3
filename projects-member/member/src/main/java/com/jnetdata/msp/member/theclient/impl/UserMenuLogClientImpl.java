package com.jnetdata.msp.member.theclient.impl;

import com.jnetdata.msp.log.usermenu.model.UserMenuLog;
import com.jnetdata.msp.log.usermenu.service.UserMenuLogService;
import com.jnetdata.msp.member.theclient.UserMenuLogClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("UserMenuLogClientImpl")
public class UserMenuLogClientImpl implements UserMenuLogClient {

    @Autowired
    private UserMenuLogService userMenuLogService;

    @Override
    public void insert(UserMenuLog userMenuLog) {
        userMenuLogService.insert(userMenuLog);
    }

}
