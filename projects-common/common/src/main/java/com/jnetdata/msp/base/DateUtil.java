package com.jnetdata.msp.base;

import java.util.Date;

/**
 * Created by Administrator on 2018/11/24 0024.
 */
public class DateUtil {

    private static Date now = null;

    public static Date getNow() {
        return (null==now) ? new Date() :  now;
    }

    public static void setNow(Date date) {
        now = date;
    }

}
