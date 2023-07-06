package com.jnetdata.msp.message.readuser.service;

import com.jnetdata.msp.core.service.BaseService;
import com.jnetdata.msp.message.msg.model.Msg;
import com.jnetdata.msp.message.readuser.model.ReaduserModel;

public interface ReaduserService extends BaseService<ReaduserModel> {
    ReaduserModel selectUser(Long userid,Long readid);
    void upUser(ReaduserModel readmodel);
    void insetUser(Long userid,Long readid);
    void insetUser1(Long userid,Long readid);
}
