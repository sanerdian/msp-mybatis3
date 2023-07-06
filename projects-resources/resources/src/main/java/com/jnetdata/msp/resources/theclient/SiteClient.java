package com.jnetdata.msp.resources.theclient;

import com.jnetdata.msp.manage.site.model.Site;

public interface SiteClient {

    Site getById(Long siteId);

}
