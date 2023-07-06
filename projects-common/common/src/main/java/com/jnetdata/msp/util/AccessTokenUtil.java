package com.jnetdata.msp.util;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSONObject;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 获取access_token工具类
 */
@Component
@Data
public class AccessTokenUtil {

    private static Map<String,Object> accessToken = new HashMap<>();

    static String appid = "wx9e62752e6f24846d";
    static String secret = "500db4a5ffe0188f1d8fe0dceeca04ba";

    //synchronized static可以防止同时被多实例化
    public synchronized static String getAccessToken() {
        //存放appId和secret 的properties文件
        //从数据库中查出accessToken对象
        //上次创建的时间
        Date creatTime = (Date)accessToken.get("createTime");
        long lastTime = 0;
        if(creatTime != null){
            lastTime = creatTime.getTime();
        }
        //将时间转换成毫秒值
        //获取到现在的毫秒值
        long nowTime = System.currentTimeMillis();
        //如果时差大于1小时59分则重新获取access_token
        if(nowTime - lastTime > 59*60*1000){
            //获取token url
            String url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid="
                    + appid + "&secret=" + secret;
            //发送http请求得到json流
            String result = HttpUtil.createGet(url).execute().body();
            JSONObject jobject = JSONObject.parseObject(result);
            //从json流中获取access_token
            String  access_token = (String) jobject.get("access_token");
            Integer ei = (Integer) jobject.get("expires_in");
            String expires_in = ei+"";
            //保存access_token
            if (access_token != null ) {
                Date d = new Date();
                accessToken.put("createTime",d);
                accessToken.put("accessToken",access_token);
                accessToken.put("expiresIn",expires_in);
            }
            return access_token;
        }
        return (String)accessToken.get("accessToken");
    }
    /**
     * 获取jsapi_ticket
     *
     * @param access_token
     * @return
     */
    public static synchronized  String getTicket(String access_token) {
        String ticket = null;
        String url = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token=" + access_token + "&type=jsapi";//这个url链接和参数不能变
        try {
            String message = HttpUtil.createGet(url).execute().body();
            JSONObject demoJson = JSONObject.parseObject(message);
            System.out.println("JSON字符串：" + demoJson);
            ticket = demoJson.getString("ticket");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ticket;
    }

    /**
     * SHA、SHA1加密
     *
     * @parameter： str：待加密字符串
     * @return： 加密串
     **/
    public static String getSHA1(String str) {
        try {
            MessageDigest digest = java.security.MessageDigest.getInstance("SHA-1"); //如果是SHA加密只需要将"SHA-1"改成"SHA"即可
            digest.update(str.getBytes());
            byte messageDigest[] = digest.digest();
            // Create Hex String
            StringBuffer hexStr = new StringBuffer();
            // 字节数组转换为 十六进制 数
            for (int i = 0; i < messageDigest.length; i++) {
                String shaHex = Integer.toHexString(messageDigest[i] & 0xFF);
                if (shaHex.length() < 2) {
                    hexStr.append(0);
                }
                hexStr.append(shaHex);
            }
            return hexStr.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }
}