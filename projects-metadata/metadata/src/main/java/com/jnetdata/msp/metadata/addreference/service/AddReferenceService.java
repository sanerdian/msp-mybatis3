package com.jnetdata.msp.metadata.addreference.service;

import com.jnetdata.msp.core.service.BaseService;
import com.jnetdata.msp.metadata.addreference.model.AddReferenceModel;
import org.thenicesys.data.api.EntityId;
import org.thenicesys.web.JsonResult;

public interface AddReferenceService extends BaseService<AddReferenceModel> {
    boolean addlist(AddReferenceModel entity);
}
