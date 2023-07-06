package com.jnetdata.msp.manage.publish.core.common.utils.parser;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableMap;
import com.thoughtworks.xstream.converters.basic.*;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.Map;

/**
 * Created by xujian on 2017/6/13.
 */
public class DefaultConvertorLookUp implements ConvertorLookUp {
    private final static Map<Class, SerializeConverter> CONVERTER_MAP;

    static {
        CONVERTER_MAP = ImmutableMap.<Class, SerializeConverter>builder()
                .put(Integer.class, new WrapConverter(new IntConverter()))
                .put(int.class, new WrapConverter(new IntConverter()))
                .put(Double.class, new WrapConverter(new DoubleConverter()))
                .put(double.class, new WrapConverter(new DoubleConverter()))
                .put(Float.class, new WrapConverter(new FloatConverter()))
                .put(float.class, new WrapConverter(new FloatConverter()))
                .put(Long.class, new WrapConverter(new LongConverter()))
                .put(long.class, new WrapConverter(new LongConverter()))
                .put(BigDecimal.class, new WrapConverter(new MyBigDecimalConverter()))
                .put(String.class, new WrapConverter(new StringConverter()))
                .put(boolean.class, new WrapConverter(new BooleanConverter()))
                .put(Boolean.class, new WrapConverter(new BooleanConverter()))
                .build();
    }

    /**
     * {@inheritDoc}
     *
     * @param filed
     * @return
     */
    @Override
    public Optional<SerializeConverter> lookUp(final Field filed) {
        return Optional.fromNullable(CONVERTER_MAP.get(filed.getType()));
    }
}
