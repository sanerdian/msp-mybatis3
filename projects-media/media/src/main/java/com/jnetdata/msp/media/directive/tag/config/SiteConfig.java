package com.jnetdata.msp.media.directive.tag.config;

import lombok.Data;

@Data
public class SiteConfig extends BaseConfigDetail {
    String weburl;//

    @Override
    public String getUrl() {
        return this.getWeburl();
    }
}
