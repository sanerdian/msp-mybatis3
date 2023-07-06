package com.jnetdata.msp.core.model.util;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSONObject;

import java.util.Map;

/**
 * @Author: YUEHAO
 * @Date: 2020/1/14
 */
public class Weixinlogin {
    //获取openid
    public static Map getWxUserOpenid(String code, String APPID, String APPSecret) {
        //拼接url
        StringBuilder url = new StringBuilder("https://api.weixin.qq.com/sns/jscode2session?");
        url.append("appid=");//appid设置
        url.append(APPID);
        url.append("&secret=");//secret设置
        url.append(APPSecret);
        url.append("&js_code=");//code设置
        url.append(code);
        url.append("&grant_type=authorization_code");
        Map<String, Object> map = null;
        String content = null;
        Map res = null;
        try {
            content = HttpUtil.createGet(url.toString()).execute().body();
            System.out.println(content);//打印返回的信息

            res = JSONObject.parseObject(content);//把信息封装为json

            System.out.println(res);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

}
