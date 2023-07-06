package com.jnetdata.msp.manage.publish.explain.impl.beetl.tags;

import com.jnetdata.msp.constants.PublishConstants;
import com.jnetdata.msp.manage.publish.explain.common.constant.TagsConstant;
import com.jnetdata.msp.manage.publish.explain.impl.beetl.annoation.BeetlTagName;
import com.jnetdata.msp.manage.publish.explain.impl.beetl.core.GeneralTag;
import com.jnetdata.msp.manage.publish.explain.impl.beetl.tags.attr.PreDocumentsTagAttr;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

/**
 *
 */
@Service
@Scope("prototype")
@BeetlTagName(TagsConstant.JNET_PREDOCUMENTS)
public class PreDocumentsTag extends GeneralTag<PreDocumentsTagAttr> {
    @Override
    public void render() {

    }

    @Override
    public String getName() {
        return TagsConstant.JNET_PREDOCUMENTS;
    }

    @Override
    public String getPathName() {
        return PublishConstants.TAG_SEPERATE + getName();
    }
}
