package com.jnetdata.msp.util;

//import org.junit.Test;
import org.springframework.util.Base64Utils;
import org.springframework.util.StringUtils;
import sun.misc.BASE64Decoder;

import java.io.*;

/**
 * 图片 与 Base64 互相转换
 * zian Y
 */
public class Base64ImgUtil {

	/**
	 * 本地图片转换成base64字符串
	 * @param imgFile	本地图片全路径 （注意：带文件名）
	 *  (将图片文件转化为字节数组字符串，并对其进行Base64编码处理)
	 * @return
	 */
	public static String ImageToBase64ByLocal(String imgFile) {
		byte[] data = null;

		// 读取图片字节数组
		try {
			InputStream in = new FileInputStream(imgFile);

			data = new byte[in.available()];
			in.read(data);
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		// 返回Base64编码过的字节数组字符串
		return Base64Utils.encodeToString(data);
	}

	public static String ImageToBase64ByInputStream(InputStream in) {
		byte[] data = null;

		// 读取图片字节数组
		try {
			data = new byte[in.available()];
			in.read(data);
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		// 返回Base64编码过的字节数组字符串
		return Base64Utils.encodeToString(data);
	}


	/**
	 * base64字符串转换成图片 (对字节数组字符串进行Base64解码并生成图片)
	 * @param imgStr		base64字符串
	 * @param imgFilePath	指定图片存放路径  （注意：带文件名）
	 * @return
	 */
	public static boolean Base64ToImage(String imgStr,String imgFilePath) {

		if (imgStr == null) // 图像数据为空
			return false;
		BASE64Decoder decoder = new BASE64Decoder();
		try {
			// Base64解码
			byte[] bytes = decoder.decodeBuffer(imgStr);
			for (int i = 0; i < bytes.length; ++i) {
				if (bytes[i] < 0) {// 调整异常数据
					bytes[i] += 256;
				}
			}
			// 生成jpeg图片
			OutputStream out = new FileOutputStream(imgFilePath);
			out.write(bytes);
			out.flush();
			out.close();
			return true;
		} catch (Exception e) {
			return false;
		}

	}
}