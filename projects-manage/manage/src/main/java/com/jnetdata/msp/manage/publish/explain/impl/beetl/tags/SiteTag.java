package com.jnetdata.msp.manage.publish.explain.impl.beetl.tags;

import com.jnetdata.msp.constants.PublishConstants;
import com.jnetdata.msp.core.model.PublishObj;
import com.jnetdata.msp.manage.site.model.Site;
import com.jnetdata.msp.manage.publish.core.common.utils.BeanMapUtil;
import com.jnetdata.msp.manage.publish.core.common.utils.PublishUtil;
import com.jnetdata.msp.manage.publish.core.publish.PublishContentManager;
import com.jnetdata.msp.manage.publish.explain.common.constant.TagsConstant;
import com.jnetdata.msp.manage.publish.explain.impl.beetl.annoation.BeetlTagName;
import com.jnetdata.msp.manage.publish.explain.impl.beetl.core.GeneralTag;
import com.jnetdata.msp.manage.publish.explain.impl.beetl.tags.attr.SiteTagAttr;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Map;

/**
 * Created by xujian on 2017/10/20.
 */
@Service
@Scope("prototype")
@BeetlTagName(TagsConstant.JNET_SITE)
public class SiteTag extends GeneralTag<SiteTagAttr> {

    @Resource
    private PublishContentManager publishContentManager;

    @Resource
    PublishUtil publishUtil;

    @Override
    public void render() {
        try {
            PublishObj obj = this.getPublishObj();
            SiteTagAttr attr = this.getAttrs();

            Site webSite = this.getContent();
            if (webSite == null) {
                webSite = publishUtil.readSiteByAttr(obj, attr);
            }
            if (webSite != null) {
                Map<String, Object> map = BeanMapUtil.beanToCaseInsensitiveMap(webSite);
                StringBuilder sb = new StringBuilder();
                String field = attr.getField();
                String fieldStr = publishUtil.readFieldValue(map.get(field), attr.getDateformat());
                int length = attr.getLength();
                String fieldStrShort;
                if (length > 0) {
                    fieldStrShort = publishUtil.trancateString(fieldStr, length, attr.getTruncatedflag());
                } else {
                    fieldStrShort = fieldStr;
                }
                if (attr.isAutolink()) {
                    String url = publishUtil.buildSitePubUrl(obj, webSite.getId(),attr.isUrlisabs());
                    sb.append("<a href='").append(url).append("' target='").append(attr.getTarget())
                            .append("' title='").append(fieldStr).append("'")
                            .append(" >");
                    sb.append(fieldStrShort);
                    sb.append("</a>");
                } else {
                    sb.append(fieldStrShort);
                }
                this.ctx.byteWriter.writeString(sb.toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getName() {
        return TagsConstant.JNET_SITE;
    }

    @Override
    public String getPathName() {
        return PublishConstants.TAG_SEPERATE + getName();
    }
}
