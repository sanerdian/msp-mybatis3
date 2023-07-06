package com.jnetdata.msp.metadata.encoding.service;

import com.jnetdata.msp.metadata.encoding.model.Encodingmodel;
import com.jnetdata.msp.core.service.BaseService;


public interface EncodingService extends BaseService<Encodingmodel> {
    Boolean selectlist(Encodingmodel vo);

    Boolean uplist(Encodingmodel vo);

    Encodingmodel selectse(String vo);

}
