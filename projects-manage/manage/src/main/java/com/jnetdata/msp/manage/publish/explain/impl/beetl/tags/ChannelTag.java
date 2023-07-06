package com.jnetdata.msp.manage.publish.explain.impl.beetl.tags;

import com.jnetdata.msp.column.model.Channel;
import com.jnetdata.msp.core.model.PublishObj;
import com.jnetdata.msp.manage.publish.core.common.utils.BeanMapUtil;
import com.jnetdata.msp.manage.publish.core.common.utils.PublishUtil;
import com.jnetdata.msp.manage.publish.core.publish.PublishContentManager;
import com.jnetdata.msp.constants.PublishConstants;
import com.jnetdata.msp.manage.publish.explain.common.constant.TagsConstant;
import com.jnetdata.msp.manage.publish.explain.common.constant.TagsPropertyConstant;
import com.jnetdata.msp.manage.publish.explain.impl.beetl.annoation.BeetlTagName;
import com.jnetdata.msp.manage.publish.explain.impl.beetl.core.GeneralTag;
import com.jnetdata.msp.manage.publish.explain.impl.beetl.tags.attr.ChannelTagAttr;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;

/**
 *
 */
@Service
@Scope("prototype")
@BeetlTagName(TagsConstant.JNET_CHANNEL)
public class ChannelTag extends GeneralTag<ChannelTagAttr> {

    @Resource
    PublishContentManager publishContentManager;

    @Resource
    PublishUtil publishUtil;

    @Override
    public void render() {
        try {
            PublishObj obj = this.getPublishObj();
            ChannelTagAttr attr = this.getAttrs();
            Channel channel = this.getContent();

            if (channel == null) {
                channel = publishUtil.readChannelByAttr(obj, attr);
            }
            //必须嵌套  metadatas 使用
            if (channel != null) {
                Map<String, Object> map = BeanMapUtil.beanToCaseInsensitiveMap(channel);
                StringBuilder sb = new StringBuilder();
                String field = attr.getField();
                String fieldStr, fieldStrShort;
                if (TagsPropertyConstant.JNET_Property_URL.equalsIgnoreCase(field)) {
                    fieldStr = publishUtil.buildChannelPubUrl(obj, channel.getId(), attr.isUrlisabs());
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
                if (attr.isAutolink()) {
                    String url = publishUtil.buildChannelPubUrl(obj, channel.getId(), attr.isUrlisabs());
                    sb.append("<a href='").append(url).append("' target='").append(attr.getTarget())
                            .append("' title='").append(fieldStr).append("'")
                            .append(" >");
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
        return TagsConstant.JNET_CHANNEL;
    }

    @Override
    public String getPathName() {
        return PublishConstants.TAG_SEPERATE + getName();
    }
}
