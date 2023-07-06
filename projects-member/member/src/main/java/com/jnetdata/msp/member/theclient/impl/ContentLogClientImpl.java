package com.jnetdata.msp.member.theclient.impl;

import com.jnetdata.msp.log.content.model.ContentLog;
import com.jnetdata.msp.log.content.service.ContentLogService;
import com.jnetdata.msp.member.theclient.ContentLogClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("ContentLogClientImpl")
public class ContentLogClientImpl implements ContentLogClient {

    @Autowired
    private ContentLogService contentLogService;

    /**
     * 添加日志
     * @param log
     * @return
     */
    @Override
    public boolean insert(ContentLog log) {
        return contentLogService.insert(log);
    }

}
