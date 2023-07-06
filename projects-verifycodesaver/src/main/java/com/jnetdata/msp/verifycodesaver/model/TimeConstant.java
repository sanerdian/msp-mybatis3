package com.jnetdata.msp.verifycodesaver.model;

/**
 * 验证码相关时间常数
 * @author Administrator
 */
public class TimeConstant {

    /**
     * 一秒(=?毫秒)
     */
    public static final Long SECOND = 1000L;

    /**
     * 一分钟(=?毫秒)
     */
    public static final Long MINUTE = 60*SECOND;

    /**
     * 一小时(=?毫秒)
     */
    public static final Long HOUR = 60*MINUTE;

    /**
     * 12小时(=?毫秒)
     */
    public static final Long HALFDAY = 12* HOUR;

    /**
     * 1天(=?毫秒)
     */
    public static final Long DAY = 2*HALFDAY;

}
