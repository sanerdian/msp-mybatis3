package com.jnetdata.msp.message.msg.utils;

import java.util.Arrays;

/**
     * Description：请求校验
     * 将token、timestamp、nonce三个参数进行字典序排序
     * @author jiaoqianjin
     * Date: 2020/8/19 9:44
     **/

    public class CheckUtil {
        // 接口配置信息中的Token
        private static final String token = "zhongkejuwang123456";
        public static boolean checkSignature(String signature, String timestamp, String nonce) {
            String[] str = new String[]{token, timestamp, nonce};
            //排序
            Arrays.sort(str);
            //拼接字符串
            StringBuffer buffer = new StringBuffer();
            for (int i = 0; i < str.length; i++) {
                buffer.append(str[i]);
            }
            //进行sha1加密
            String temp = SHA1.encode(buffer.toString());
            //与微信提供的signature进行匹对
            return signature.equals(temp);
        }
    }