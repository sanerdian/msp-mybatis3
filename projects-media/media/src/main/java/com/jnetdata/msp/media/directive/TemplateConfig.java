package com.jnetdata.msp.media.directive;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import com.jnetdata.msp.media.directive.tag.*;
import org.apache.velocity.runtime.RuntimeConstants;


public class TemplateConfig {
	public static Properties props = null;

	//public String templatePath = null;

	public String targetFile = null;

	private String tempalteHtml=null;

	public Map<String,Object> context = new HashMap<String,Object>();

	static{
		props = new Properties();
//		props.setProperty(RuntimeConstants.VM_LIBRARY, TemplateConfig.class.getResource("/").getPath()+"Macro_Library.vm");
		props.setProperty(RuntimeConstants.FILE_RESOURCE_LOADER_PATH, PublishConstant.FILE_RESOURCE_LOADER_PATH);
		props.setProperty(RuntimeConstants.ENCODING_DEFAULT,PublishConstant.ENCODING_DEFAULT);
		props.setProperty(RuntimeConstants.INPUT_ENCODING, PublishConstant.INPUT_ENCODING);
//		props.setProperty(RuntimeConstants.OUTPUT_ENCODING, PublishConstant.OUTPUT_ENCODING);
		//自定义标签
		props.setProperty("userdirective",
				ChannelTag.class.getName()
				+","+ ChannelsTag.class.getName()
				+","+ DocumentTag.class.getName()
				+","+ DocumentsTag.class.getName()
				+","+ SiteTag.class.getName()
		);
	}

	public TemplateConfig(){

	}

	public void setProperties(Properties props){
		TemplateConfig.props = props;
	}

	public void addProperties(String key,String value){
		props.setProperty(key, value);
	}

	public void addContextParam(String key,Object value){
		context.put(key, value);
	}

	public void setTargetPath(String path){
		this.targetFile = path;
	}
	public String getTempalteHtml() {
		return tempalteHtml;
	}

	public void setTempalteHtml(String tempalteHtml) {
		this.tempalteHtml = tempalteHtml;
	}

}
