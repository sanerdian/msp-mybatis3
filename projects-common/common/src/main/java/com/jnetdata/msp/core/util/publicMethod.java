package com.jnetdata.msp.core.util;

import eu.medsea.mimeutil.MimeUtil;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import sun.util.calendar.LocalGregorianCalendar;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Date;

/**
 * Created by 19912 on 2020/4/8.
 */
public class publicMethod {
    //判断图片可以使用，判断excel不准确
    public static String mineType(MultipartFile f) {

        String mineType="";

        MimeUtil.registerMimeDetector("eu.medsea.mimeutil.detector.MagicMimeMimeDetector");
        try {
            mineType=MimeUtil.getMimeTypes(f.getBytes()).toString();
        } catch (IOException e) {
            e.printStackTrace();
            mineType="";
        }

        return mineType;
    }

    /**
     * 如果a为空返回b
     * @param a
     */
    public static Integer ValueIsNull(Integer a,int b){
        if(a==null){
            return b;
        }
        return a;
    }

    /**
     * 如果a为空返回b
     * @param a
     */
    public static Long ValueIsNull(Long a,long b){
        if(a==null){
            return b;
        }
        return a;
    }


    /**
     * 如果a为空返回b
     * @param a
     */
    public static String ValueIsNull(String a,String b){
        if(StringUtils.isEmpty(a)){
            return b;
        }
        return a;
    }

    /**
     * 如果a为空返回b
     * @param a
     */
    public static Double ValueIsNull(Double a,double b){
        if(a==null){
            return b;
        }
        return a;
    }

    /**
     * 如果a为空返回b
     * @param a
     */
    public static Date ValueIsNull(Date a, Date b){
        if(a==null){
            return b;
        }
        return a;
    }


    /**
     * String转换Long
     *
     * @return Long
     * @author wangxuetao
     */
    public static long longFormat(String str) {
        long lg = 0;
        if (str != null && str.length() > 0) {
            lg = Long.parseLong(str.toString());
        }
        return lg;
    }

    /**
     * Object转换Long
     *
     * @param defaultVal 默认值
     * @return Long
     * @author lxy
     */
    public static long longFormat(Object object, long defaultVal) {
        long lg = defaultVal;
        if (object != null && object.toString().length() > 0) {
            lg = Long.parseLong(object.toString());
        }
        return lg;
    }

    /**
     * String转换Integer
     *
     * @return Long
     * @author wangxuetao
     */
    public static Integer intFormat(String str) {
        int i = 0;
        if (str != null && str.length() > 0) {
            i = Integer.parseInt(str.toString());
        }
        return i;
    }

    /**
     * Object转换Integer
     *
     * @param defaultVal 默认值
     * @return Integer
     * @author lxy
     */
    public static Integer intFormat(Object object, int defaultVal) {
        int i = defaultVal;
        if (object != null && object.toString().length() > 0) {
            i = Integer.parseInt(object.toString());
        }
        return i;
    }

    /**
     * Object转换String
     *
     * @return String
     * @author wangxuetao
     */
    public static String stringFormat(Object obj) {
        String str = "";
        if (obj != null) {
            str = obj.toString();
        }
        return str;
    }


}
