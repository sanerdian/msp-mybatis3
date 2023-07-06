package com.jnetdata.msp.config.systemmsg.service;

import com.jnetdata.msp.config.systemmsg.model.DatabaseMsg;
import com.jnetdata.msp.core.service.BaseService;

/**
 * Created by TF on 2019/3/27.
 */
public interface DatabaseMsgService extends BaseService<DatabaseMsg> {




    public DatabaseMsg getMsg() throws Exception;
}
