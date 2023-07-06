package com.jnetdata.msp.message.phonemessage.service.impl;

import com.jnetdata.msp.message.phonemessage.service.PhoneMessageService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.thenicesys.store.message.sms.model.PhoneMessageProperties;
import org.thenicesys.store.message.sms.service.PhoneMessageUtil;

@Service
@Transactional(propagation= Propagation.REQUIRED)
public class PhoneMessageServiceImpl implements PhoneMessageService {
    private PhoneMessageProperties prop;

    public PhoneMessageServiceImpl(PhoneMessageProperties prop) {
        this.prop = prop;
    }

    public Boolean sendMsg(String mobs, String msg, String kFlag1) {
        return PhoneMessageUtil.sendMsg(this.prop, mobs, msg, kFlag1);
    }
}
