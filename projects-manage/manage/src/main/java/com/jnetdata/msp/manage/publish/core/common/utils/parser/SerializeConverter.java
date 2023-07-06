package com.jnetdata.msp.manage.publish.core.common.utils.parser;

import java.lang.reflect.Field;

/**
 * Created by xujian on 2017/6/13.
 */
public interface SerializeConverter {
    /**
     * 是否可以转换
     *
     * @param field
     * @return
     */
    boolean canConvert(final Field field);

    /**
     * 将target的field值转换成string
     *
     * @param field
     * @param target
     * @return
     */
    String toString(final Field field,
                    final Object target);

    /**
     * 将string转换成target的field值
     *
     * @param field
     * @param str
     * @return
     */
    Object fromString(final Field field,
                      final String str);
}
