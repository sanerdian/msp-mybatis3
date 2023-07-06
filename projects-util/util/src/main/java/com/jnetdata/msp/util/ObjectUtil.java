package com.jnetdata.msp.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author Administrator
 */
public abstract class ObjectUtil {

    public static boolean isNotEmpty(String str) {
        return null!=str && str.length()>0 && str.trim().length()>0;
    }

    public static boolean isEmpty(String str) {
        return null==str || str.length()==0 || str.trim().length()==0;
    }

    public static List<String> splitStrs(String str) {
        if (Objects.isNull(str) || "".equals(str) || "".equals(str.trim())) {
            return new ArrayList<>();
        }
        String[] splits = str.replaceAll(" ","").split(",|，");
        List<String> list = new ArrayList<>();
        for(String split : splits) {
            list.add(split);
        }
        return list;
    }

}
