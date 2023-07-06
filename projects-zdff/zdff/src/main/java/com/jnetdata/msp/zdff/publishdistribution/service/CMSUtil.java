package com.jnetdata.msp.zdff.publishdistribution.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.*;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

//import com.zkjw.core.modules.channel.service.ChannelService;

public  class CMSUtil {
	
	
	/**
	 * 获取项目名，默认jnetcms
	 * @author wangxuetao
	 */
	public static String getProjectName(){
		String projectName = "jnetcms";
		return projectName;
	}
	
	/**
	 * 转换日期类型
	 * @author wangxuetao
	 * @param obj
	 * @return Date
	 * @throws ParseException
	 */
	public static Date TimeFormatToDate(Object obj) throws ParseException {
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//小写的mm表示的是分钟
		String str = "1970-01-01 00:00:00";
		if(obj !=null){
			str = obj.toString();
		}
		return sdf.parse(str);
	}
	/**
	 * 转换日期类型
	 * @author wangxuetao
	 * @param obj
	 * @return String
	 */
	public static String TimeFormatToString(Object obj){
		String str = "1970-01-01 00:00:00";
		if(obj !=null){
			str = obj.toString().substring(0, 19);
		}
		return str;
	}
	/**
	 * 转换日期类型
	 * @author wangruiheng
	 * @param obj
	 * @return String
	 */
	public static String TimeFormatToString2(Object obj){
		String str = "1970-01-01 00:00";
		if(obj !=null){
			str = obj.toString().substring(0, 16);
		}
		return str;
	}
	/**
	 * 字符串长日期转换 :年月日  时分秒
	 * @author wangxuetao
	 * @param obj
	 * @return String
	 */
	public static Date DateTimeFormat(Object str){
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//小写的mm表示的是分钟
		if (str == null) {
			return null;
		}
		try {
			return sdf.parse(str.toString());
		} catch (ParseException e) {
			return null;
		}
	}
	/**
	 * 字符串转换日期类型
	 * @author wangxuetao
	 * @param str
	 * @return Date
	 * @throws ParseException
	 */
	public static Date DayFormat(String str) throws ParseException {
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");//小写的mm表示的是分钟
		return sdf.parse(str);
	}
	/**
	 * 可自定义转换格式的字符串转换日期类型
	 * @author lipenghe
	 * @param String DateStr
	 * @param String format
	 * @return Date
	 * @throws ParseException
	 */
	public static Date DateTimeFormat(String str, String format) throws ParseException {
		SimpleDateFormat sdf=new SimpleDateFormat(format);//小写的mm表示的是分钟
		if(str!=null&&str.length()>0){
			return sdf.parse(str);
		}else{
			return null;
		}
	}
	
	/**
	 * 可自定义转换格式的字符串转换日期类型
	 * @author lipenghe
	 * @param String format
	 * @return String str
	 */
	public static String DateTimeStr(String format){
		SimpleDateFormat sdf=new SimpleDateFormat(format);//小写的mm表示的是分钟
		return sdf.format(new Date());
	}
	
	/**
	 * 转换java.util.Date为java.sql.Date
	 * @author lipenghe
	 * @param java.util.Date
	 * @return java.sql.Date
	 */
	public static java.sql.Timestamp DateChange(Date date){
		//java.sql.Date sqldate = new java.sql.Date(ud.getTime());
		//return sqldate;
		//不用上面的因为java.sql.Date只有年月日没有时分秒
		return new java.sql.Timestamp(date.getTime());
	}
	
	/**
	 * 可自定义转换格式的字符串转换日期类型
	 * @author lipenghe
	 * @param Date date
	 * @param String format
	 * @return String
	 */
	public static String DateStrFormat(Date date, String format){
		SimpleDateFormat sdf=new SimpleDateFormat(format);//小写的mm表示的是分钟
		if(date!=null){
			return sdf.format(date);
		}else{
			return "";
		}
	}
	
	/**
	 * 获取当前系统时间 yyyy-MM-dd HH:mm:ss格式
	 * @author wangxuetao
	 * @return String
	 */
	public static String getSystemTime(){
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//小写的mm表示的是分钟
		return sdf.format(new Date());
		
	}
	/**
	 * 获取当前系统时间 yyyy-MM-dd HH:mm格式
	 * @author wangruiheng
	 * @return String
	 */
	public static String getSystemTime2(){
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm");//小写的mm表示的是分钟
		return sdf.format(new Date());
		
	}
	/**
	 * 获取当前系统时间 yyyyMM格式
	 * @author li.penghe
	 * @return String
	 */
	public static String getTimeAccurateToMonth(){
		SimpleDateFormat sdf=new SimpleDateFormat("yyyyMM");//小写的mm表示的是分钟
		return sdf.format(new Date());
	}
	/**
	 * 获取时间的前一天
	 * @author wang.ruiheng
	 * @param str
	 * @return Date
	 * @throws ParseException
	 */
	public static String getNextDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);  
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        date = calendar.getTime();  
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(date);
    } 
	/**
	 * 获取当前时间
	 * @author wang.ruiheng
	 * @param str
	 * @return Date
	 * @throws ParseException
	 */
	public static String getDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);  
        calendar.add(Calendar.DAY_OF_MONTH, 0);
        date = calendar.getTime();  
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(date);
    } 
	/**
	 * 获取当前系统时间 yyyyMMdd格式
	 * @author li.penghe
	 * @return String
	 */
	public static String getTimeAccurateToDay(){
		SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMdd");//小写的mm表示的是分钟
		return sdf.format(new Date());
	}
	/**
	 * 获取当前系统时间 yyyyMMddHHmmssSSS格式
	 * @author li.penghe
	 * @return String
	 */
	public static String getTimeAccurateToMS(){
		SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMddHHmmssSSS");//小写的mm表示的是分钟
		return sdf.format(new Date());
	}
	/**
	 * 获取当前系统时间毫秒
	 * @author wangxuetao
	 * @return long
	 */
	public static long getStartTime(){
		return new Date().getTime();
	}
	/**
	 * 计算执行所需时间毫秒
	 * @author wangxuetao
	 * @return long
	 */
	public static long getExecTime(long startTime) {
		long endTime = new Date().getTime();
		return endTime-startTime;
	}
	/**
	 * 获取用户Ip
	 * @author wangxuetao
	 * @param request
	 * @return String
	 */
	public static String logUserIp(HttpServletRequest request){
		// 获取客户机IP
		String logUserIp = request.getHeader("x-forwarded-for");
		if (logUserIp == null || logUserIp.length() == 0
				|| logUserIp.equalsIgnoreCase("unknown")) {
			logUserIp = request.getHeader("Proxy-Client-IP");
		}
		if (logUserIp == null || logUserIp.length() == 0
				|| logUserIp.equalsIgnoreCase("unknown")) {
			logUserIp = request.getHeader("WL-Proxy-Client-IP");
		}
		if (logUserIp == null || logUserIp.length() == 0
				|| logUserIp.equalsIgnoreCase("unknown")) {
			logUserIp = request.getRemoteAddr();
		}
		if(logUserIp.equals("0:0:0:0:0:0:0:1")){
			logUserIp = "127.0.0.1";
		}
		return logUserIp;
	}
	
	/**
	 * 判断ip是否合法
	 * @author wangxuetao
	 * @param request
	 * @return String
	 */
	public static String checkIp(HttpServletRequest request){
		String result = "0";
		String userIp = logUserIp(request);
		String[] ipstrs = userIp.split("\\.");
		if((ipstrs[0] == "10") && ((ipstrs[1] == "30") || (ipstrs[1] == "240"))){
			result = "1";
		}
		return result;
	}
	/**
	 * 获取当前系统日期  yyyy-MM-dd 格式
	 * @author wangxuetao
	 * @return String
	 */
	public static String getSystemDay(){
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");//小写的mm表示的是分钟
		return sdf.format(new Date());
	}
	/**
	 * String转换Long
	 * @author wangxuetao
	 * @param Object
	 * @return Long
	 */
	public static long longFormat(String str){
		long lg = 0;
		if(str != null && str.length()>0){
			lg = Long.parseLong(str.toString());
		}
		return lg;
	}
	/**
	 * Object转换Long
	 * @author lxy
	 * @param Object
	 * @param defaultVal	默认值
	 * @return Long
	 */
	public static long longFormat(Object object, long defaultVal){
		long lg = defaultVal;
		if(object != null && object.toString().length() > 0){
			lg = Long.parseLong(object.toString());
		}
		return lg;
	}
	
	/**
	 * String转换Integer
	 * @author wangxuetao
	 * @param Object
	 * @return Long
	 */
	public static Integer intFormat(String str){
		int i = 0;
		if(str != null && str.length()>0){
			i = Integer.parseInt(str.toString());
		}
		return i;
	}
	/**
	 * Object转换Integer
	 * @author lxy
	 * @param Object
	 * @param defaultVal 默认值
	 * @return Integer
	 */
	public static Integer intFormat(Object object, int defaultVal){
		int i = defaultVal;
		if(object != null && object.toString().length() > 0){
			i = Integer.parseInt(object.toString());
		}
		return i;
	}
	/**
	 * Object转换String
	 * @author wangxuetao
	 * @param Object
	 * @return String
	 */
	public static String stringFormat(Object obj){
		String str = "";
		if(obj != null){
			str = obj.toString();
		}
		return str;
	}
	/**
	 * 带默认值的Object转换String,如为空则返回传入默认字符
	 * @author lipenghe
	 * @param Object
	 * @param String 
	 * @return String
	 */
	public static String stringFormat(Object obj, String str){
		if(obj != null){
			str = obj.toString();
		}
		return str;
	}
	
	/**
	 * 转换字符串,如为空则返回传入默认字符,不为空则排除给定字符
	 * @author lipenghe
	 * @param Object obj
	 * @param String defStr:默认的字符
	 * @param String exceptStr:排除的字符
	 * @return String
	 */
	public static String stringFormat(Object obj, String defStr, String exceptStr){
		String str="";
		if(obj == null){
			return defStr;
		}else{
			str=obj.toString();
			if(exceptStr!=null&&str.equals(exceptStr)){
				return defStr;
			}else{
				return str;
			}
		}
	}
	
	/**
	 * String转换小数
	 * @author lipenghe
	 * @param String str
	 * @return double dou
	 */
	public static double dobleFormat(String str){
		double dou=0.0;
		if(str != null&&str.length()>0){
			dou = Double.parseDouble(str);
		}
		return dou;
	}
	
	/**
	 * Object转换小数
	 * @author lipenghe
	 * @param Object obj
	 * @return double dou
	 */
	public static double dobleFormat(Object obj){
		double dou=0.0;
		if(obj != null&&obj.toString().length()>0){
			dou = Double.parseDouble(obj.toString());
		}
		return dou;
	}
	
	/**
	 * MD5加密
	 * @author wangxuetao
	 * @param str
	 * @return
	 * @throws Exception
	 */
