package com.jnetdata.msp.manage.publish.core.common.utils.parser;

import com.thoughtworks.xstream.converters.basic.BigDecimalConverter;

import java.math.BigDecimal;

/**
 * Created by xujian on 2017/6/19.
 */
public class MyBigDecimalConverter extends BigDecimalConverter {

    @Override
    public String toString(Object obj) {
        return obj == null ? null : new BigDecimal(obj.toString()).setScale(2, BigDecimal.ROUND_HALF_UP).stripTrailingZeros().toPlainString();
    }
}
