package com.jnetdata.msp.manage.publish.explain.impl.beetl.tags;

import com.jnetdata.msp.constants.PublishConstants;
import com.jnetdata.msp.manage.publish.explain.common.constant.TagsConstant;
import com.jnetdata.msp.manage.publish.explain.impl.beetl.annoation.BeetlTagName;
import com.jnetdata.msp.manage.publish.explain.impl.beetl.core.GeneralTag;
import com.jnetdata.msp.manage.publish.explain.impl.beetl.tags.attr.RowNumTagAttr;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 */
@Service
@Scope("prototype")
@BeetlTagName(TagsConstant.JNET_ROWNUM)
public class RowNumTag extends GeneralTag<RowNumTagAttr> {
    @Override
    public void render() {
        int index =this.getContent();
        try {
            this.ctx.byteWriter.writeInteger(index);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getName() {
        return TagsConstant.JNET_ROWNUM;
    }

    @Override
    public String getPathName() {
        return PublishConstants.TAG_SEPERATE + getName();
    }
}
