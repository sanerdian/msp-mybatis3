package com.jnetdata.msp.core.context;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

/**
 * ApplicationContext Servlet 用于将当前ApplicationContext设置到ApplicationContextHolder
 * @author zeng yuanjin
 */
@Slf4j
public class ApplicationContextServlet extends HttpServlet {

	@Override
	public void destroy() {
		log.info("<<<<< --- ApplicationContextServlet::destroy() --- ");
		ThreadLocalContextUtil.clearContext();
		super.destroy();
	}

	@Override
	public void init() throws ServletException {
		
		ApplicationContext ac = WebApplicationContextUtils.getWebApplicationContext(this.getServletContext());
		ApplicationContextHolder.setApplicationContext(ac);
		
		log.debug("<<<<< --- ApplicationContextServlet::init() ----,  applicatinoConext=" + ac);

		super.init();
		
	}
	
}
