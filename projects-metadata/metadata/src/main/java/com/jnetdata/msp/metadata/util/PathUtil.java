package com.jnetdata.msp.metadata.util;

import com.jnetdata.msp.util.generator.rules.NamingStrategy;
import org.springframework.util.ClassUtils;

import java.io.File;
import java.util.Arrays;
import java.util.List;

public class PathUtil {
    private static String jarVersion = "1.0.0";
    public static String getEntityPackage(String tableName){
        tableName = tableName.toLowerCase();
        if (tableName.startsWith("jmeta_")) {
            return tableName.replace("jmeta_", "");
        } else {
            return tableName.replace("view_", "").replace("_", "");
        }
    }

    public static String getEntityName(String tableName){
        return NamingStrategy.capitalFirst(processName(tableName));
    }

    public static String processName(String name) {
        if(name.indexOf("_") > 0){
            String prefix = name.split("_")[0] + "_";
            // 删除前缀、下划线转驼峰
            name = NamingStrategy.removePrefixAndCamel(name, new String[]{prefix});
        }
        if(name.endsWith(".keyword")) name = name.replace(".keyword","2keyword");
        return name;
    }

    public static String getLibPath(){
        String path = ClassUtils.getDefaultClassLoader().getResource("").getPath();
        path = path.substring(0,path.lastIndexOf("/classes")) + "/lib/";

        String os = System.getProperty("os.name");
        if(os.toLowerCase().startsWith("win")) { //windows
            path = path.replace("/","\\");
        }
        return path;
    }

    public static String getLibJarPath(String project,int i){
        String jarPath = "-"+jarVersion+".jar";
        String path = getLibPath();
        if(i == 1){
            return path + File.separator +"jnetdata-msp-"+project+jarPath;
        }else{
            return path + File.separator +"jnetdata-msp-"+project+"-api"+jarPath;
        }
    }

    public static String getUrl(String moduleName,String tableName){
        String[] arr = tableName.split("_");
        if(arr.length>1){
            String[] temp = new String[arr.length - 1];
            System.arraycopy(arr, 1, temp, 0, temp.length);
            arr = temp;
        }
        String join = String.join("", arr);
        return moduleName + "/" + join.toLowerCase();
    }

    public static void main(String[] args) {
        System.out.println(123);
        System.out.println(System.getProperty("user.dir"));
        System.out.println(Class.class.getResource("/").getPath());
    }
}
