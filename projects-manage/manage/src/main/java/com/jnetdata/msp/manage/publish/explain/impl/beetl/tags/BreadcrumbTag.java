package com.jnetdata.msp.manage.publish.explain.impl.beetl.tags;

import com.jnetdata.msp.column.model.Channel;
import com.jnetdata.msp.constants.PublishConstants;
import com.jnetdata.msp.core.model.PublishObj;
import com.jnetdata.msp.manage.publish.core.common.utils.PublishUtil;
import com.jnetdata.msp.manage.publish.core.publish.PublishContentManager;
import com.jnetdata.msp.manage.publish.explain.common.constant.TagsConstant;
import com.jnetdata.msp.manage.publish.explain.impl.beetl.annoation.BeetlTagName;
import com.jnetdata.msp.manage.publish.explain.impl.beetl.core.GeneralTag;
import com.jnetdata.msp.manage.publish.explain.impl.beetl.tags.attr.BreadcrumbTagAttr;
import com.jnetdata.msp.manage.publish.explain.impl.beetl.tags.attr.MetadatasTagAttr;
import com.jnetdata.msp.manage.site.model.Site;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.MessageFormat;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by penghe.li
 */
@Service
@Scope("prototype")
@BeetlTagName(TagsConstant.JNET_BREADCRUMB)
public class BreadcrumbTag extends GeneralTag<BreadcrumbTagAttr> {

    @Resource
    PublishContentManager publishContentManager;

    @Resource
    PublishUtil publishUtil;


    @Override
    public void render() {
        try {
            PublishObj publishObj = this.getPublishObj();
            BreadcrumbTagAttr attr = this.getAttrs();

            List<Channel> channels = publishUtil.readRoute(publishObj.getChannelId());
            String route = attr.getHomepagedesc() + attr.getValue();
            if(attr.isAutoSite()){
                Site site = publishUtil.readSiteBySiteId(publishObj.getSiteId());
                if (attr.isAutolink()) {
                    route += MessageFormat.format("<a href=''{0}'' target=''{1}''>{2}</a>", publishUtil.buildSitePubUrl(publishObj, site.getId(), false), attr.getTarget(), site.getName());
                }else{
                    route += site.getName();
                }
                route +=attr.getValue();
            }
            List<String> names;
            if (attr.isAutolink()) {
                names = channels.stream().map(m -> MessageFormat.format("<a href=''{0}'' target=''{1}''>{2}</a>", publishUtil.buildChannelPubUrl(publishObj, m.getId(), false),attr.getTarget(),m.getName())).collect(Collectors.toList());
            } else {
                names = channels.stream().map(m -> m.getName()).collect(Collectors.toList());
            }
            route += String.join(attr.getValue(), names);
            this.ctx.byteWriter.writeString(route);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getName() {
        return TagsConstant.JNET_BREADCRUMB;
    }

    @Override
    public String getPathName() {
        return PublishConstants.TAG_SEPERATE + getName();
    }
}
