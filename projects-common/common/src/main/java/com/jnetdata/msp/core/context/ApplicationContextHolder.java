package com.jnetdata.msp.core.context;

import org.springframework.context.ApplicationContext;

/**
 * ApplicationContextHolder
 * @author zeng yuanjin
 */
public class ApplicationContextHolder {

	private static ApplicationContext applicationContext;

	public static ApplicationContext getApplicationContext() {
		return applicationContext;
	}

	public static void setApplicationContext(ApplicationContext applicationContext) {
		ApplicationContextHolder.applicationContext = applicationContext;
	}

	public static Object getBean(String name) {
		return getApplicationContext().getBean(name);
	}
	
	public static boolean testContext = false;

	public static boolean isTestContext() {
		return testContext;
	}

	public static void setTestContext(boolean testContext) {
		ApplicationContextHolder.testContext = testContext;
	}
		
}
