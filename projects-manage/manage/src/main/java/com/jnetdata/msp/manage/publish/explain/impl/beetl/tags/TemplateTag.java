package com.jnetdata.msp.manage.publish.explain.impl.beetl.tags;

import com.jnetdata.msp.constants.PublishConstants;
import com.jnetdata.msp.core.model.PublishObj;
import com.jnetdata.msp.manage.publish.core.common.utils.PublishUtil;
import com.jnetdata.msp.manage.publish.core.publish.PublishContentManager;
import com.jnetdata.msp.manage.publish.explain.common.constant.TagsConstant;
import com.jnetdata.msp.manage.publish.explain.impl.beetl.annoation.BeetlTagName;
import com.jnetdata.msp.manage.publish.explain.impl.beetl.core.GeneralTag;
import com.jnetdata.msp.manage.publish.explain.impl.beetl.tags.attr.TemplateTagAttr;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by xujian on 2017/10/20.
 */
@Service
@Scope("prototype")
@BeetlTagName(TagsConstant.JNET_TEMPLATE)
public class TemplateTag extends GeneralTag<TemplateTagAttr> {

    @Resource
    private PublishContentManager publishContentManager;

    @Resource
    PublishUtil publishUtil;

    @Override
    public void render() {
        try {
            PublishObj obj = this.getPublishObj();
            Long siteId = obj.getSiteId();
            TemplateTagAttr templateTagAttr = this.getAttrs();
            String str = publishUtil.readTemplate(siteId, templateTagAttr.getTempname());
            publishContentManager.createTemplateInner(str, obj, this.ctx.byteWriter);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getName() {
        return TagsConstant.JNET_TEMPLATE;
    }

    @Override
    public String getPathName() {
        return PublishConstants.TAG_SEPERATE + getName();
    }
}
