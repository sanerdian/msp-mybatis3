package com.jnetdata.msp.manage.publish.core.common.utils.parser;

import com.thoughtworks.xstream.converters.SingleValueConverter;

import java.lang.reflect.Field;

/**
 * Created by xujian on 2017/6/13.
 */
public class WrapConverter extends AbstractSerializeConverter {
    private final SingleValueConverter converter;

    public WrapConverter(final SingleValueConverter converter) {
        this.converter = converter;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean canConvert(final Field field) {
        return converter.canConvert(field.getType());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString(final Field field, final Object target) {
        return converter.toString(getFieldValue(field, target));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object fromString(final Field field,
                             final String str) {
        return converter.fromString(str);
    }
}
