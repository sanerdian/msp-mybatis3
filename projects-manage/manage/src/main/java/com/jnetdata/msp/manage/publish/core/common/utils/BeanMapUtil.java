package com.jnetdata.msp.manage.publish.core.common.utils;

import com.google.common.collect.Maps;
import net.sf.cglib.beans.BeanMap;
import org.apache.commons.collections.map.CaseInsensitiveMap;

import java.util.Map;

/**
 * @author penghe.li
 * @version 1.0
 * @description: TODO
 * @date 2022/8/30 12:53
 */
public class BeanMapUtil {

    /**
     * 将javabean对象转换为map
     */
    public static <T> Map<String, Object> beanToMap(T bean) {
        Map<String, Object> map = Maps.newHashMap();
        if (bean != null) {
            BeanMap beanMap = BeanMap.create(bean);
            for (Object key : beanMap.keySet()) {
                Object value = beanMap.get(key);
                map.put(String.valueOf(key), value);
            }
        }
        return map;
    }

    /**
     * 将javabean对象转换为map
     */
    public static <T> Map<String, Object> beanToCaseInsensitiveMap(T bean) {
        CaseInsensitiveMap map = new CaseInsensitiveMap();
        if (bean != null) {
            BeanMap beanMap = BeanMap.create(bean);
            for (Object key : beanMap.keySet()) {
                Object value = beanMap.get(key);
                map.put(String.valueOf(key), value);
            }
        }
        return map;
    }

    /**
     * 将map转换为javabean对象
     */
    public static <T> T mapToBean(Map<String, Object> map, T bean) {
        if (map != null) {
            BeanMap beanMap = BeanMap.create(bean);
            beanMap.putAll(map);
        }
        return bean;
    }
}
