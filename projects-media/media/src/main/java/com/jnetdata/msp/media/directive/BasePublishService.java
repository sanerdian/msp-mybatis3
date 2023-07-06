package com.jnetdata.msp.media.directive;



import java.io.IOException;


/**
 * @title 发布模板配置父类
 * @author lxy
 * @date 2014-08-07
 * @version 1.2
 */
public abstract class BasePublishService {
	/**
	 * 初始化模板配置，然后解析模板，生成静态页面
	 * @param config
	 * @throws Exception
	 */
	public void mergeTemplate(TemplateConfig config) throws IOException {
		TemplateContext ctx = null;
		ctx = new TemplateContext(config);
		ctx.init();
		ctx.merge();
	}
}
