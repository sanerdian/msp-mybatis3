package com.jnetdata.msp.metadata.precise.service;

import com.jnetdata.msp.core.service.BaseService;
import com.jnetdata.msp.metadata.precise.model.PreciseModel;
import com.jnetdata.msp.metadata.precise.vo.PreciseVo;

public interface PreciseService extends BaseService<PreciseModel> {
    Boolean addlist(PreciseVo entity);
}
