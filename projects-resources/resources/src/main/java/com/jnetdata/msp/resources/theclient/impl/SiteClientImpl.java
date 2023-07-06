package com.jnetdata.msp.resources.theclient.impl;

import com.jnetdata.msp.manage.site.model.Site;
import com.jnetdata.msp.manage.site.service.SiteService;
import com.jnetdata.msp.resources.theclient.SiteClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("com.jnetdata.msp.resources.theclient.impl.SiteClientImpl")
public class SiteClientImpl implements SiteClient {

    // TODO 可根据需要改成微服务
    @Autowired
    private SiteService service;

    @Override
    public Site getById(Long siteId) {
        return service.getById(siteId);
    }
}
