package com.jnetdata.msp.util.reflect;

import java.lang.reflect.Field;

/**
 * @author Administrator
 */
public abstract class FieldUtils {

    static public<T> void setObjectValue(T obj, String propertyName, Object value) throws NoSuchFieldException, IllegalAccessException {
        Field fld = obj.getClass().getDeclaredField(propertyName);
        fld.setAccessible(true);
        fld.set(obj, value);
    }

    static public<T> Object getObjectValue(T obj, String propertyName) throws NoSuchFieldException, IllegalAccessException {

        Field fld = obj.getClass().getDeclaredField(propertyName);
        fld.setAccessible(true);
        Object retObj = fld.get(obj);
        return retObj;
    }

}
