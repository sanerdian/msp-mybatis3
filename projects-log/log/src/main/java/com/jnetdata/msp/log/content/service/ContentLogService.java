package com.jnetdata.msp.log.content.service;

import com.jnetdata.msp.core.service.BaseService;
import com.jnetdata.msp.log.content.model.ContentLog;

/**
 * Created by TF on 2019/3/13.
 */
public interface ContentLogService extends BaseService<ContentLog> {
    void addLog(String handleType,String contentType,String content,Boolean result);

}
