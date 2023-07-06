package com.jnetdata.msp.media.directive.tag;

import com.jnetdata.msp.media.directive.PublishConstant;
import com.jnetdata.msp.media.directive.tag.config.DocumentConfig;
import com.jnetdata.msp.media.util.JsonLibUtil;
import com.jnetdata.msp.media.util.ReflactUtil;
import com.jnetdata.msp.tlujy.xinwen020.model.Xinwen020;
import org.apache.velocity.context.InternalContextAdapter;
import org.apache.velocity.runtime.directive.Directive;
import org.apache.velocity.runtime.directive.DirectiveConstants;
import org.apache.velocity.runtime.parser.node.Node;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.io.Writer;
import java.util.Map;

@Component
public class DocumentTag extends Directive {

	@Override
	public String getName() {
		return "jnet_document";
	}

	@Override
	public int getType() {
		return DirectiveConstants.BLOCK;
	}

	@Override
	public boolean render(InternalContextAdapter context, Writer writer, Node node) {
		//获取CMS上下文
		Map<String,Object> ctx = (Map<String, Object>) context.get(PublishConstant.MEDIA_CONTEXT);
		Node body = node.jjtGetChild(1);
		String configJson = ((String) node.jjtGetChild(0).value(context));
		Map map=JsonLibUtil.json2Bean(configJson,Map.class);
		DocumentConfig config = (DocumentConfig) ReflactUtil.map2Obj(map,DocumentConfig.class,true);

		boolean result=false;
		//文档置标属性值
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

	public String analyze(Map<String,Object> ctx, Writer writer, Node node, DocumentConfig config) {
		String field = config.getField();
		Xinwen020 xinwen = (Xinwen020) ctx.get(PublishConstant.MEDIA_DOC_DATA);
		if(ObjectUtils.isEmpty(field)){
			return config.getLink(xinwen.getDocpuburl(),xinwen.getTitle());
		}
		if("index".equalsIgnoreCase(field)){
			return String.valueOf(ctx.get(PublishConstant.MEDIA_DOC_INDEX));
		}
		if("url".equalsIgnoreCase(field)){
			field="docpuburl";
		}else if("name".equalsIgnoreCase(field)){
			field="title";
		}
		return config.fixMaxLength(ReflactUtil.getFieldStringValue(xinwen,field));
	}

}
