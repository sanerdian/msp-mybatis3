package com.jnetdata.msp.media.directive.tag;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jnetdata.msp.manage.column.mapper.ProgramaMapper;
import com.jnetdata.msp.manage.column.model.Programa;
import com.jnetdata.msp.media.directive.PublishConstant;
import com.jnetdata.msp.media.directive.tag.config.ChannelsConfig;
import com.jnetdata.msp.media.util.JsonLibUtil;
import com.jnetdata.msp.media.util.ReflactUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.velocity.context.InternalContextAdapter;
import org.apache.velocity.runtime.directive.Directive;
import org.apache.velocity.runtime.directive.DirectiveConstants;
import org.apache.velocity.runtime.parser.node.Node;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.thenicesys.data.api.Pair;
import org.thenicesys.mybatis.impl.PropertyWrapper;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.io.IOException;
import java.io.Writer;
import java.util.List;
import java.util.Map;

/**
 * @title 栏目列表置标处理
 */
@Component
public class ChannelsTag extends Directive{
	@Resource
	private ProgramaMapper mapper;
	private static ProgramaMapper programaMapper;
	@PostConstruct
	public void init(){
		ChannelsTag.programaMapper=this.mapper;
	}

	@Override
	public String getName() {
		return "jnet_channels";
	}

	@Override
	public int getType() {
		return DirectiveConstants.BLOCK;
	}

	@Override
	public boolean render(InternalContextAdapter context, Writer writer, Node node)
			throws IOException {
		//获取CMS上下文
		Map<String,Object> ctx = (Map<String, Object>) context.get(PublishConstant.MEDIA_CONTEXT);
		boolean result=true;
		String configJson = ((String) node.jjtGetChild(0).value(context));
		Node body = node.jjtGetChild(1);
		ChannelsConfig config = (ChannelsConfig) ReflactUtil.map2Obj(JsonLibUtil.json2Bean(configJson,Map.class),ChannelsConfig.class,true);

		List<Programa> chnlList=analyze(ctx,writer,node,config);
		if(chnlList!=null){
			Programa channel=null;
			for(int i=0;i<chnlList.size();i++){
				channel= chnlList.get(i);
				ctx.put(PublishConstant.MEDIA_CHANNEL_DATA,channel);
				ctx.put(PublishConstant.MEDIA_CHANNEL_INDEX, i+1);
				result=body.render(context, writer)&result;
			}
			//用完之后清空,否则会影响下个置标取数据
			ctx.remove(PublishConstant.MEDIA_CHANNEL_DATA);
			ctx.remove(PublishConstant.MEDIA_CHANNEL_INDEX);
			chnlList=null;
		}else{
//			logger.error("找不到当前栏目下的子栏目:"+JsonUtil.jsonObjectToString(config));
		}
		return result;
	}

	public List<Programa> analyze(Map<String,Object> ctx, Writer writer, Node node,ChannelsConfig config) {
		String owner = config.getOwner();
		PropertyWrapper wrapper= new PropertyWrapper(Programa.class);
		Map<String, Object> fieldsAndValues = ReflactUtil.getFieldsAndValues(config, Programa.class, true);
		if(ObjectUtils.isEmpty(owner)||"self".equalsIgnoreCase(owner)){//查询当前传入的栏目id的子栏目
			Long channelid= (Long) ctx.get(PublishConstant.MEDIA_CHANNEL_ID);
			fieldsAndValues.put("parentid",channelid);
		}else if("site".equalsIgnoreCase(owner)){//查询当前传入的网站id下的顶级栏目
			Long siteid= (Long) ctx.get(PublishConstant.MEDIA_SITE_ID);
			fieldsAndValues.put("siteid",siteid);
			fieldsAndValues.put("parentid",0);
		}else {
			throw new RuntimeException("jnet_channels预期外的owner取值:"+owner);
		}

		for(String key:fieldsAndValues.keySet()){
			Object value=fieldsAndValues.get(key);
			wrapper.eq(value!=null,key,value);
		}
		List<Pair<String, Boolean>> orderBy = config.getOrderBy();
		if(orderBy.size()>0){
			wrapper.orderBy(orderBy);
		}
		int size ;
		if(config.getNum()!=null&&config.getNum()>0){
			size=config.getNum();
		}else {
			size=Integer.MAX_VALUE;
		}
		Page page = new Page(1,size);
		List<Programa> chnlList=programaMapper.selectPage((IPage<Programa>) page,wrapper.getWrapper()).getRecords();
		return chnlList;
	}

}
