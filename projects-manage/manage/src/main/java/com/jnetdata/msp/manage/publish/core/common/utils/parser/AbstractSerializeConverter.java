package com.jnetdata.msp.manage.publish.core.common.utils.parser;

import com.google.common.base.Throwables;
import org.apache.commons.lang3.reflect.FieldUtils;

import java.lang.reflect.Field;

/**
 * Created by xujian on 2017/6/13.
 */
public abstract class AbstractSerializeConverter implements SerializeConverter {
    /**
     * {@inheritDoc}
     */
    @Override
    public String toString(final Field field,
                           final Object target) {
        return getFieldValue(field, target).toString();
    }

    protected final Object getFieldValue(final Field field,
                                         final Object target) {
        try {
            return FieldUtils.readField(field, target, true);
        } catch (IllegalAccessException e) {
            throw Throwables.propagate(e);
        }
    }
}
