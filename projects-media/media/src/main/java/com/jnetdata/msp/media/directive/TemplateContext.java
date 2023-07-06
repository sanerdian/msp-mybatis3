package com.jnetdata.msp.media.directive;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;


import org.apache.commons.io.output.FileWriterWithEncoding;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



public class TemplateContext extends VelocityContext {
	protected TemplateConfig config;
	private static boolean doInit = false;
	private static Logger logger = LoggerFactory.getLogger(TemplateContext.class);

	public TemplateContext(TemplateConfig config) {
		this.config = config;
	}

	public void init() {
		if (!doInit) {
			synchronized (TemplateContext.class) {
				// 初始化模板引擎数据
				try {
					Velocity.init(TemplateConfig.props);
				} catch (Exception e) {
					logger.info(e.getMessage());
					throw e;
				}
				doInit = true;
			}
		}
		// 设置文档上下文
		this.put(PublishConstant.MEDIA_CONTEXT, config.context);
	}

	public void merge() throws IOException {
		try (BufferedWriter writer = new BufferedWriter(new FileWriterWithEncoding(new File(config.targetFile), PublishConstant.INPUT_ENCODING))) {
			boolean evaluate = Velocity.evaluate(this, writer, "", config.getTempalteHtml());
			writer.flush();
		} catch (IOException e) {
			logger.info(e.getMessage());
			throw e;
		}
	}
}
