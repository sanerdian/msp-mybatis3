package com.jnetdata.msp.metadata.tstable.service;

import com.jnetdata.msp.core.service.BaseService;
import com.jnetdata.msp.metadata.tciteadd.model.TciteaddModel;

public interface TciteaddService extends BaseService<TciteaddModel> {
    boolean addlist(TciteaddModel entity);
}
