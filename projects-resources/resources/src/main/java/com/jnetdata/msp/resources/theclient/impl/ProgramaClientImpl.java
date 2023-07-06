package com.jnetdata.msp.resources.theclient.impl;

import com.jnetdata.msp.manage.column.model.Programa;
import com.jnetdata.msp.manage.column.service.ProgramaService;
import com.jnetdata.msp.resources.theclient.ProgramaClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("com.jnetdata.msp.resources.theclient.impl.ProgramaClientImpl")
public class ProgramaClientImpl implements ProgramaClient {

    // TODO 可根据需要改成微服务
    @Autowired
    private ProgramaService service;

    @Override
    public Programa getById(Long id) {
        return service.getById(id);
    }
}
