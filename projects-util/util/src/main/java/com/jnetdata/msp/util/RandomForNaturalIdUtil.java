package com.jnetdata.msp.util;

import lombok.Data;

/**
 * 用户单号的随机数产生器
 * 主要目的：测试时可以确定产生的单号
 */
@Data
public class RandomForNaturalIdUtil {

    /**
     * 只有测试时候可以修改!!!!!
     */
    public static boolean test = false;

    public static double random() {
        if (!test) {
            return Math.random();
        }
        return 1.0;
    }

}
