package com.jnetdata.msp.manage.publish.core.common.utils;

/**
 * @author penghe.li
 * @version 1.0
 * @description: TODO
 * @date 2022/8/28 11:02
 */
public class NumberUtil {

    public static boolean isNumeber(String str) {
        for (int i = str.length(); --i >= 0; ) {
            int chr = str.charAt(i);
            if (chr < 48 || chr > 57)
                return false;
        }
        return true;
    }

    public static boolean isId(String str) {
        char chr = str.charAt(0);
        return chr >= 48 && chr <= 57;
    }
}
