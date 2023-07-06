package com.jnetdata.msp.manage.publish.explain.impl.beetl.tags;

import com.jnetdata.msp.constants.PublishConstants;
import com.jnetdata.msp.manage.publish.explain.common.constant.TagsConstant;
import com.jnetdata.msp.manage.publish.explain.impl.beetl.annoation.BeetlTagName;
import com.jnetdata.msp.manage.publish.explain.impl.beetl.core.GeneralTag;
import com.jnetdata.msp.manage.publish.explain.impl.beetl.tags.attr.ChannelLogoTagAttr;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

/**
 */
@Service
@Scope("prototype")
@BeetlTagName(TagsConstant.JNET_CHANNELLOGO)
public class ChannelLogoTag extends GeneralTag<ChannelLogoTagAttr> {
    @Override
    public void render() {

    }

    @Override
    public String getName() {
        return TagsConstant.JNET_CHANNELLOGO;
    }

    @Override
    public String getPathName() {
        return PublishConstants.TAG_SEPERATE + getName();
    }
}
