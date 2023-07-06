package com.jnetdata.msp.media.directive.tag;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jnetdata.msp.media.directive.PublishConstant;
import com.jnetdata.msp.media.directive.tag.config.BaseConfigList;
import com.jnetdata.msp.media.directive.tag.config.DocumentsConfig;
import com.jnetdata.msp.media.util.JsonLibUtil;
import com.jnetdata.msp.media.util.ReflactUtil;
import com.jnetdata.msp.tlujy.xinwen020.mapper.Xinwen020Mapper;
import com.jnetdata.msp.tlujy.xinwen020.model.Xinwen020;
import org.apache.velocity.context.InternalContextAdapter;
import org.apache.velocity.runtime.directive.Directive;
import org.apache.velocity.runtime.directive.DirectiveConstants;
import org.apache.velocity.runtime.parser.node.Node;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.thenicesys.data.api.Pair;
import org.thenicesys.mybatis.impl.PropertyWrapper;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.Writer;
import java.util.List;
import java.util.Map;

/**
 * @title 文档列表置标处理
 */
@Component
public class DocumentsTag extends Directive {
	@Autowired
	private Xinwen020Mapper mapper;
	private static Xinwen020Mapper xinwenMapper;
	@PostConstruct
	public void init(){
		xinwenMapper=this.mapper;
	}

	@Override
	public String getName() {
		return "jnet_documents";
	}

	@Override
	public int getType() {
		return DirectiveConstants.BLOCK;
	}

	@Override
	public boolean render(InternalContextAdapter context, Writer writer, Node node) throws IOException {
		//获取CMS上下文
		Map<String,Object> ctx = (Map<String, Object>) context.get(PublishConstant.MEDIA_CONTEXT);
		boolean result=true;
		Node body = node.jjtGetChild(1);
		String configJson = (String) node.jjtGetChild(0).value(context);
		DocumentsConfig config = (DocumentsConfig) ReflactUtil.map2Obj(JsonLibUtil.json2Bean(configJson,Map.class),DocumentsConfig.class,true);

		List<Xinwen020> docList=null;
		docList=analyze(ctx,writer,node,config);
		Integer total = (Integer) ctx.get(PublishConstant.MEDIA_PAGE_TOTAL);
		Integer pageSize = (Integer) ctx.get(PublishConstant.MEDIA_PAGE_SIZE);
        Integer pageNo = (Integer) ctx.get(PublishConstant.MEDIA_PAGE_CURRENT);
		String urlFirst = (String) ctx.get(PublishConstant.MEDIA_PAGE_URL_FIRST);
		int baseIndex=0;
        if(pageSize>0&&pageNo>0){
            baseIndex=(pageNo-1)*pageSize;
        }
		if(docList!=null){
			for(int i=0;i<docList.size();i++){
				ctx.put(PublishConstant.MEDIA_DOC_DATA, docList.get(i));
				ctx.put(PublishConstant.MEDIA_DOC_INDEX, baseIndex+i+1);
				result=body.render(context, writer)&result;
			}
			writer.write(BaseConfigList.getPager(pageNo,pageSize,total,urlFirst));
			//用完后去掉,否则影响下一篇文档的读取
			ctx.remove(PublishConstant.MEDIA_DOC_DATA);
			ctx.remove(PublishConstant.MEDIA_DOC_INDEX);
			docList=null;
		}

		return result;
	}

	public List<Xinwen020> analyze(Map<String,Object> ctx, Writer writer, Node node, DocumentsConfig config) {
		Long channelid = (Long) ctx.get(PublishConstant.MEDIA_CHANNEL_ID);
		PropertyWrapper wrapper = new PropertyWrapper(Xinwen020.class);
		Map<String, Object> entity = ReflactUtil.getFieldsAndValues(config,Xinwen020.class,true);
		entity.put("columnid",channelid);
		entity.put("status","21");
		entity.put("docstatus",0);
		for(String key:entity.keySet()){
			wrapper.eq(!ObjectUtils.isEmpty(entity.get(key)),key,entity.get(key));
		}
		List<Pair<String, Boolean>> orderBy = config.getOrderBy();
		if(orderBy.size()>0){
			wrapper.orderBy(orderBy);
		}
		//分页查询规则：优先尝试使用分页查询，再次尝试获取num(限定查询的记录数)，否则查询全部
        Integer pageSize = (Integer) ctx.get(PublishConstant.MEDIA_PAGE_SIZE);
        Integer pageNo = (Integer) ctx.get(PublishConstant.MEDIA_PAGE_CURRENT);
        Integer num = config.getNum();
        int current,size;
        if(pageSize!=null&&pageNo!=null&&pageSize>0&&pageNo>0){
            current=pageNo;
            size=pageSize;
        }else if(num!=null&&num>0){
            current=1;
            size=num;
        }else {
            current=1;
            size=Integer.MAX_VALUE;
        }
        Page<Xinwen020> page = new Page(current, size);
		return xinwenMapper.selectPage((IPage<Xinwen020>)page,wrapper.getWrapper()).getRecords();
	}
}
