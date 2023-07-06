package com.jnetdata.msp.constants;

/**
 * @author Administrator
 */
public class WebPathConstant {

    public static final String ROOT_HTML = "/simple";


    public static String getHtmlFilePath(String path) {
        return ROOT_HTML + path;
    }

}
