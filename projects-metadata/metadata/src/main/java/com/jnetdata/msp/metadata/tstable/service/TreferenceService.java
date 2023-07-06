package com.jnetdata.msp.metadata.tstable.service;

import com.jnetdata.msp.core.service.BaseService;
import com.jnetdata.msp.metadata.treference.model.TreferenceModel;
import com.jnetdata.msp.metadata.tstable.model.TstableModel;

import java.util.List;

public interface TreferenceService extends BaseService<TreferenceModel> {
    boolean addlist(TreferenceModel entity);

    List<TreferenceModel> selectBay(Long columnid);
}
