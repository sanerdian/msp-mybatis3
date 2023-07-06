package com.jnetdata.msp.metadata.theclient.impl;

import com.jnetdata.msp.log.metadata.model.MetadataLog;
import com.jnetdata.msp.log.metadata.service.MetadataLogService;
import com.jnetdata.msp.metadata.theclient.MetadataLogClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("com.jnetdata.msp.metadata.theclient.impl.MetadataLogClientImpl")
public class MetadataLogClientImpl implements MetadataLogClient {

    @Autowired
    private MetadataLogService metadataLogService;

    @Override
    public void insert(MetadataLog metadataLog) {
        metadataLogService.insert(metadataLog);
    }

}
