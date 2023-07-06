package com.jnetdata.msp.media.directive.tag.config;

import lombok.Data;

@Data
public class ChannelConfig extends BaseConfigDetail {
    String listurl;//列表页url

    @Override
    public String getUrl() {
        return this.getListurl();
    }
}
