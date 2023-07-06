package com.jnetdata.msp.docs.document.util;

import lombok.SneakyThrows;
import org.springframework.util.Base64Utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author: ZKJW
 * @date: 2019/5/20
 */
public class PictureUtils {

    /**
     * base64加密图片
     * @param path 本地文件路径
     * @return base64加密后的图片
     */
    @SneakyThrows
    public static String base64ToString(String path) {
        byte[] data = null;
        try {
            InputStream inputStream  = new FileInputStream(path);
            data = new byte[inputStream.available()];
            inputStream.read(data);
            inputStream.close();
        } catch (IOException e) {
            throw e;
        }
        String base64Result= Base64Utils.encodeToString(data);
        String []strs=path.split("\\.");
        String imgType=strs[strs.length-1];
        String base64Head="data:image/"+imgType+";base64, ";
        String base64Img=base64Head+base64Result;
        return base64Img;
    }

    @SneakyThrows
    public static byte[] base64(String path) {
        byte[] data = null;
        try {
            InputStream inputStream  = new FileInputStream(path);
            data = new byte[inputStream.available()];
            inputStream.read(data);
            inputStream.close();
        } catch (IOException e) {
            throw e;
        }
        return Base64Utils.encode(data);
    }
}
