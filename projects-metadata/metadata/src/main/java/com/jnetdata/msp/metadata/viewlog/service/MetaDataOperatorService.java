package com.jnetdata.msp.metadata.viewlog.service;

import com.jnetdata.msp.core.service.BaseService;
import com.jnetdata.msp.metadata.viewfieldinfo.model.ViewField;
import com.jnetdata.msp.metadata.viewlog.model.MetaDataOperator;

/**
 * 功能描述：
 */
public interface MetaDataOperatorService extends BaseService<MetaDataOperator> {

    void addLog(String handletype,String title);


    void AddLog(String handletype,String title );

}
