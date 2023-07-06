package com.jnetdata.msp.resources.theclient.impl;

import com.jnetdata.msp.config.config.model.ConfigModel;
import com.jnetdata.msp.config.config.service.ConfigModelService;
import com.jnetdata.msp.resources.theclient.SystemClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("SystemClientImpl")
public class SystemClientImpl implements SystemClient {

    // TODO 可根据需要改成微服务
    @Autowired
    private ConfigModelService service;

    @Override
    public ConfigModel getById(Long id) {
        return service.getById(id);
    }
}
