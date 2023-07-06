package com.jnetdata.msp.filter;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.util.HtmlUtils;

import java.util.Arrays;
import java.util.List;

public class XssUtil {

    public static String xss_clean(String value) {
//        if (value != null) {
//            int isJson = isJson(value);
//            if(isJson == 1){
//                value = xssJson(value);
//            }else if(isJson == 2){
//                value = xssJsonArray(value);
//            }else{
//                value = HtmlUtils.htmlEscape(value);
////            value = StringEscapeUtils.escapeSql(value);
//            }
//        }
        return value;
    }

    public static Object toJson(String value) {
        if (value != null) {
            int isJson = isJson(value);
            if(isJson == 1){
                return JSONObject.parseObject(value);
            }else if(isJson == 2){
                return JSONArray.parseArray(value);
            }else{
                return value;
            }
        }
        return value;
    }

    public static String xssJsonArray(String value){
        JSONArray jsonArray = JSONArray.parseArray(value);
        for (Object o : jsonArray) {
            o = xss_clean(o.toString());
        }
        return jsonArray.toString();
    }

    public static String xssJson(JSONObject jsonObject){
        if(jsonObject.containsKey("sortProps")){
            jsonObject.put("sortProps", sqlSortProps(jsonObject.getString("sortProps")));
        }
        for(String str:jsonObject.keySet()){
            jsonObject.put(str,toJson(xss_clean(jsonObject.getString(str))));
        }
        return jsonObject.toString();
    }

    private static JSONArray sqlSortProps(String value){
        String str="^[0-9a-zA-Z_]{1,}$";
//        List<String> keys = Arrays.asList("createTime","seqencing","crtime");
        int json = isJson(value);
        JSONArray pars = new JSONArray();
        if(json == 2) pars = JSONArray.parseArray(value);
        else pars.add(JSONObject.parse(value));

        for (int i=pars.size()-1;i>=0;i--) {
            JSONObject o = pars.getJSONObject(i);
            if(!o.getString("key").matches(str)){
                pars.remove(i);
            }
        }
        return pars;
    }

    private static String doFormatSql(String value){
        String regex = "'|%|--|and|or|not|use|insert|delete|update|select|count|group|union" +
                "|create |drop|truncate|alter|grant|execute|exec|xp_cmdshell|call|declare|source|sql|from|SLEEP|(|)";
        if(StringUtils.isBlank(value)){
            return value;
        }
        return value.replaceAll("(?i)"+regex, "");//(?i)不区分大小写替换
    }

    public static String xssJson(String value){
        JSONObject jsonObject = JSONObject.parseObject(value);
        return xssJson(jsonObject);
    }

    public static int isJson(String content) {
        // 此处应该注意，不要使用StringUtils.isEmpty(),因为当content为"  "空格字符串时，JSONObject.parseObject可以解析成功，
        // 实际上，这是没有什么意义的。所以content应该是非空白字符串且不为空，判断是否是JSON数组也是相同的情况。
        if(StringUtils.isBlank(content))
            return 0;
        try {
            JSONObject.parseObject(content);
            return 1;
        } catch (Exception e) {
            try {
                JSONArray.parseArray(content);
                return 2;
            }catch (Exception e1){
                return 0;
            }
        }
    }
}
