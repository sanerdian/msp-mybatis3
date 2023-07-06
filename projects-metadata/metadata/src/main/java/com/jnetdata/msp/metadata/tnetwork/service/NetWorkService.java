package com.jnetdata.msp.metadata.tnetwork.service;

import com.jnetdata.msp.core.service.BaseService;
import com.jnetdata.msp.metadata.pushgroup.model.PushgroupModel;
import com.jnetdata.msp.metadata.tnetwork.model.NetWorkModel;

public interface NetWorkService extends BaseService<NetWorkModel> {
    int insertba(NetWorkModel netWorkModel);

}
