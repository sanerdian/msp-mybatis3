package com.jnetdata.msp.util;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import com.jnetdata.msp.core.controller.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author zyj
 * @since 2020-08-04
 */
@Controller
@RequestMapping("/wx")
public class WXController extends BaseController{

    private static Map<String,String> wxUserInfo = new HashMap<>();

    /**
     * 获取微信签名
     *
     */
    @GetMapping(value = "/signature")
    @ResponseBody
    public Map<String, Object> WapSignSignatureAction(String url, HttpServletRequest request, HttpServletResponse response)throws Exception {
        Map<String, Object> data = new HashMap<String, Object>();
        Map<String, Object> wxInfo = new HashMap<>();
        try {
            String accessToken = AccessTokenUtil.getAccessToken();
            String jsapiTicket = AccessTokenUtil.getTicket(accessToken);
            String noncestr = UUID.randomUUID().toString().replace("-", "").substring(0, 16);//随机字符串
            String timestamp = String.valueOf(System.currentTimeMillis()/ 1000);// 时间戳
            String params = "jsapi_ticket=" + jsapiTicket + "&noncestr=" + noncestr + "&timestamp=" + timestamp + "&url=" + url;
            //将字符串进行sha1加密
            String signature = AccessTokenUtil.getSHA1(params);
            //微信appId
            String appId = AccessTokenUtil.appid;
            wxInfo.put("appId", appId);
            wxInfo.put("accessToken", accessToken);
            wxInfo.put("jsapi_ticket", jsapiTicket);
            wxInfo.put("timestamp", timestamp);
            wxInfo.put("nonceStr", noncestr);
            wxInfo.put("signature", signature);
            wxInfo.put("msg", "success");
            wxInfo.put("status", "1");
            wxInfo.put("code", "1");
            return wxInfo;
        } catch (Exception e) {
            wxInfo.put("code", "0");
            wxInfo.put("status", "0");
            wxInfo.put("msg", "error");
            return wxInfo;
        }
    }

    /**
     * 获取微信签名
     *
     */
    @GetMapping(value = "/token")
    @ResponseBody
    public String token(){
        return AccessTokenUtil.getAccessToken();
    }

    @GetMapping("/msgreply")
    @ResponseBody
    public void getmsgreply(HttpServletRequest request, HttpServletResponse response) {
        isProcessRequest(request,response);
    }

    @PostMapping("/msgreply")
    @ResponseBody
    public String postmsgreply(HttpServletRequest request, @RequestBody String body) throws UnsupportedEncodingException {

        JSONObject obj = new JSONObject(body);

        if(!obj.getStr("MsgType").equals("text")){
            return null;
        }

        String key = obj.getStr("Content");
        request.setCharacterEncoding("UTF-8");         //返回页面防止出现中文乱码
        String FromUserName = obj.getStr("FromUserName");

        acceptMessage(FromUserName,new String[]{"1","2"},key);
        return null;
    }

    @GetMapping("/getCodeToken")
    @ResponseBody
    public String getCodeToken(String code) {
        String body = HttpUtil.createGet("https://api.weixin.qq.com/sns/oauth2/access_token?appid="+AccessTokenUtil.appid+"&secret="+AccessTokenUtil.secret+"&code="+code+"&grant_type=authorization_code").execute().body();
        return body;
    }

    @GetMapping("/getWxUserinfo")
    @ResponseBody
    public JSONObject getWxUserinfo(String code) {
        if(wxUserInfo.containsKey(code)){
            return new JSONObject(wxUserInfo.get(code));
        }
        String body = HttpUtil.createGet("https://api.weixin.qq.com/sns/oauth2/access_token?appid="+AccessTokenUtil.appid+"&secret="+AccessTokenUtil.secret+"&code="+code+"&grant_type=authorization_code").execute().body();
        System.out.println("获取用户信息token:"+body);
        JSONObject jsonObject = new JSONObject(body);
        String userinfo = null;
        if(jsonObject.containsKey("access_token")){
            userinfo = HttpUtil.createGet("https://api.weixin.qq.com/sns/userinfo?access_token=" + jsonObject.get("access_token") + "&openid=" + jsonObject.get("openid") + "&lang=zh_CN").execute().body();
            wxUserInfo.put(code,userinfo);
            System.out.println("微信用户信息:"+userinfo);
        }
        return new JSONObject(userinfo);
    }

