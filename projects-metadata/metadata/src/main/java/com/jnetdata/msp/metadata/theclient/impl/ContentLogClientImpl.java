package com.jnetdata.msp.metadata.theclient.impl;

import com.jnetdata.msp.log.content.model.ContentLog;
import com.jnetdata.msp.log.content.service.ContentLogService;
import com.jnetdata.msp.metadata.theclient.ContentLogClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("com.jnetdata.msp.metadata.theclient.impl.ContentLogClientImpl")
public class ContentLogClientImpl implements ContentLogClient {

    @Autowired
    private ContentLogService contentLogService;

    @Override
    public void insert(ContentLog contentLog) {
        contentLogService.insert(contentLog);
    }
}
