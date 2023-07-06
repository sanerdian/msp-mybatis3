package com.jnetdata.msp.util.aes;

import lombok.SneakyThrows;
import org.springframework.util.Base64Utils;
import org.springframework.util.StringUtils;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import java.security.Key;
import java.security.SecureRandom;

/**
 * @author: ZKJW
 * @date: 2019/8/24
 */
public class AesUtil {

    /**
     * 加密算法
     */
    private static String CIPHER_ALGORITHM = "AES";

    /**
     * 密钥
     */
    private static String KEY = "WHfRLTcgx7yXOgH/g";

    public static Key getKey(String strKey) {
        try {
            if (strKey == null) {
                strKey = "";
            }
            KeyGenerator generator = KeyGenerator.getInstance(CIPHER_ALGORITHM);
            SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
            secureRandom.setSeed(strKey.getBytes());
            generator.init(128, secureRandom);
            return generator.generateKey();
        } catch (Exception e) {
            throw new RuntimeException(" 密钥出现异常 ");
        }
    }


    /**
     * AES加密
     * @param data 原文
     * @return 密文
     */
    @SneakyThrows
    public static String encrypt(String data){
        if(StringUtils.isEmpty(data)){
            return data;
        }
        SecureRandom secureRandom = new SecureRandom();
        Key secureKey = getKey(KEY);
        Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, secureKey, secureRandom);
        byte[] bt = cipher.doFinal(data.getBytes("utf-8"));
        String string = Base64Utils.encodeToString(bt);
        //String strS = new BASE64Encoder().encode(bt);
        return string;
    }


    /**
     * AES解密
     * @param message 密文
     * @return 原文
     */
    @SneakyThrows
    public static String decrypt(String message){
        if(StringUtils.isEmpty(message)){
            return message;
        }
        SecureRandom secureRandom = new SecureRandom();
        Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
        Key secureKey = getKey(KEY);
        cipher.init(Cipher.DECRYPT_MODE, secureKey, secureRandom);
        byte[] bytes = Base64Utils.decodeFromString(message);

        //byte[] res = new BASE64Decoder().decodeBuffer(message);
        byte[] res = cipher.doFinal(bytes);
        return new String(res,"utf-8");
    }

    public static void main(String[] args) {
        String message = "北京市";
        System.out.println("原文信息："+message);
        System.out.println("原文长度："+message.length());

        String encryptMsg = encrypt(message);
        System.out.println("加密信息：");
        System.out.println(encryptMsg);
        System.out.println("密文长度："+encryptMsg.length());

        String decryptedMsg = decrypt(encryptMsg);
        System.out.println("解密信息："+decryptedMsg);
        System.out.println(message.equals(decryptedMsg));
    }


}
