package com.jnetdata.msp.TauditLog.service;

import com.jnetdata.msp.TauditLog.model.TauditLogModel;
import com.jnetdata.msp.core.service.BaseService;



/**
 * 功能描述：
 */
public interface TauditLogService extends BaseService<TauditLogModel> {

    void addLog(Long status,String columnName,String operationStatus);
}
