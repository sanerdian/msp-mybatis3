package com.jnetdata.msp.visual.log.service;

import com.jnetdata.msp.core.service.BaseService;
import com.jnetdata.msp.visual.log.model.ElementModel;

/**
 * 功能描述：
 */
public interface ElementService extends BaseService<ElementModel> {
    void AddLog(String templateName,String operation);
}
