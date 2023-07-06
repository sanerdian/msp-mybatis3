package com.jnetdata.msp.manage.publish.core.common.utils;


import com.jnetdata.msp.manage.publish.core.common.exception.FlowException;
import org.springframework.lang.Nullable;

public class AssertUtil {

    /**
     * 非空
     *
     * @param object  对象
     * @param message 消息
     */
    public static void notNull(@Nullable Object object, String message) {
        if (object == null) {
            throw new FlowException(message);
        }
    }

    /**
     * 是真的
     *
     * @param expression 表达式
     * @param message    消息
     */
    public static void isTrue(boolean expression, String message) {
        if (!expression) {
            throw new FlowException(message);
        }
    }

    /**
     * 是真的
     *
     * @param message 消息
     */
    public static void isTrue(String message) {
        throw new FlowException(message);
    }

    public static void exception(String message) {
            throw new FlowException(message);
    }
}
