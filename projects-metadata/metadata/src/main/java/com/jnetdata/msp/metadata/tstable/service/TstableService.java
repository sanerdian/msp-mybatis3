package com.jnetdata.msp.metadata.tstable.service;

import com.jnetdata.msp.core.service.BaseService;
import com.jnetdata.msp.metadata.tstable.model.TstableModel;

public interface TstableService extends BaseService<TstableModel> {
    boolean addlist(TstableModel entity);
}
