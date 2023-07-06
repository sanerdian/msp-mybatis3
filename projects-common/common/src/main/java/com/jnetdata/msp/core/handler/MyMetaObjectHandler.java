package com.jnetdata.msp.core.handler;


import com.jnetdata.msp.core.model.util.IUser;
import com.jnetdata.msp.core.model.util.SessionUser;
import org.springframework.stereotype.Component;

@Component
public class MyMetaObjectHandler extends BaseMetaObjectHandler {

    @Override
    protected IUser getCurrentUser() {
        return  SessionUser.getCurrentUserWithoutException();
    }




}