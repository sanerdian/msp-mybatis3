package com.jnetdata.msp.media.directive.tag;

import com.jnetdata.msp.manage.site.model.Site;
import com.jnetdata.msp.media.directive.PublishConstant;
import com.jnetdata.msp.media.directive.tag.config.SiteConfig;
import com.jnetdata.msp.media.util.JsonLibUtil;
import com.jnetdata.msp.media.util.ReflactUtil;
import org.apache.velocity.context.InternalContextAdapter;
import org.apache.velocity.runtime.directive.Directive;
import org.apache.velocity.runtime.directive.DirectiveConstants;
import org.apache.velocity.runtime.parser.node.Node;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.io.IOException;
import java.io.Writer;
import java.util.Map;

/**
 * @version 1.2
 * @title 站点置标处理
 */
@Component
public class SiteTag extends Directive {

    @Override
    public String getName() {
        return "jnet_site";
    }

    @Override
    public int getType() {
        return DirectiveConstants.BLOCK;
    }

    @Override
    public boolean render(InternalContextAdapter context, Writer writer, Node node) {

        //获取CMS上下文
        Map<String, Object> ctx = (Map<String, Object>) context.get(PublishConstant.MEDIA_CONTEXT);
        String configJson = (String) node.jjtGetChild(0).value(context);
        Node body = node.jjtGetChild(1);
        SiteConfig config = (SiteConfig) ReflactUtil.map2Obj(JsonLibUtil.json2Bean(configJson,Map.class),SiteConfig.class,true);

        boolean result = false;
        //栏目置标属性值
        String value = "";
        try {
            value = analyze(ctx, writer, node, config);
            writer.write(value);
            result = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    public String analyze(Map<String, Object> ctx, Writer writer, Node node, SiteConfig config) {
        String field = config.getField();
        Site site=(Site) ctx.get(PublishConstant.MEDIA_SITE_DATA);
        if(ObjectUtils.isEmpty(field)){
            return config.getLink(site.getWebUrl(),site.getName());
        }
        if("index".equalsIgnoreCase(field)){
            return String.valueOf(ctx.get(PublishConstant.MEDIA_DOC_INDEX));
        }
        if("url".equalsIgnoreCase(field)){
            field="weburl";
        }
        return config.fixMaxLength(ReflactUtil.getFieldStringValue(site, field));
    }
}
