package com.jnetdata.msp.core.context;

import lombok.extern.slf4j.Slf4j;

import java.io.Closeable;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author zyj
 */
@Slf4j
public class ThreadLocalContextUtil {

	private static ThreadLocal<Map<String,Object>> threadLocalContextMap = new  ThreadLocal<Map<String,Object>>() {
		@Override
		protected Map<String, Object> initialValue() {
			return new HashMap<String, Object>();
		}
	};
	
	public static Object putContext(String key, Object obj) {
		return threadLocalContextMap.get().put(key, obj);
	}
	
	public static Object getContext(String key) {
		return threadLocalContextMap.get().get(key);
	}
	
	public static Object removeKey(String key) {
		return threadLocalContextMap.get().remove(key);
	}
	
	public static void clearContext() {

		Map<String,Object> map = threadLocalContextMap.get();
		map.forEach((k,v) -> {
			if (v instanceof Closeable) {
				try {
					((Closeable) v).close();
				} catch (IOException e) {
					log.error(e.getMessage());
				}
			}
		});
		map.clear();
	}
	
	public static boolean containsKey(String key) {
		return threadLocalContextMap.get().containsKey(key);
	}
	
}
