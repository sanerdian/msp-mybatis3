package com.jnetdata.msp.message.msg.model;

import lombok.Data;

import java.util.TreeMap;

@Data
public class WxTemplateMsg {
    /**
     * 接收者openId
     */
    private String touser;
    /**
     * 模板ID
     */
    private String template_id;
    /**
     * 模板跳转链接
     */
    private String url;
 
    // "miniprogram":{ 未加入
    // "appid":"xiaochengxuappid12345",
    // "pagepath":"index?foo=bar"
    // },
 
    /**
     * data数据
     */
    private TreeMap<String, TreeMap<String, String>> data;
 
    /**
     * 参数
     *
     * @param value 值
     * @param color 颜色 可不填
     * @return params
     */
    public static TreeMap<String, String> item(String value, String color) {
        TreeMap<String, String> params = new TreeMap<String, String>();
        params.put("value", value);
        params.put("color", color);
        return params;
    }
}