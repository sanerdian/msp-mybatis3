package com.jnetdata.msp.metadata.Push.service;

import com.jnetdata.msp.core.service.BaseService;
import com.jnetdata.msp.metadata.push.model.PushModel;

public interface PushService extends BaseService<PushModel> {
    PushModel selectpush(PushModel vo);

    /*Long selectlist(Long pushid, Long nameid);*/
}
