package com.jnetdata.msp.message.msg.utils;

import java.util.Map;

public class MapUtils {
    /**
     * 将Map参数转换为字符串
     *
     * @param map
     * @return
     */
    public static String mapToString(Map<String, Object> map) {
        StringBuffer sb = new StringBuffer();
        map.forEach((key, value) -> {
            sb.append(key).append("=").append(value.toString()).append("&");
        });
        String str = sb.toString();
        str = str.substring(0, str.length() - 1);
        return str;
    }

    /**
     * 将Bean对象转换Url请求的字符串
     *
     * @param t
     * @param <T>
     * @return
     */
    //public static <T> String getUrlByBean(T t) {
    //    String pre = "?";
    //    Map<String, Object> map = entityToMap(t);
    //    return pre + mapToString(map);
    //}
}
