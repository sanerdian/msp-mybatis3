package com.jnetdata.msp.manage.publish.core.common.utils.parser;

import com.google.common.base.Optional;

import java.lang.reflect.Field;

/**
 * Created by xujian on 2017/6/13.
 */
public interface ConvertorLookUp {
    /**
     * 寻找转换器
     *
     * @param filed
     * @return
     */
    Optional<SerializeConverter> lookUp(final Field filed);
}
