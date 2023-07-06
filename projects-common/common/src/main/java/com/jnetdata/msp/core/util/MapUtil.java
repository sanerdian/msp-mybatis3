package com.jnetdata.msp.core.util;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import org.springframework.cglib.beans.BeanMap;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;

public class MapUtil {
	public static Map<String,Object> toMap(Object object){
//		Map<String, Object> map = Maps.newHashMap();
//		BeanMap beanMap = BeanMap.create(object);
//		for (Object key : beanMap.keySet()) {
//			map.put(key + "", beanMap.get(key));
//		}
		String s = JSONObject.toJSONString(object);
		JSONObject json = JSONObject.parseObject(s);
		Map<String, Object> innerMap = json.getInnerMap();
		return innerMap;
	}

	public static Map<String,Object> toMapValueNotNull(Object object){
		Map<String, Object> map = Maps.newHashMap();
		BeanMap beanMap = BeanMap.create(object);
		for (Object key : beanMap.keySet()) {
			if(beanMap.get(key) == null) continue;
			map.put(key + "", beanMap.get(key));
		}
		return map;
	}

	public static Map<String,Object> toMapValueForEs(Object object){
		Map<String, Object> map = Maps.newHashMap();
		BeanMap beanMap = BeanMap.create(object);
		List<String> fields = Arrays.asList("commonKey", "searchFields", "highlightInfos", "hightlightFields");
		for (Object key : beanMap.keySet()) {
			if(beanMap.get(key) == null || fields.indexOf(key)>=0) continue;
			map.put(key + "", beanMap.get(key));
		}
		return map;
	}

	public static List<Map<String,Object>> toListMap(List object){
		List<Map<String,Object>> list = new ArrayList<>();
		int i = 0;
		for (Object o : object) {
			list.add(toMap(o));
		}
		return list;
	}

	public static Map<String,Object> toMapForFlowable(Object object){
		Map<String, Object> map = Maps.newHashMap();
		BeanMap beanMap = BeanMap.create(object);
		for (Object key : beanMap.keySet()) {
			String keystr = key + "";
			if (keystr.indexOf("Variables") >= 0 || keystr.indexOf("variable") >= 0 || keystr.equals("identityLinks") || keystr.equals("processDefinition")) {
				continue;
			}
			System.out.println(key);
			map.put(key + "", beanMap.get(key));
		}
		return map;
	}

	public static List<Map<String,Object>> toListMapForFlowable(List object){
		List<Map<String,Object>> list = new ArrayList<>();
		int i = 0;
		for (Object o : object) {
			list.add(toMapForFlowable(o));
		}
		return list;
	}

	public static Map<String,Object> toMapNotNull(Object object){
		Map<String, Object> map = Maps.newHashMap();
		BeanMap beanMap = BeanMap.create(object);
		for (Object key : beanMap.keySet()) {
			if(beanMap.get(key) == null) continue;
			map.put(key + "", beanMap.get(key));
		}
		return map;
	}

	public static List<Map<String,Object>> toListMapNotNull(List object){
		List<Map<String,Object>> list = new ArrayList<>();
		int i = 0;
		for (Object o : object) {
			list.add(toMapNotNull(o));
		}
		return list;
	}

	public static <T>T map2Obj(Map<String,Object> map,Class<T> clz){
		T obj = JSONObject.parseObject(new JSONObject(map).toJSONString(), clz);
		return obj;
	}

	public static <T>T map2Obj(JSONObject map,Class<T> clz){
		T obj = JSONObject.parseObject(map.toJSONString(), clz);
		return obj;
	}
}
