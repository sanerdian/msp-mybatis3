package com.jnetdata.msp.media.util;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.thenicesys.data.api.Pager;

public class ValueHelper {
    public static Integer intValue(Object object,Number defaultValue){
        if(object!=null&&object instanceof Number){
            return ((Number) object).intValue();
        }else if(defaultValue!=null){
            return defaultValue.intValue();
        }else {
            return null;
        }
    }
    public static Long longValue(Object object,Number defaultValue){
        if(object!=null&&object instanceof Number){
            return ((Number) object).longValue();
        }else if(defaultValue!=null){
            return defaultValue.longValue();
        }else {
            return null;
        }
    }
    public static String stringValue(Object object,String defaultValue){
        if(object!=null){
            return String.valueOf(object);
        }else {
            return defaultValue;
        }
    }
    public static Pager changePage(Page page){
        Pager pager= new Pager();
        pager.setCurrent(intValue(page.getCurrent(),null));
        pager.setTotal(intValue(page.getTotal(),null));
        return pager;
    }
}
