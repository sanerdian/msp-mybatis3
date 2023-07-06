package com.jnetdata.msp.config.systemmsg.service;

import com.jnetdata.msp.config.systemmsg.model.SystemMsg;
import com.jnetdata.msp.core.service.BaseService;

import java.util.Map;

/**
 * Created by TF on 2019/3/27.
 */
public interface SystemMsgService extends BaseService<SystemMsg>{


    Map<String,Object> getSystemMsg() throws Exception;
}