//	public static String MD5Str(String str){
//		return MD5Utils.md5Hex(str);
//	}
	/**
	 * base16转正常码
	 * @author wangxuetao
	 * @param String
	 * @return String
	 */
//	public static String base16ToStr(String str) {
//		return StringUtil.base16ToStr(str);
		/*
		if(str == null){
			return StringUtil.EMPTY;
		}else{
			Base64 decoder=new Base64();
	        try {
				return new String(decoder.decodeBuffer(URLDecoder.decode(str,"UTF-8")),"UTF-8");
			} catch (IOException e) {
				e.printStackTrace();
			}
			return StringUtil.EMPTY;
		}
		*/
		/*
		if(str == null){
			str = "";
		}
        
        byte[] theByte = new byte[str.length() / 2];  
        
        for (int i = 0; i < str.length(); i += 2) {  
            theByte[i / 2 ] = Integer.decode("0X" + str.substring(i, i + 2)).byteValue();  
        }  
        
        return new String(theByte); 
        */
//    }
	/**
	 * 正常码转base16
	 * @author wangxuetao
	 * @param String
	 * @return String
	 */
//	public static String strToBase16(String str){
//		return StringUtil.strToBase16(str);
		/*
		if(str == null){
			return StringUtil.EMPTY;
		}else{
			Base64 encoder=new Base64();
	        try {
				return URLEncoder.encode(encoder.encode(str.getBytes("UTF-8")),"UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			return StringUtil.EMPTY;
		}
		*/
		/*
        byte[] bytes;  
        int tmp;  
        String tmpStr;  
        bytes = theStr.getBytes();  
        StringBuffer result = new StringBuffer(bytes.length * 2);  
        for (int i = 0; i < bytes.length; i++) {  
        	tmp = bytes[i];  
        	if (tmp < 0) {  
                tmp += 256;  
            }  
        	tmpStr = Integer.toHexString(tmp);  
        	if (tmpStr.length() == 1) {
        		result.append("0");
        	}
        	result.append(tmpStr);
        }  
        return result.toString();
        */
//    }

	/**
	 * 获取当前登录用户名
	 * @author wangxuetao
	 * @param request
	 * @return
	 */
	public static String getLoginUserName(HttpServletRequest request){
		String loginUserName = "system";
		if(request != null){
			if(request.getSession().getAttribute("loginUserName")!=null){
				loginUserName = request.getSession().getAttribute("loginUserName").toString();
			}
		}
		return loginUserName;
	}
	
	/**
	 * 获取当前登录用户Id
	 * @author wangxuetao
	 * @param request
	 * @return
	 */
	public static long getLoginUserId(HttpServletRequest request){
		long loginUserId = 201;
		if(request != null){
			if(request.getSession().getAttribute("loginUserId")!=null){
				loginUserId = CMSUtil.longFormat(request.getSession().getAttribute("loginUserId").toString());
			}
		}
		return loginUserId;
	}
	
	/**
	 * 对String[]进行解密
	 * @author lipenghe
	 * @param m :加密的String[]
	 * @return String[]：解密的String[]
	 */
