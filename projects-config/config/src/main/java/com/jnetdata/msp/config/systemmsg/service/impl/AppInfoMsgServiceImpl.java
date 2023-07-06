package com.jnetdata.msp.config.systemmsg.service.impl;

import com.jnetdata.msp.config.systemmsg.mapper.AppInfoMsgMapper;
import com.jnetdata.msp.config.systemmsg.model.AppInfoMsg;
import com.jnetdata.msp.core.service.impl.BaseServiceImpl;
import com.jnetdata.msp.config.systemmsg.service.AppInfoMsgService;
import org.springframework.stereotype.Service;

/**
 * Created by Administrator on 2019/9/12.
 */
@Service
public class AppInfoMsgServiceImpl extends BaseServiceImpl<AppInfoMsgMapper, AppInfoMsg> implements AppInfoMsgService{
}
