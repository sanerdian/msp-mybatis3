package com.jnetdata.msp.log.publish.service;

import com.jnetdata.msp.core.service.BaseService;
import com.jnetdata.msp.log.publish.model.PublishLog;

/**
 * Created by TF on 2019/3/13.
 */
public interface PublishLogService extends BaseService<PublishLog> {
    void addLog(String ip, String filePath, String fileName, String columnName);
}
