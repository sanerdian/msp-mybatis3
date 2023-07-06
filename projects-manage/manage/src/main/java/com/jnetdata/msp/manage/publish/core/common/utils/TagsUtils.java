package com.jnetdata.msp.manage.publish.core.common.utils;

import com.jnetdata.msp.constants.PublishConstants;
import com.jnetdata.msp.core.model.PublishObj;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 */
public class TagsUtils {
    /**
     * 格式化时间
     *
     * @param date
     * @param pattern
     * @return
     */
    public static String getDate(final Date date, final String pattern) {
        SimpleDateFormat dft = new SimpleDateFormat(pattern);
        return dft.format(date);
    }

    public static Map<String, Object> buildMapObj(PublishObj publishObj) {
        Map<String, Object> attr = new HashMap<>();
        attr.put(PublishConstants.PUBLISH_OBJ_STR, publishObj);
        return attr;
    }


}
