package com.jnetdata.msp.log.metadata.service;

import com.jnetdata.msp.core.service.BaseService;
import com.jnetdata.msp.log.metadata.model.MetadataLog;

import java.util.Map;

/**
 * Created by TF on 2019/3/13.
 */
public interface MetadataLogService extends BaseService<MetadataLog> {
    void addFieldLog(Map<String, Object> old, Map<String, Object> newObj,String ip,Long id);

    void addlogd(Map<String, Object> newObj,String ip);
}
