package com.jnetdata.msp.metadata.viewfieldinfo.service;

import com.jnetdata.msp.core.service.BaseService;
import com.jnetdata.msp.metadata.viewfieldinfo.model.ViewField;

public interface ViewFieldService extends BaseService<ViewField> {

    void deleteByViewId(Long id);

    void updateViewGen(Long id);

    Long[] getVFId(Long id);
}