//	public static String[] decryptStringArr(String[] strArr){
//		if(strArr!=null){
//			for(int i=0,len=strArr.length;i<len;i++){
//				strArr[i]=StringUtil.base16ToStr(strArr[i]);
//			}
//		}
//		return strArr;
//	}
	
	/**
	 * 对Map集合进行解密
	 * @author wangxuetao
	 * @param m :加密的Map
	 * @return Map：解密的Map
	 */
	public static Map decryptMap(Map m){
		Set<String> keSet = new HashSet<String>();
		Map newMap = new HashMap<String,Object>();
		if(!m.isEmpty()){
			keSet =	m.keySet();
			String key = null;//map集合对应的key;
			String[] value = null;//map集合对应的value;
			String s = null;
			for (Iterator<String> iterator = keSet.iterator(); iterator.hasNext();) {
				key = iterator.next();
				if(!key.equals("m")){
					value = (String[])m.get(key);
					for(int i=0;i<value.length;i++){
						s = value[i];
//						s =  CMSUtil.base16ToStr(s);
						s = s.trim();
						value[i] = s;
					}
					if(value.length == 1){
						newMap.put(key, s);
					}else{
						newMap.put(key, value);
					}
				}
	        }
		}
		return newMap;
	}
	
	/**
	 * 对Map集合进行解密
	 * @author wangxuetao
	 * @param m :加密的Map
	 * @return Map：解密的Map
	 */
	public static Map decryptMap(Map m, boolean bool){
		Set<String> keSet = new HashSet<String>();
		Map newMap = new HashMap<String,Object>();
		if(!m.isEmpty()){
			keSet =	m.keySet();
			String key = null;//map集合对应的key;
			String[] value = null;//map集合对应的value;
			String s = null;
			for (Iterator<String> iterator = keSet.iterator(); iterator.hasNext();) {
				key = iterator.next();
				if(!key.equals("m")){
					value = (String[])m.get(key);
					for(int i=0;i<value.length;i++){
						s = value[i];
//						s =  CMSUtil.base16ToStr(s);
						if(bool){
							s = s.trim();
						}
						value[i] = s;
					}
					if(value.length == 1){
						newMap.put(key, s);
					}else{
						newMap.put(key, value);
					}
				}
	        }
		}
		return newMap;
	}
    /**
	 * 获取"yyyyMM/yyyyMMdd/"的路径
	 * @author li.penghe
	 * @return String folderPath:路径
	 */
    public static String getShortFolderPath(){
    	StringBuffer sb=null;
    	sb=new StringBuffer();
    	sb.append("\\");
    	sb.append(CMSUtil.getTimeAccurateToMonth());
    	sb.append("\\");
    	sb.append(CMSUtil.getTimeAccurateToDay());
    	sb.append("\\");
    	String folderPath=sb.toString();
    	return  folderPath;
    }
    /**
	 * 获取文件名
	 * @author li.penghe
	 * @param String extension:扩展名
	 * @return String fileName:文件名
	 */
    public static String getFileName(String extension){
    	Random random=new Random();
    	StringBuffer sb=null;
    	sb=new StringBuffer();
    	int length=extension.length();
    	if(length>3){
    		sb.append(extension.substring(0,3));
    	}else if(length==3){
    		sb.append(extension);
    	}else{
    		sb.append(extension);
    		for(int i=0;i<(3-length);i++){
    			sb.append("_");
    		}
    	}
    	sb.append("_");
    	sb.append(CMSUtil.getTimeAccurateToMS());
    	sb.append("_");
    	sb.append(random.nextInt(100000));
    	sb.append(".");
    	sb.append(extension);
    	String fileName=sb.toString();
    	return  fileName;
    }
    /**
	 * 获取短物理路径
	 * @author li.penghe
	 * @param  request
	 * @return String physicalPath:短物理路径
	 */
    public static String getPhysicalPath(HttpServletRequest request){
    	String physicalPath=request.getSession().getServletContext().getRealPath("/");
    	physicalPath=physicalPath.substring(0,physicalPath.indexOf("webapps")-1);
    	int index=physicalPath.lastIndexOf("\\");
    	if(index!=-1){
    		physicalPath=physicalPath.substring(0,index+1);
    	}
    	return physicalPath;
    }
    /**
	 * 获取全物理路径
	 * @author li.penghe
	 * @param  request
	 * @param  String folder:upload,zip,webpic等
	 * @param  String sitePath:site路径名
	 * @return String physicalPath:全物理路径
	 */
    public static String getFullPhysicalPath(HttpServletRequest request, String folder, String sitePath){
    	String physicalPath=request.getSession().getServletContext().getRealPath("/");
    	physicalPath=physicalPath.substring(0,physicalPath.indexOf("webapps")-1);
    	int index=physicalPath.lastIndexOf("\\");
    	if(index!=-1){
    		physicalPath=physicalPath.substring(0,index+1);
    	}
    	StringBuffer sb=null;
    	sb=new StringBuffer();
    	sb.append(physicalPath);
    	sb.append("\\JNETDATA\\");
    	sb.append(folder);
    	sb.append("\\");
    	sb.append(sitePath);
    	sb.append("\\");
    	sb.append(CMSUtil.getTimeAccurateToMonth());
    	sb.append("\\");
    	sb.append(CMSUtil.getTimeAccurateToDay());
    	sb.append("\\");
    	physicalPath=sb.toString();
    	CMSUtil.createFolders(physicalPath);
    	return physicalPath;
    }
    /**
	 * 创建文件夹
	 * @author li.penghe
	 * @param  String path
	 * @return boolean bool
	 */
    public static boolean createFolders(String path){
    	boolean bool=false;
    	File file=new File(path);
    	if(!file.exists()){
    		try {
				file.mkdirs();
				bool=true;
			} catch (Exception e) {
				System.out.println("异常:上传图片文件夹创建错误!");
			}
    	} else {
    		bool = true;
    	}
    	return bool;
    }
    /**
	 * 删除文件夹
	 * @author li.penghe
	 * @param  String path
	 * @return boolean bool
	 */
    public static void delteFolders(String path){
    	File file = new File(path);
    	if(file.exists()){                    //判断文件是否存在
    	    if(file.isFile()){                    //判断是否是文件
    	     file.delete();                       //delete()方法 你应该知道 是删除的意思;
    	    }else if(file.isDirectory()){              //否则如果它是一个目录
    	     File files[] = file.listFiles();               //声明目录下所有的文件 files[];
    	     for(int i=0;i<files.length;i++){            //遍历目录下所有的文件
    	    	 files[i].delete();             //把每个文件 用这个方法进行迭代
    	     } 
    	    } 
    	    file.delete(); 
    	   }else{ 
    	    System.out.println("所删除的文件不存在！"+'\n');
    	   } 
    }
    /**
	 * 创建文件
	 * @author li.penghe
	 * @param  String path
	 */  
    public static boolean createFile(File filePath){
     boolean bool=false;  
     try{  
    	 if(!filePath.exists()){  
    		 filePath.createNewFile();  
    		 bool=true;  
    	 }  
     }catch(Exception e){
    	 e.printStackTrace();  
     }  
     	return bool;  
    }   
    public static String getLogoFolder(){
    	String logoFolder="logo";
    	return  logoFolder;
    }
    public static int  getMaxLogoSize(){
    	int maxLogoSize=1024000;
    	return  maxLogoSize;
    }
    public static String getLogoEncoding(){
    	String logoEncoding="UTF-8";
    	return logoEncoding;
    }
    public static String killNull(String str){
    	if(str==null){
    		str="";
    	}
    	return str;
    }
    /**
	 * 解析rightValue的List
	 * @author lipenghe
	 * @param List<Map<String,Object>> rightList:权限值的集合
	 * @param String rightValue:默认权限值
	 * @return boolean status:是否有权限
	 */
	public static boolean hasRight(List<Map<String,Object>> rightList, String rightValue){
		boolean status=false;
		if(rightList!=null&&rightList.size()>0){
			for(int i=0;i<rightList.size();i++){
				String currentRight=CMSUtil.stringFormat(rightList.get(i).get("RIGHTVALUE"));
				if(currentRight.contains(rightValue)){
					status=true;
					return status;
				}
			}
		}
		return status;
	}
	
	/**
	 * 给List定义长度
	 * @author lipenghe
	 * @param List<Map<String,Object>> list
	 * @param int size
	 * @return String
	 */
	public static int getListSize(List<Map<String,Object>> list, int size) {
			if(list!=null&&list.size()>0){
				return list.size();
			}else{
				return size;
			}
	}
	/**
	 * 给Map定义长度
	 * @author lipenghe
	 * @param Map<String,Object> map
	 * @param int size
	 * @return String
	 */
	public static int getMapSize(Map<String,Object> map, int size) {
			if(map!=null&&map.size()>0){
				return map.size();
			}else{
				return size;
			}
	}
	
	/**
	 * 判定是否为空
	 * @author lxy
	 * @param str
	 * @return
	 */
	public static boolean isEmpty(Object str) {
		if (str == null) {
			return true;
		}else if (str.toString().length() <= 0) {
			return true;
		} else {
			return false;
		}
	}
	/**
	 * 判定不为空
	 * @author lxy
	 * @param str
	 * @return
	 */
	public static boolean isNotEmpty(Object str) {
		if (str == null) {
			return false;
		}else if (str.toString().length() <= 0) {
			return false;
		} else {
			return true;
		}
	}
	/**
	 * 将内容添加到文件中
	 * @param file
	 * @param context 文件内容
	 * @param append  是否追加
	 * @return
	 */
	public static boolean createFile(String path, String filename, String context, boolean append) {
		if (context == null) {
			return false;
		}
		if (!createFolders(path)) {
			return false;
		}
		FileOutputStream out = null;
		BufferedOutputStream buffer = null;
		try {
			out = new FileOutputStream(new File(path + filename), append);
			buffer = new BufferedOutputStream(out);
			byte[] b = context.getBytes();
			buffer.write(b);
			return true;
		} catch (Exception e) {
			return false;
		} finally {
			if (buffer != null) {
				try {
					buffer.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	 /**
	 * 读取Properties配置文件
	 * @author lipenghe
	 * @param HttpServletRequest request
	 * @param String path
	 * @return Properties props
	 */
	public static Properties getProperties(HttpServletRequest request, String path){
		Properties props = new Properties();
	    try {
	    	 String realpath = request.getSession().getServletContext().getRealPath("/");
			 FileInputStream in = new FileInputStream(realpath+path);
			 props.load(in);
		} catch (IOException e) {
      		e.printStackTrace();
		}
		return props;
	}
	 /**
	 * 获取当前项目的根地址
	 * @author lipenghe
	 * @param HttpServletRequest request
	 * @return String
	 */
	public static String getServerPath(HttpServletRequest request){
		StringBuffer sb=null;
		sb=new StringBuffer();
		sb.append(request.getScheme());
		sb.append("://");
		sb.append(request.getServerName());
		sb.append(":");
		sb.append(request.getServerPort());
		sb.append(request.getContextPath());
		sb.append("/");
		String path=sb.toString();
		return path;
	}
	
	 /**
	 * 获取当前项目的根地址
	 * @author lipenghe
	 * @param HttpServletRequest request
	 * @return String
	 */
	public static String getServerPath(HttpServletRequest request, String extraPath){
		StringBuffer sb=null;
		sb=new StringBuffer();
		sb.append(request.getScheme());
		sb.append("://");
		sb.append(request.getServerName());
		sb.append(":");
		sb.append(request.getServerPort());
		sb.append(request.getContextPath());
		sb.append("/");
		sb.append(extraPath);
		String path=sb.toString();
		return path;
	}
	
	 /**
	 * 获取当前项目的域名
	 * @author lipenghe
	 * @param HttpServletRequest request
	 * @return String
	 */
	public static String getServerHost(HttpServletRequest request){
		StringBuffer sb=null;
		sb=new StringBuffer();
		sb.append(request.getScheme());
		sb.append("://");
		sb.append(request.getServerName());
		sb.append(":");
		sb.append(request.getServerPort());
		sb.append("/");
		String path=sb.toString();
		return path;
	}
	
	 /**
	 * 获取当前项目的物理路径
	 * @author lipenghe
	 * @param HttpServletRequest request
	 * @param String path
	 * @return String
	 */
	public static String getServerRelPath(HttpServletRequest request, String path){
		path=request.getSession().getServletContext().getRealPath(path);
		return path;
	}
	
	/** 
	  * 读TXT文件内容 
	  * @author lipenghe
	  * @param fileName 
	  * @return 
	  */  
	public static String readTxtFile(File fileName){
	  String result=null;
	  FileReader fileReader=null;
	  BufferedReader bufferedReader=null;
	  try{  
		  fileReader=new FileReader(fileName);
		  bufferedReader=new BufferedReader(fileReader);
    	  String read=null;
    	  while((read=bufferedReader.readLine())!=null){  
    		  result=result+read+"\r\n";  
    	  }  
	  }catch(IOException e){
		  e.printStackTrace();  
	  }finally{  
		  if(bufferedReader!=null){  
			  try {
				bufferedReader.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}  
		  }  
		  if(fileReader!=null){  
			  try {
				fileReader.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}  
		  }  
	  }  
	  return result;  
    }  
	/** 
	  * 添加TXT文件内容 
	  * @author lipenghe
	  * @param fileName 
	  * @return 
	  */ 
	 public static void addTextFile(String str, String filePath){
		  String s = new String();
		  StringBuffer sb = new StringBuffer();
		  File f = new File(filePath);
		  if(!f.getParentFile().exists()) {
			  f.getParentFile().mkdirs();
		  }
		  try {
			  if(!f.exists()){
				  f.createNewFile();//不存在则创建
		      }
			  //BufferedReader input = new BufferedReader(new FileReader(f));
			  InputStreamReader input = new InputStreamReader(new FileInputStream(f),"UTF-8");
			  BufferedReader reader=new BufferedReader(input);
			  while((s = reader.readLine())!=null){
			      sb.append(s);
			      sb.append("\r\n");
			  }
			  input.close();
			  sb.append(str);
			  //BufferedWriter output = new BufferedWriter(new FileWriter(f));
			  OutputStreamWriter output = new OutputStreamWriter(new FileOutputStream(f),"UTF-8");
			  output.write(sb.toString());
			  output.close();
		  }catch (IOException e) {
		      e.printStackTrace();
		  }
	}  
	 
	 /** 
	  * 创建TXT文件内容 
	  * @author lipenghe
	  * @param fileName 
	  * @return 
	  */ 
	 public static void createTextFile(String str, String filePath){
		  File f = new File(filePath);
		  if(!f.getParentFile().exists()) {
			  f.getParentFile().mkdirs();
		  }
		  try {
			  if(f.exists()){
				  f.delete();//存在则删除
		      }
			  f.createNewFile();//创建文件
			  OutputStreamWriter output = new OutputStreamWriter(new FileOutputStream(f),"UTF-8");
			  //BufferedWriter output = new BufferedWriter(new FileWriter(f));
			  output.write(str);
			  output.close();
		  }catch (IOException e) {
		      e.printStackTrace();
		  }
	}  
	 
	 /** 
	  * 判断字符串长度并替换内容
	  * @author lipenghe
	  * @param String str
	  * @param int length
	  * @param String repStr
	  * @return 
	  */ 
	 public static String getByteLength(String str, int length, String repStr){
		    int valueLength = 0; 
		    String value="";
	        String chinese = "[\u4e00-\u9fa5]";
	        // 获取字段值的长度，如果含中文字符，则每个中文字符长度为2，否则为1  
	        for (int i = 0; i < str.length(); i++) {  
	            // 获取一个字符  
	            String temp = str.substring(i, i + 1);
	            // 判断是否为中文字符  
	            if (temp.matches(chinese)) {  
	                // 中文字符长度为1  
	                valueLength += 2;  
	            } else {  
	                // 其他字符长度为0.5  
	                valueLength += 1;  
	            }  
	            if(valueLength>length){
	            	value=str.substring(0,i)+repStr;
	            	return value;
	            }
	        }  
	        return  str;
	 }  
	 
	 /** 
	  * 字符串替换双引号
	  * @author lipenghe
	  * @param String str
	  * @return String
	  */ 
	 public static String getTransferStr(String str){
		StringBuffer sb=null;
		sb=new StringBuffer();
		if(str!=null&&str.length()>0){
			char[] charArr=str.toCharArray();
			for(int i=0;i<charArr.length;i++){
				if('\"'!=charArr[i]){
					sb.append(charArr[i]);
				}else {
					sb.append("&quot;");
				}
			}
			return sb.toString();
		}
			return "";
	 }  
	 
	 /** 
	  * 字符串subString
	  * @author lipenghe
	  * @param String str
	  * @param int startIndex
	  * @param int endIndex
	  * @return String
	  */ 
	 public static String subString(String str, int startIndex, int endIndex){
		int length=endIndex+1;
		if(str!=null&&(str.length()>=length)){
			str=str.substring(startIndex,endIndex);
		}
		return str;
	 }  
	 /** 
	  * 字符串缩写
	  * @author wangxuetao
	  * @param String str 原字符串
	  * @param num 保留长度
	  * @return String 新字符串
	  */ 
	 public static String shortString(String str, int num){
		String suffix = "...";
		if(str.length()>num){
			str=str.substring(0,num)+suffix;
		}
		return str;
	 }  
	 
	 /** 
	  * Object转字符串subString
	  * @author lipenghe
	  * @param Object str
	  * @param int startIndex
	  * @param int endIndex
	  * @return String
	  */ 
	 public static String subString(Object obj, int startIndex, int endIndex){
		String str="";
		if(obj!=null){
			str=obj.toString();
			if(str.length()>=(endIndex+1)){
				str=str.substring(startIndex,endIndex);
			}
		}
		return str;
	 }  
	 
	 /** 
	  * 获取栏目全路径(根据当前栏目id,查询所有父级栏目路径)
	  * @author lipenghe
	  * @param long chnlId
	  * @param String separator
	  * @return String
	 **/ 
	/* public static String getChnlPath(long chnlId) {
		ChannelService chnlService=null;
		chnlService=new ChannelService();
		Map<String,Object> map=null;
		String separator=File.separator;
		String path=separator;
		while(chnlId!=0){
			map=chnlService.getChnlInfo(chnlId);
			if(map!=null&&map.size()>0){
				String currPath=CMSUtil.stringFormat(map.get("CHNLDATAPATH"));
				chnlId=CMSUtil.longFormat(map.get("PARENTID"),0);
				path=separator+currPath+path;
			}
		}
		return path;
	 }  */
	 
	 /** 
	  * 日期加月
	  * @author lipenghe
	  * @param Object str
	  * @param int startIndex
	  * @param int endIndex
	  * @return String
	  */ 
	 public static Date addMonth(Date date, int num){
		  //SimpleDateFormat sdf = new SimpleDateFormat(format);//格式化对象
		  Calendar calendar = Calendar.getInstance();//日历对象
		  calendar.setTime(date);//设置当前日期
		  //calendar.set(calendar.YEAR, calendar.MONTH, calendar.DATE);
		  calendar.add(Calendar.MONTH, num);//月
		  date=calendar.getTime();//输出格式化的日期
		  return date;
	 }  
	 
	 /** 
	  * 日期加年
	  * @author lipenghe
	  * @param Object str
	  * @param int startIndex
	  * @return String
	  */ 
	 public static Date addYear(Date date, int num){
		  //SimpleDateFormat sdf = new SimpleDateFormat(format);//格式化对象
		  Calendar calendar = Calendar.getInstance();//日历对象
		  calendar.setTime(date);//设置当前日期
		  //calendar.set(calendar.YEAR, calendar.MONTH, calendar.DATE);
		  calendar.add(Calendar.YEAR, num);//年
		  date=calendar.getTime();//输出格式化的日期
		  return date;
	 }  
	 
	 /** 
	  * 日期加日
	  * @author lipenghe
	  * @param Object str
	  * @param int startIndex
	  * @return String
	  */ 
	 public static Date addDay(Date date, int num){
		  //SimpleDateFormat sdf = new SimpleDateFormat(format);//格式化对象
		  Calendar calendar = Calendar.getInstance();//日历对象
		  calendar.setTime(date);//设置当前日期
		  //calendar.set(calendar.YEAR, calendar.MONTH, calendar.DATE);
		  calendar.add(Calendar.DATE, num);//日
		  date=calendar.getTime();//输出格式化的日期
		  return date;
	 }  
	 
	 /** 
	  * 日期加年/月/日
	  * @author lipenghe
	  * @param Object str
	  * @param int startIndex
	  * @return String
	  */ 
	 public static Date addTime(Date date, int year, int month , int day){
		  //SimpleDateFormat sdf = new SimpleDateFormat(format);//格式化对象
		  Calendar calendar = Calendar.getInstance();//日历对象
		  calendar.setTime(date);//设置当前日期
		  //calendar.set(calendar.YEAR, calendar.MONTH, calendar.DATE);
		  calendar.add(Calendar.YEAR, year);//年
		  calendar.add(Calendar.MONTH, month);//月
		  calendar.add(Calendar.DATE, day);//日
		  date=calendar.getTime();//输出格式化的日期
		  return date;
	 } 
	 
	 /** 
	  * 每月第一天
	  * @author lipenghe
	  * @param Object str
	  * @param int startIndex
	  * @return String
	  */ 
	 public static Date getMonthFirstDay(){
		  Date date = new Date();
		  Calendar calendar = Calendar.getInstance();//日历对象
		  calendar.setTime(date);//设置当前日期
		  calendar.set(calendar.YEAR, calendar.MONTH, calendar.DATE);
		  calendar.set(Calendar.DAY_OF_MONTH, 1);//日
		  calendar.set(Calendar.HOUR_OF_DAY, 0);//日
		  calendar.set(Calendar.MINUTE, 0);//日
		  calendar.set(Calendar.SECOND, 0);//日
		  date=calendar.getTime();//输出格式化的日期
		  return date;
	 }  
	 
	 /** 
	  * 每月第一天
	  * @author lipenghe
	  * @param Object str
	  * @param int startIndex
	  * @return String
	  */ 
	 public static String getMonthFirstDayStr(){
		  Date date = new Date();
		  SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM");
		  String dateStr=sdf.format(date);
		  dateStr=dateStr+"-01";
		  return dateStr;
	 } 
	 
	 /** 
	  * 当前日期截止到日
	  * @author lipenghe
	  * @return Date
	 * @throws ParseException
	  */ 
	 public static Date getDateShort() throws ParseException {
		 SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		 Date date = new Date();
		 String dateStr = sdf.format(date);
		 date=sdf.parse(dateStr);
		 return date;
	 }
	 /** 
	  * 判断对象类型
	  * @author wangxuetao
	  * @return int
	  */
	 public static int getTypeNum(Object obj){
		 int r = 0;
		 if(obj instanceof String){
			 r = 1;
		 }else if(obj instanceof Integer){
			 r = 2;
		 }else if(obj instanceof Long){
			 r = 3;
		 }else if(obj instanceof Double){
			 r = 4;
		 }else if(obj instanceof Date){
			 r = 5;
		 }
		 return r;
	 }
	 
	 /** 
	  * 获取当前栏目id及子栏目id集合
	  * @author wangxuetao
	  * @return List
	  **/ 
	/*public static List<Long> getAllChannelById(long channelId){
		List<Long> list = new ArrayList<Long>();
		list.add(channelId);
		List<Long> ChannelIds = new ArrayList<Long>();
		ChannelIds = CMSUtil.getChannelIdsById(channelId,ChannelIds);
		for(int i=0;i<ChannelIds.size();i++){
			list.add(ChannelIds.get(i));
		}
		return list; 
	}*/
	/** 
	 * 获取当前栏目id及子栏目id集合
	 * @author wangxuetao
	 * @return List
	 **/ 
	/*public static List<Long> getAllChannelById1(long channelId){
		List<Long> list = new ArrayList<Long>();
		list.add(channelId);
		List<Long> ChannelIds = new ArrayList<Long>();
		ChannelIds = CMSUtil.getChannelIdsById1(channelId,ChannelIds);
		for(int i=0;i<ChannelIds.size();i++){
			list.add(ChannelIds.get(i));
		}
		return list; 
	}*/
	/** 
	  * 判断当前栏目是否有子栏目
	  * @author wangxuetao
	  * @return boolean
	  */ 
	/*public static boolean ischild(long channelId){
		boolean r = false;
		List<Map<String,Object>>  chnlList = new ChannelService().childChnlTree(channelId);//判断当前栏目是否子栏目
		if(chnlList.size()>0){
			r = true;
		}
		return r;
	}*/
	/** 
	  * 获得当前栏目子栏目id集合
	  * @author wangxuetao
	  * @return boolean
	  **/ 
	/*public static List<Long> getChannelIdsById(long channelId,List<Long> ChannelIds){
		long tempChannelid = 0;
		List<Map<String,Object>>  chnlList = new ChannelService().childChnlTree(channelId);
		for(int i=0;i<chnlList.size();i++){
			Map<String,Object> m = chnlList.get(i);
			tempChannelid = CMSUtil.longFormat(m.get("channelid"), 0);
			ChannelIds.add(tempChannelid);
			if(CMSUtil.ischild(tempChannelid)){
				ChannelIds = CMSUtil.getChannelIdsById(tempChannelid,ChannelIds);
			}
		}
		return ChannelIds;
	}*/
	/** 
	 * 获得当前栏目子栏目id集合
	 * @author wangxuetao
	 * @return boolean
	 **/ 
	/*public static List<Long> getChannelIdsById1(long channelId,List<Long> ChannelIds){
		long tempChannelid = 0;
		List<Map<String,Object>>  chnlList = new ChannelService().childChnlTree1(channelId);
		for(int i=0;i<chnlList.size();i++){
			Map<String,Object> m = chnlList.get(i);
			tempChannelid = CMSUtil.longFormat(m.get("channelid"), 0);
			ChannelIds.add(tempChannelid);
			if(CMSUtil.ischild(tempChannelid)){
				ChannelIds = CMSUtil.getChannelIdsById(tempChannelid,ChannelIds);
			}
		}
		return ChannelIds;
	}*/
	
	/**
	 * 当前session中是否存在已登录的用户
	 * @author wangxuetao
	 */
	public  boolean checkSession(HttpServletRequest request, String userName){
	    boolean issession = true;
	    Hashtable hashtable= (Hashtable)request.getSession().getServletContext().getAttribute("idlist");
	    HashMap hashMap=(HashMap)request.getSession().getServletContext().getAttribute("sessionid");
	    synchronized(this){
	    	Object obj = hashtable.get(userName);
	        if(obj!=null){
	        	//System.out.println("-----------21--------");
	        	//这里使用Hashtable因为 Hashtable本身是synchronized 所以为了方便就使用Hashtable
	        	//如果自己编写一个类实现了synchronized 并且只是放入String[只是验证登录名称] 效果会更好
	        	//如果不是null就已经可以判断出来登录了，如果想进一步判断登录信息，这里做处理
	        	//.............
	        	issession=false;
	        }else{
	        	//System.out.println("-----------22--------");
	        	//放入上下文
	            //SessionModel sessionModel=new SessionModel();
	            //sessionModel.setPhone("12345678");
	        	//UserInfo userInfo = new UserInfo();
	        	//System.out.println("----放入---userName:"+userName+"   sessionId:"+request.getSession().getId());
	            hashtable.put(userName,request.getSession().getId());
	            hashMap.put(request.getSession().getId(),userName);
	        }
	    }
	    return issession;
	}
	
	/**
	 * 当前session中是否存在已登录的用户
	 * @author wangxuetao
	 */
	/*
	public  boolean removeSession(HttpServletRequest request,String userName){
	    Hashtable hashtable= (Hashtable)request.getSession().getServletContext().getAttribute("idlist");
	    HashMap hashMap=(HashMap)request.getSession().getServletContext().getAttribute("sessionid");
	    synchronized(this){
	    	return 
	    }
	}
	*/
	
	public static String getSessionId(HttpServletRequest request, String userName){
		Hashtable hashtable= (Hashtable)request.getSession().getServletContext().getAttribute("idlist");
		String sessionId = "";
		sessionId = CMSUtil.stringFormat(hashtable.get(userName));
		return sessionId;
	}
	
	 /** 
	  * 获取随机数
	  * @author lipenghe
	  * @return String
	  */ 
	 public static String getRandom(){
		 Random random=new Random();
		 String randomStr= System.currentTimeMillis()+"_"+ random.nextInt(999999);
		 return randomStr;
	} 
	
	 /** 
	  * 校验参数
	  * @author lipenghe
	  * @param String str
	  * @return boolean
	  */ 
	 public static boolean checkParam(String str){
		 if(str!=null&&str.trim().length()>0){
			 return true;
		 }else{
			 return false;
		 }
	} 
	
	 /** 
	  * 校验参数
	  * @author lipenghe
	  * @param String[] strArr
	  * @return boolean
	  */ 
	 public static boolean checkParams(String[] strArr) throws Exception {
		 if(strArr!=null&&strArr.length>0){
			 for(int i=0;i<strArr.length;i++){
				 if(!CMSUtil.checkParam(strArr[i])){
					 return false; 
				 }
			 }
			 return true;
		 }else{
			 return false;
		 }
	}



	/**
	 * 获取logo文件大小
	 * @author yangxiaojie
	 * @param fileName
	 * @return
	 */
	 /*public static long getLogoSize(String key,String siteSign,String fileName){
		 String logoPath=ConfigUtil.getDownloadPath(key, siteSign, fileName)+fileName;
			File file=new File(logoPath);
			if(file.exists()){
				return file.length();
			}
			return 0;
	 }*/
	public static Map decryptMapLogin(Map m) throws UnsupportedEncodingException {
		Set<String> keSet = new HashSet<String>();
		Map newMap = new HashMap<String,Object>();
		if(!m.isEmpty()){
			keSet =	m.keySet();
			String key = null;//map集合对应的key;
			String[] value = null;//map集合对应的value;
			String s = null;
			for (Iterator<String> iterator = keSet.iterator(); iterator.hasNext();) {
				key = iterator.next();
				if(!key.equals("m")){
					value = (String[])m.get(key);
					for(int i=0;i<value.length;i++){
						s = value[i];
						s =  java.net.URLDecoder.decode(s,"UTF-8");
//						s =  CMSUtil.base16ToStr(s);
						s = s.trim();
						value[i] = s;
					}
					if(value.length == 1){
						newMap.put(key, s);
					}else{
						newMap.put(key, value);
					}
				}
			}
		}
		return newMap;
	}


	public static String doGet(String url, String access_token) {
		CloseableHttpClient httpClient = null;
		CloseableHttpResponse response = null;
		String result = "";
		try {
			// 通过址默认配置创建一个httpClient实例
			httpClient = HttpClients.createDefault();
			// 创建httpGet远程连接实例
			HttpGet httpGet = new HttpGet(url);
			// 设置请求头信息，鉴权
			httpGet.setHeader("Authorization", "Bearer " + access_token);
			// 设置配置请求参数
			RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(35000)// 连接主机服务超时时间
					.setConnectionRequestTimeout(35000)// 请求超时时间
					.setSocketTimeout(60000)// 数据读取超时时间
					.build();
			// 为httpGet实例设置配置
			httpGet.setConfig(requestConfig);
			// 执行get请求得到返回对象
			response = httpClient.execute(httpGet);
			// 通过返回对象获取返回数据
			HttpEntity entity = response.getEntity();
			// 通过EntityUtils中的toString方法将结果转换为字符串
			result = EntityUtils.toString(entity);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			// 关闭资源
			if (null != response) {
				try {
					response.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (null != httpClient) {
				try {
					httpClient.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return result;
	}


	public static String doPut(String url, String access_token, String jsonStr) {
		CloseableHttpClient httpClient = HttpClients.createDefault();
		HttpPut httpPut = new HttpPut(url);
		RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(35000).setConnectionRequestTimeout(35000).setSocketTimeout(60000).build();
		httpPut.setConfig(requestConfig);
//		httpPut.setHeader("Content-type", "application/json");
//		httpPut.setHeader("DataEncoding", "UTF-8");
//		httpPut.setHeader("token", token);
		httpPut.setHeader("Authorization", "Bearer " + access_token);
		CloseableHttpResponse httpResponse = null;
		try {
			Map<String, String> prarms = new HashMap<>();
			prarms.put("newowner", jsonStr);
			String jsonPrarms = JSON.toJSONString(prarms);
			HttpEntity entityParam = new StringEntity(jsonPrarms, ContentType.create("application/json", "UTF-8"));
			httpPut.setEntity(entityParam);
			httpResponse = httpClient.execute(httpPut);
			HttpEntity entity = httpResponse.getEntity();
			String result = EntityUtils.toString(entity);
			return result;
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (httpResponse != null) {
				try {
					httpResponse.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (null != httpClient) {
				try {
					httpClient.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}

	//医生咨询退款 hostconfig
	public static String doPutst(String hostconfig, Map<String,Object> map1,String yy) {
		CloseableHttpClient httpClient = HttpClients.createDefault();
//		ClientConnectionManager connManager = new PoolingClientConnectionManager();
//		DefaultHttpClient httpClient = new DefaultHttpClient(connManager);
		ResourceBundle resource = ResourceBundle.getBundle("config");
		String wxtkurl= resource.getString("wxtkurl");
		System.out.println("退款接口地址："+wxtkurl);
		HttpPost httpPut = new HttpPost(wxtkurl);
		RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(35000).setConnectionRequestTimeout(35000).setSocketTimeout(60000).build();
		httpPut.setConfig(requestConfig);
//      httpPut.setHeader("Authorization", "Bearer " + access_token);
		CloseableHttpResponse httpResponse = null;
		try {
			Map<String, String> prarms = new HashMap<>();
			prarms.put("refund_fee", CMSUtil.stringFormat(CMSUtil.intFormat(CMSUtil.stringFormat(CMSUtil.dobleFormat(CMSUtil.stringFormat(map1.get("PAYAMOUNT")))*100).split("\\.")[0])));
			prarms.put("totalAmount", CMSUtil.stringFormat(CMSUtil.intFormat(CMSUtil.stringFormat(CMSUtil.dobleFormat(CMSUtil.stringFormat(map1.get("PAYAMOUNT")))*100).split("\\.")[0])));
			prarms.put("out_trade_no", CMSUtil.stringFormat(map1.get("ORDERNUM")));
			prarms.put("refund_desc",yy);
			prarms.put("isysd", "");
			prarms.put("paytype", "0");
			prarms.put("issd", "自动");
			String jsonPrarms = JSON.toJSONString(prarms);
			HttpEntity entityParam = new StringEntity(jsonPrarms, ContentType.create("application/json", "UTF-8"));
			httpPut.setEntity(entityParam);
			httpResponse = httpClient.execute(httpPut);
			HttpEntity entity = httpResponse.getEntity();
			String result = EntityUtils.toString(entity);
			System.out.println("自动退款返回："+result);
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (httpResponse != null) {
				try {
					httpResponse.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (null != httpClient) {
				try {
					httpClient.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}


	public static String doPost(String url, String access_token, String groupname, String desc, Boolean publics, String maxusers, Boolean allowinvites, Boolean members_only, String owner, String members, String custom) {
		CloseableHttpClient httpClient = HttpClients.createDefault();
		HttpPost httpPut = new HttpPost(url);
		RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(35000).setConnectionRequestTimeout(35000).setSocketTimeout(60000).build();
		httpPut.setConfig(requestConfig);
		httpPut.setHeader("Authorization", "Bearer " + access_token);
		CloseableHttpResponse httpResponse = null;
		try {
			Map<String, Object> prarms = new HashMap<>();
			prarms.put("groupname", groupname);
			prarms.put("desc", desc);
			prarms.put("public", publics);
			prarms.put("maxusers", maxusers);
			prarms.put("allowinvites", allowinvites);
			prarms.put("members_only", members_only);
			prarms.put("owner", owner);
//			prarms.put("members", members);
			prarms.put("custom", custom);
			String jsonPrarms = JSON.toJSONString(prarms);
			HttpEntity entityParam = new StringEntity(jsonPrarms, ContentType.create("application/json", "UTF-8"));
			httpPut.setEntity(entityParam);
			httpResponse = httpClient.execute(httpPut);
			HttpEntity entity = httpResponse.getEntity();
			String result = EntityUtils.toString(entity);
			return result;
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (httpResponse != null) {
				try {
					httpResponse.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (null != httpClient) {
				try {
					httpClient.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}

	//        {"target_type": "users","target": ["user2","user3"],"msg": {"type": "txt","msg": "testmessage"},"from": "user1"}'
	public static String doPost_xx(String url, String access_token,String gid, String msg) {
		CloseableHttpClient httpClient = HttpClients.createDefault();
		HttpPost httpPut = new HttpPost(url);
		RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(35000).setConnectionRequestTimeout(35000).setSocketTimeout(60000).build();
		httpPut.setConfig(requestConfig);
		httpPut.setHeader("Authorization", "Bearer " + access_token);
		CloseableHttpResponse httpResponse = null;
		try {
			JSONObject jsonObjet = new JSONObject();
			jsonObjet.put("type", "txt");
			jsonObjet.put("msg", msg);
			List<String> strings = new ArrayList<String>();
			strings.add(gid);
			Map<String, Object> prarms = new HashMap<>();
			prarms.put("target_type", "chatgroups");
			prarms.put("target", strings);
			prarms.put("msg", jsonObjet);
			String jsonPrarms = JSON.toJSONString(prarms);
			HttpEntity entityParam = new StringEntity(jsonPrarms, ContentType.create("application/json", "UTF-8"));
			httpPut.setEntity(entityParam);
			httpResponse = httpClient.execute(httpPut);
			HttpEntity entity = httpResponse.getEntity();
			String result = EntityUtils.toString(entity);
			return result;
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (httpResponse != null) {
				try {
					httpResponse.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (null != httpClient) {
				try {
					httpClient.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}


	public static String doPost_gx(String url, String access_token,String announcement) {
		CloseableHttpClient httpClient = HttpClients.createDefault();
		HttpPost httpPut = new HttpPost(url);
		RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(35000).setConnectionRequestTimeout(35000).setSocketTimeout(60000).build();
		httpPut.setConfig(requestConfig);
		httpPut.setHeader("Authorization", "Bearer " + access_token);
		CloseableHttpResponse httpResponse = null;
		try {
			Map<String, Object> prarms = new HashMap<>();
			prarms.put("announcement", announcement);
			String jsonPrarms = JSON.toJSONString(prarms);
			HttpEntity entityParam = new StringEntity(jsonPrarms, ContentType.create("application/json", "UTF-8"));
			httpPut.setEntity(entityParam);
			httpResponse = httpClient.execute(httpPut);
			HttpEntity entity = httpResponse.getEntity();
			String result = EntityUtils.toString(entity);
			return result;
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (httpResponse != null) {
				try {
					httpResponse.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (null != httpClient) {
				try {
					httpClient.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}


	public static String doPost_jy(String url, String access_token,Long mute_duration,String usernames) {
		CloseableHttpClient httpClient = HttpClients.createDefault();
		HttpPost httpPut = new HttpPost(url);
		RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(35000).setConnectionRequestTimeout(35000).setSocketTimeout(60000).build();
		httpPut.setConfig(requestConfig);
		httpPut.setHeader("Authorization", "Bearer " + access_token);
		CloseableHttpResponse httpResponse = null;
		try {
			List<String> strings = new ArrayList<String>();
			strings.add(usernames);
			Map<String, Object> prarms = new HashMap<>();
			prarms.put("mute_duration", mute_duration);
			prarms.put("usernames", strings);
			String jsonPrarms = JSON.toJSONString(prarms);
			HttpEntity entityParam = new StringEntity(jsonPrarms, ContentType.create("application/json", "UTF-8"));
			httpPut.setEntity(entityParam);
			httpResponse = httpClient.execute(httpPut);
			HttpEntity entity = httpResponse.getEntity();
			String result = EntityUtils.toString(entity);
			return result;
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (httpResponse != null) {
				try {
					httpResponse.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (null != httpClient) {
				try {
					httpClient.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}


	public static String gettokens() {
		String access_token = "";
		try {
			ResourceBundle resource = ResourceBundle.getBundle("config");
			String client_id_hx= resource.getString("client_id_hx");
			String client_secret_hx= resource.getString("client_secret_hx");
			String url_hxs= resource.getString("url_hxs");
			String basePath1 = url_hxs+"/token";//yycm#iyanmi
			CloseableHttpClient httpClient2 = HttpClients.createDefault();
			HttpPost httpPost2 = new HttpPost();
			httpPost2 = new HttpPost(basePath1);
			String str3 = "";
			Map<String, String> prarms3 = new HashMap<>();
			prarms3.put("grant_type", "client_credentials");
			prarms3.put("client_id", client_id_hx);
			prarms3.put("client_secret",client_secret_hx);
			String jsonPrarms3 = JSON.toJSONString(prarms3);
			HttpEntity entityParam3 = new StringEntity(jsonPrarms3, ContentType.create("application/json", "UTF-8"));
			httpPost2.setEntity(entityParam3);     //把参数添加到post请求
			HttpResponse response3 = httpClient2.execute(httpPost2);
			StatusLine statusLine3 = response3.getStatusLine();   //获取请求对象中的响应行对象
			int responseCode3 = statusLine3.getStatusCode();
			HttpEntity entity3 = response3.getEntity();
			InputStream input3 = entity3.getContent();
			BufferedReader br3 = new BufferedReader(new InputStreamReader(input3,"utf-8"));
			str3 = br3.readLine();
			System.out.println("服务器的响应是:" + str3);
			br3.close();
			input3.close();
			JSONObject jsonObject3 = JSONObject.parseObject(str3);
			access_token = CMSUtil.stringFormat(jsonObject3.get("access_token"));
			System.out.println(access_token);
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {

		}
		return access_token;
	}

	public static String doDelete(String url, String token) {
		CloseableHttpClient httpClient = HttpClients.createDefault();
		HttpDelete httpDelete = new HttpDelete(url);
		RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(35000).setConnectionRequestTimeout(35000).setSocketTimeout(60000).build();
		httpDelete.setConfig(requestConfig);
		httpDelete.setHeader("Content-type", "application/json");
		httpDelete.setHeader("DataEncoding", "UTF-8");
//        httpDelete.setHeader("token", token);
		httpDelete.setHeader("Authorization", "Bearer " + token);

		CloseableHttpResponse httpResponse = null;
		try {
			httpResponse = httpClient.execute(httpDelete);
			HttpEntity entity = httpResponse.getEntity();
			String result = EntityUtils.toString(entity);
			return result;
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (httpResponse != null) {
				try {
					httpResponse.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (null != httpClient) {
				try {
					httpClient.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}


	public static String doPost_hmd(String url, String access_token) {
		CloseableHttpClient httpClient = HttpClients.createDefault();
		HttpPost httpPut = new HttpPost(url);
		RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(35000).setConnectionRequestTimeout(35000).setSocketTimeout(60000).build();
		httpPut.setConfig(requestConfig);
		httpPut.setHeader("Authorization", "Bearer " + access_token);
		CloseableHttpResponse httpResponse = null;
		try {
			Map<String, Object> prarms = new HashMap<>();
			String jsonPrarms = JSON.toJSONString(prarms);
			HttpEntity entityParam = new StringEntity(jsonPrarms, ContentType.create("application/json", "UTF-8"));
			httpPut.setEntity(entityParam);
			httpResponse = httpClient.execute(httpPut);
			HttpEntity entity = httpResponse.getEntity();
			String result = EntityUtils.toString(entity);
			return result;
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (httpResponse != null) {
				try {
					httpResponse.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (null != httpClient) {
				try {
					httpClient.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}


	public static String doPost_gly(String url, String access_token,String newadmin) {
		CloseableHttpClient httpClient = HttpClients.createDefault();
		HttpPost httpPut = new HttpPost(url);
		RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(35000).setConnectionRequestTimeout(35000).setSocketTimeout(60000).build();
		httpPut.setConfig(requestConfig);
		httpPut.setHeader("Authorization", "Bearer " + access_token);
		CloseableHttpResponse httpResponse = null;
		try {
			Map<String, Object> prarms = new HashMap<>();
			prarms.put("newadmin", newadmin);
			String jsonPrarms = JSON.toJSONString(prarms);
			HttpEntity entityParam = new StringEntity(jsonPrarms, ContentType.create("application/json", "UTF-8"));
			httpPut.setEntity(entityParam);
			httpResponse = httpClient.execute(httpPut);
			HttpEntity entity = httpResponse.getEntity();
			String result = EntityUtils.toString(entity);
			return result;
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (httpResponse != null) {
				try {
					httpResponse.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (null != httpClient) {
				try {
					httpClient.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}

	public static String sendLabelToUser(String url, String json){
		String result = null;
		System.out.println("json:"+json);
		try {
			result = sendPost(url, json);
//			System.out.println("返回jsons="+result);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public static String sendPost(String url, String param) {
		PrintWriter out = null;
		BufferedReader in = null;
		String result = "";
		try {
			URL realUrl = new URL(url);
			// 打开和URL之间的连接
			URLConnection conn = realUrl.openConnection();
			//设置通用的请求属性
			conn.setRequestProperty("user-agent","Mozilla/5.0 (Windows NT 6.1; WOW64; rv:21.0) Gecko/20100101 Firefox/21.0)");
			// 发送POST请求必须设置如下两行
			conn.setDoOutput(true);
			conn.setDoInput(true);
			// 获取URLConnection对象对应的输出流
			OutputStreamWriter outWriter = new OutputStreamWriter(conn.getOutputStream(), "utf-8");
			out = new PrintWriter(outWriter);
			// 发送请求参数
			if(param!=null){
				out.print(param);
			}
			// flush输出流的缓冲
			out.flush();
			// 定义BufferedReader输入流来读取URL的响应
			in = new BufferedReader(new InputStreamReader(conn.getInputStream(),"utf-8"));
			String line;
			while ((line = in.readLine()) != null) {
				in = new BufferedReader(new InputStreamReader(conn.getInputStream()));result += line;
			}
		} catch (Exception e) {
			System.out.println("发送 POST 请求出现异常！"+e);
			e.printStackTrace();
		}
		//使用finally块来关闭输出流、输入流
		finally{
			try{
				if(out!=null){
					out.close();
				}
				if(in!=null){
					in.close();
				}
			}
			catch(IOException ex){
				ex.printStackTrace();
			}
		}
		return result;
	}

	/**
	 * 普通文本消息，需用户在48h与公共帐号有互动
	 * 微信公共账号发送给账号
	 * @param content 文本内容
	 * @param toUser(OPENID) 微信用户
	 * @return
	 */
	/*public static Map<String,Object> sendTextMessageToUser(String content, String toUser, String txt){
		ResourceBundle resource = ResourceBundle.getBundle("config");
		Map<String,Object> resMap=new HashMap<String,Object>();
		String json = "";
		if(txt.equals("text")){
			json = "{\"touser\": \""+toUser+"\",\"msgtype\": \""+txt+"\", \"text\": {\"content\": \""+content+"\"}}";
		}else {
			json = "{\"touser\": \""+toUser+"\",\"msgtype\": \""+txt+"\", \"image\": {\"media_id\": \""+content+"\"}}";
		}
		String appid=null;
		String appsecret=null;
		appid= resource.getString("appid");
		appsecret= resource.getString("secret");
		Map<String,Object> infos = Singleton.getInstance().getAccessTokenAndJsapiTicket(appid,appsecret);
		System.out.println("json:"+json);
		try {
			String result = null;
			//发送客服消息给指定用户
			String action = "https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token="+infos.get("access_token");
			try {
				result = sendPost(action, json);
				net.sf.json.JSONObject jsonResult = net.sf.json.JSONObject.fromObject(result);
				if (jsonResult != null) {
					int errorCode = jsonResult.getInt("errcode");
					String errorMessage = jsonResult.getString("errmsg");
					if (errorCode == 0) {
						resMap.put("status", Integer.valueOf("0"));
						resMap.put("msg", "获取成功");
					}else {
						resMap.put("status", Integer.valueOf("-1"));
						resMap.put("msg", "获取失败");
					}
				}else {
					resMap.put("status", Integer.valueOf("-1"));
					resMap.put("msg", "获取失败");
				}
				System.out.println("返回jsons="+result);
			} catch (Exception e) {
				e.printStackTrace();
			}
//			System.out.println(result);"errmsg":"empty post data hint: [eT4Y80411ge31]","errcode":44002
//			{"errcode":45015,"errmsg":"response out of time limit or subscription
//			is canceled hint: [YuFnWa00623935]"}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resMap;
	}*/


	public static Object keyFirstToLower(Object obj) throws Exception {
		if(obj != null){
			if(obj instanceof List){
				List<Map<String,Object>> list = (List<Map<String, Object>>) obj;
				List<Map<String,Object>> listRes = new ArrayList<Map<String,Object>>();
				if(list != null && list.size() > 0){
					for (Map<String, Object> map : list) {
						listRes.add(toLower(map));
					}
					return listRes;
				}
			}else if(obj instanceof Map){
				Map<String,Object> result = new HashMap<String,Object>();
				Map<String,Object> map = (Map<String, Object>) obj;
				if(map!= null && !map.isEmpty()){
					result = toLower(map);
					return result;
				}
			}
		}
		return null;
	}
	public static Map<String,Object> toLower(Map<String, Object> map) {
		Map<String,Object> result = new HashMap<String,Object>();
		try {
			if(map != null && !map.isEmpty()){
				Iterator<Map.Entry<String, Object>> iterator = map.entrySet().iterator();
				while(iterator.hasNext()) {
					Map.Entry<String, Object> entry = iterator.next();
					String key = entry.getKey();
					Object value = entry.getValue();
					String firstChar = String.valueOf(key);
					key = key.replaceFirst(firstChar, firstChar.toLowerCase());
					result.put(key, String.valueOf(entry.getValue()).replaceAll("null",""));
//					if(value instanceof List){
//						List<Map<String,Object>> list = (List<Map<String, Object>>) value;
//						List<Map<String,Object>> listRes = new ArrayList<Map<String,Object>>();
//						if(list != null && list.size() > 0){
//							for (Map<String, Object> map2 : list) {
//								Map<String,Object> mapres = (Map<String, Object>) keyFirstToLower(map2);
//								listRes.add(mapres);
//							}
//							result.put(key, listRes);
//						}
//					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	public static void main(String[] args) throws Exception {
		String ss1 = "合作";
		String ss = "合作";
		String ss2 = "";
		/*String[] sdf1 = ss1.split("#");
		if(sdf1.length<=2){
			System.out.println("444");
		}*/

		String[] sdf = ss.split("#");
		System.out.println(sdf.length);
		boolean f = false;
		if(sdf.length>=2){
			for(String s:sdf){
				if(!CMSUtil.stringFormat(s).equals("")){
					System.out.println("#"+s+"#");
					String hj = "#"+s+"#";
					ss2 = "#"+ss1+"#";
					System.out.println(ss2+"sssssss"+hj);
//					if(hj.contains(ss1)){
					if(hj.equals(ss2)){
						System.out.println("baohan");
						f = true;
					}
				}
			}
		}
		System.out.println(f);
//		System.out.println(CMSUtil.stringFormat(CMSUtil.intFormat(CMSUtil.stringFormat(CMSUtil.dobleFormat(CMSUtil.stringFormat("1"))*100).split("\\.")[0])));
//		String get_double = CMSUtil.stringFormat(Math.round(CMSUtil.dobleFormat("0.12999999999999998")*100)/100.0);
//		System.out.println(get_double);
//		String basePath4 = "http://a1.easemob.com/yycm/iyanmi/chatgroups";
//		String dft = CMSUtil.doPost(basePath4, gettokens(), CMSUtil.stringFormat(""),"圈子描述", false, "200", true, false, "UYEodP0V6uZgi3DzwhZM72998ztD4P8", "", "");
//		System.out.println(dft);
		//System.out.println(CMSUtil.strToBase16("jnetcms_xkz"));
		/*RightService rightService=new RightService();
		List<Map<String,Object>> lst=rightService.getSeniorRight(0, 1, 68);
		boolean bool=CMSUtil.hasRight(lst, "12");
		//System.out.println(bool);
		String str=CMSUtil.getByteLength("''''''''''''",10,"***");*/
		//String str=CMSUtil.getTransferStr("\"\"\"\"'''''''''''");
		//System.out.println(str);
		//Date date1=CMSUtil.addMonth( new Date(),1);
		//Date date2=CMSUtil.addYear( new Date(),1);
		//Date date3=CMSUtil.addDay( new Date(),1);
		//Date date4=CMSUtil.addTime(new Date(), 1, 1, 1);
		//SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		/*System.out.println(sdf.format(date1));
		//System.out.println(sdf.format(date2));
		//System.out.println(sdf.format(date3));
		//System.out.println(sdf.format(date4));
		String date=CMSUtil.getMonthFirstDayStr();
		//System.out.println(CMSUtil.getFileName("doc"));
		//System.out.println(date);*/
		//System.out.println(CMSUtil.subString("1899-12-30 16:00:00.0", 0, 19));
		//System.out.println("1899-12-30 16:00:00.0".substring(0, 19));
		/*List list = getAllChannelById(1103);
		for(Object o :list){
			//System.out.println(CMSUtil.longFormat(o, 0));
		}*/
		//System.out.println(String.format("%.2f", 12.234567f));
		//System.out.println(String.format("%.2f", 0.00));
		//System.out.println(CMSUtil.DateChange(new Date()));
		
//		String result = "0";
//		String userIp = "10.240.1.107;127.0.0.1";
//		String[] ipstrs = userIp.split("\\.");
//		if((ipstrs[0].equals("10")) && ((ipstrs[1].equals("30")) || (ipstrs[1].equals("240")))){
//			result = "1";
//		}
//		System.out.println(result);
	}
	
}
