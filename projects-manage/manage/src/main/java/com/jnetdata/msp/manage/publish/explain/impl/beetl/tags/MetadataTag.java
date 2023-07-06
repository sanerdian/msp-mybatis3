package com.jnetdata.msp.manage.publish.explain.impl.beetl.tags;

import com.jnetdata.msp.core.model.PublishObj;
import com.jnetdata.msp.manage.publish.core.common.utils.PublishUtil;
import com.jnetdata.msp.manage.publish.core.publish.PublishContentManager;
import com.jnetdata.msp.constants.PublishConstants;
import com.jnetdata.msp.manage.publish.explain.common.constant.TagsConstant;
import com.jnetdata.msp.manage.publish.explain.common.constant.TagsPropertyConstant;
import com.jnetdata.msp.manage.publish.explain.impl.beetl.annoation.BeetlTagName;
import com.jnetdata.msp.manage.publish.explain.impl.beetl.core.GeneralTag;
import com.jnetdata.msp.manage.publish.explain.impl.beetl.tags.attr.MetadataTagAttr;
import org.jsoup.Jsoup;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;

/**
 * Created by penghe.li
 */
@Service
@Scope("prototype")
@BeetlTagName(TagsConstant.JNET_METADATA)
public class MetadataTag extends GeneralTag<MetadataTagAttr> {

    @Resource
    PublishContentManager publishContentManager;

    @Resource
    PublishUtil publishUtil;


    @Override
    public void render() {
        try {
            PublishObj obj = this.getPublishObj();
            MetadataTagAttr attr = this.getAttrs();
            Map<String, Object> map = this.getContent();

            //必须嵌套  metadatas 使用
            if (map != null) {
                StringBuilder sb = new StringBuilder();
                String field = attr.getField().trim().toUpperCase();

                String fieldStr, fieldStrShort;
                if (TagsPropertyConstant.JNET_Property_URL.equalsIgnoreCase(field)) {
                    fieldStr = publishUtil.buildMetadataPubUrl(obj, map, attr.isUrlisabs());
                    fieldStrShort = fieldStr;
                } else {
                    fieldStr = publishUtil.readFieldValue(map.get(field), attr.getDateformat());
                    int length = attr.getLength();
                    if (length > 0) {
                        fieldStrShort = publishUtil.trancateString(fieldStr, length, attr.getTruncatedflag());
                    } else {
                        fieldStrShort = fieldStr;
                    }
                }
                if(attr.isOnlyText())fieldStrShort = Jsoup.parse(fieldStrShort).text();
                if (attr.isAutolink()) {
                    String linkAlt = attr.getLinkalt();
                    sb.append("<a href='").append(
                            publishUtil.buildMetadataPubUrl(obj, map, attr.isUrlisabs()))
                            .append("' target='").append(attr.getTarget()).append("'");
                    if (linkAlt != null) {
                        sb.append(" alt='").append(linkAlt).append("'");
                    }
                    sb.append(" title='").append(fieldStr).append("'");
                    sb.append(" >");
                    sb.append(fieldStrShort);
                    sb.append("</a>");
                } else {
                    sb.append(fieldStrShort);
                }
                this.ctx.byteWriter.writeString(sb.toString());
                //this.doBodyRender();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public String getName() {
        return TagsConstant.JNET_METADATA;
    }

    @Override
    public String getPathName() {
        return PublishConstants.TAG_SEPERATE + getName();
    }
}