    /**
     * 聊天处理方法
     * @throws Exception
     */
    public void acceptMessage(String openid,String[] url,String title){
        Map<String,Object> map1 = new HashMap<>();
//        map1.put("content","<a href='"+url+"'>"+title+"</a>");
        map1.put("title",title);
        map1.put("description",title);
        map1.put("thumb_url",url[1]);
        map1.put("url",url[0]);
        Map<String,Object> map = new HashMap<>();
        map.put("touser",openid);
        map.put("msgtype","link");
//        map.put("text",map1);
        map.put("link",map1);
        JSONObject jsonObj=new JSONObject(map);
        String accessToken = AccessTokenUtil.getAccessToken();
        String tourl="https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token="+accessToken;
        String result = HttpUtil.createPost(tourl)
                .body(jsonObj.toString())
                .execute()
                .body();
    }

    /**
     * 聊天处理方法
     * @throws Exception
     */
    public void acceptTextMessage(String openid,String msg){

        Map<String,Object> map1 = new HashMap<>();
        map1.put("content",msg);
        Map<String,Object> map = new HashMap<>();
        map.put("touser",openid);
        map.put("msgtype","text");
        map.put("text",map1);
        JSONObject jsonObj=new JSONObject(map);
        String accessToken = AccessTokenUtil.getAccessToken();
        String tourl="https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token="+accessToken;
        String result = HttpUtil.createPost(tourl)
                .body(jsonObj.toString())
                .execute()
                .body();
    }
    /**
     * 验证消息推送Token
     */
    public void isProcessRequest(HttpServletRequest request, HttpServletResponse response){
        // 微信加密签名
        String signature = request.getParameter("signature");
        // 时间戳
        String timestamp = request.getParameter("timestamp");
        // 随机数
        String nonce = request.getParameter("nonce");
        // 随机字符串
        String echostr = request.getParameter("echostr");

        PrintWriter out = null;
        // 通过检验signature对请求进行校验，若校验成功则原样返回echostr，表示接入成功，否则接入失败
        if (signature != null && checkSignature(signature, timestamp, nonce)) {
            try {
                out = response.getWriter();
                // 通过检验signature对请求进行校验，若校验成功则原样返回echostr，否则接入失败  
                out.print(echostr);
                out.flush(); //必须刷新
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                out.close();
            }
        } else {

        }
    }

    /**
     * 验证签名
     *
     * @param signature
     * @param timestamp
     * @param nonce
     * @return
     */
    public static boolean checkSignature(String signature, String timestamp, String nonce) {
        String token="NCi06DwiLs3x8Gi7";//要和我们小程序管理员配置的一样才行
        String[] arr = new String[]{token, timestamp, nonce};
        // 将token、timestamp、nonce三个参数进行字典序排序
        // Arrays.sort(arr);
        sort(arr);
        StringBuilder content = new StringBuilder();
        for (int i = 0; i < arr.length; i++) {
            content.append(arr[i]);
        }
        MessageDigest md = null;
        String tmpStr = null;

        try {
            md = MessageDigest.getInstance("SHA-1");
            // 将三个参数字符串拼接成一个字符串进行sha1加密
            byte[] digest = md.digest(content.toString().getBytes());
            tmpStr = byteToStr(digest);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        content = null;
        // 将sha1加密后的字符串可与signature对比，标识该请求来源于微信
        return tmpStr != null ? tmpStr.equals(signature.toUpperCase()) : false;
    }

    /**
     * 将字节数组转换为十六进制字符串
     *
     * @param byteArray
     * @return
     */
    private static String byteToStr(byte[] byteArray) {
        String strDigest = "";
        for (int i = 0; i < byteArray.length; i++) {
            strDigest += byteToHexStr(byteArray[i]);
        }
        return strDigest;
    }

    /**
     * 将字节转换为十六进制字符串
     *
     * @param mByte
     * @return
     */
    private static String byteToHexStr(byte mByte) {
        char[] Digit = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        char[] tempArr = new char[2];
        tempArr[0] = Digit[(mByte >>> 4) & 0X0F];
        tempArr[1] = Digit[mByte & 0X0F];
        String s = new String(tempArr);
        return s;
    }

    public static void sort(String a[]) {
        for (int i = 0; i < a.length - 1; i++) {
            for (int j = i + 1; j < a.length; j++) {
                if (a[j].compareTo(a[i]) < 0) {
                    String temp = a[i];
                    a[i] = a[j];
                    a[j] = temp;
                }
            }
        }
    }

}

