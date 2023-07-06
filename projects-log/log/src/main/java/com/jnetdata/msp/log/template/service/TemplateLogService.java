package com.jnetdata.msp.log.template.service;

import com.jnetdata.msp.core.service.BaseService;
import com.jnetdata.msp.log.template.model.TemplateLog;

/**
 * Created by TF on 2019/3/13.
 */
public interface TemplateLogService extends BaseService<TemplateLog> {
    void addLog(String str, String tempname, Integer temptype, String requestIp,String columnName);
}
