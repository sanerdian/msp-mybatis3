package com.jnetdata.msp.constants;

import java.util.UUID;

/**
 * @author Administrator
 */
public class RedisKey {

    public static String getUUID() {
        String uuid = UUID.randomUUID().toString();
        return uuid.replaceAll("-", "");
    }

}
