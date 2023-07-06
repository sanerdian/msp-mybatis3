package com.jnetdata.msp.metadata.precise.entity;

public class ValueHelper {

    public static long longValue(Object object){
        return longValue(object,0L);
    }
    public static Long longValue(Object object,Long defaultValue){
        if(object!=null&&object instanceof Number){
            return ((Number) object).longValue();
        }else {
            return defaultValue;
        }
    }
}
