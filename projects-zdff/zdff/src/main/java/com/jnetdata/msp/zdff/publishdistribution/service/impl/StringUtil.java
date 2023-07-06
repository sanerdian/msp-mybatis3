package com.jnetdata.msp.zdff.publishdistribution.service.impl;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.util.UUID;

/**
 * @author :  Amayadream
 * @date :  2017.01.11 19:03
 */
public class StringUtil {
	public static final String EMPTY="";
	public static String getGuid(){
		return UUID.randomUUID().toString().trim().replaceAll("-", "").toLowerCase();
	}
	/**
	 * 判断字符是否不为空,null,"","  "均为false
	 * @author lipenghe
	 * @param str
	 * @return boolean
	 */
	public static boolean isNotEmpty(String str){
		if(str!=null&&str.trim().length()>0){
			return true;
		}else{
			return false;
		}
	}
	/**
	 * 判断Object是否为空,null,"","  "均为true
	 * @author lipenghe
	 * @param obj
	 * @return boolean
	 */
	public static boolean isEmpty(Object obj){
		if(obj==null){
			return true;
		}else{
			if(obj.toString().trim().length()==0){
				return true;
			}else{
				return false;
			}
		}
	}

	/**
	 * 判断Object否不为空,null,"","  "均为false
	 * @author lipenghe
	 * @param obj
	 * @return boolean
	 */
	public static boolean isNotEmpty(Object obj){
		if(obj!=null&&obj.toString().trim().length()>0){
			return true;
		}else{
			return false;
		}
	}

	/**
	 * base16转正常码
	 * @author wangxuetao
	 * @param String
	 * @return String
	 * @throws null
	 */
	public static String base16ToStr(String str) {
//		if(str!=null){
//			try {
//				str= Base64Special.base64ToStr(str);
//			} catch (Exception e) {
////				e.printStackTrace();
////				str= LockUtil.base64ToStr(str);
//			}
//			return str;
//		}
		return StringUtil.EMPTY;
	}

	/**
	 * 正常码转base16
	 * @author wangxuetao
	 * @param String
	 * @return String
	 * @throws ParseException
	 */
	public static String strToBase16(String str) {
//		if(str!=null){
//			try {
//				return null;
////				return Base64Special.strToBase64(str);
//			} catch (UnsupportedEncodingException e) {
//				e.printStackTrace();
//			}
//		}
		return StringUtil.EMPTY;
	}
}
