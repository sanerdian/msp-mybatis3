package com.jnetdata.msp.log.usermenu.service;

import com.jnetdata.msp.core.service.BaseService;
import com.jnetdata.msp.log.usermenu.model.UserMenuLog;

/**
 * Created by TF on 2019/3/13.
 */
public interface UserMenuLogService extends BaseService<UserMenuLog> {
    String selectSql(String name);
}
