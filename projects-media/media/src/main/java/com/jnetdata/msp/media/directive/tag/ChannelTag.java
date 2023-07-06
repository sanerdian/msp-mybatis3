package com.jnetdata.msp.media.directive.tag;

import com.jnetdata.msp.manage.column.model.Programa;
import com.jnetdata.msp.media.directive.PublishConstant;
import com.jnetdata.msp.media.directive.tag.config.ChannelConfig;
import com.jnetdata.msp.media.util.JsonLibUtil;
import com.jnetdata.msp.media.util.ReflactUtil;
import org.apache.velocity.context.InternalContextAdapter;
import org.apache.velocity.runtime.directive.Directive;
import org.apache.velocity.runtime.directive.DirectiveConstants;
import org.apache.velocity.runtime.parser.node.Node;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.io.Writer;
import java.util.Map;


/**
 * @title 栏目置标处理
 */
@Component
public class ChannelTag extends Directive{
	@Override
	public String getName() {
		return "jnet_channel";
	}

	@Override
	public int getType() {
		return DirectiveConstants.BLOCK;
	}

	@Override
	public boolean render(InternalContextAdapter context, Writer writer, Node node) {
		//获取CMS上下文
		Map<String,Object> ctx = (Map<String, Object>) context.get(PublishConstant.MEDIA_CONTEXT);
		//int childNum = node.jjtGetNumChildren();
		//Node body = node.jjtGetChild(1);
		String configJson = ((String) node.jjtGetChild(0).value(context));
		Node body = node.jjtGetChild(1);
		ChannelConfig config = (ChannelConfig) ReflactUtil.map2Obj(JsonLibUtil.json2Bean(configJson,Map.class),ChannelConfig.class,true);

		boolean result=false;
		//栏目置标属性值
		String value ="";
		try {
			value=analyze(ctx,writer,node,config);
			writer.write(value);
			result=true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public  String analyze(Map<String,Object> ctx, Writer writer, Node node,ChannelConfig config) {
		String field=config.getField();
		Programa programa = (Programa) ctx.get(PublishConstant.MEDIA_CHANNEL_DATA);
		if(ObjectUtils.isEmpty(field)){
			return config.getLink(programa.getListUrl(),programa.getName());
		}
		if("index".equalsIgnoreCase(field)){
			return String.valueOf(ctx.get(PublishConstant.MEDIA_CHANNEL_INDEX));
		}
		if("url".equalsIgnoreCase(field)){
			field="listurl";
		}

		return config.fixMaxLength(ReflactUtil.getFieldStringValue(programa,field));
	}

}
