package com.jnetdata.msp.message.msgConfig.service.impl;

import com.jnetdata.msp.message.msgConfig.mapper.SetMsgMapper;
import com.jnetdata.msp.message.msgConfig.model.Msg;
import com.jnetdata.msp.message.msgConfig.service.SetMsgService;
import com.jnetdata.msp.core.service.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;
import org.thenicesys.mybatis.impl.PropertyWrapper;

import java.util.Map;

/**
 * Created by Admin on 2019/3/11.
 */
@Service
public class SetMsgServiceImpl extends BaseServiceImpl<SetMsgMapper,Msg> implements SetMsgService {
    @Override
    protected PropertyWrapper<Msg> makeSearchWrapper(Map<String, Object> condition) {
        return createWrapperUtil(condition)
                .like("name")
                .eq("mark")
                .getWrapper();
    }
}
