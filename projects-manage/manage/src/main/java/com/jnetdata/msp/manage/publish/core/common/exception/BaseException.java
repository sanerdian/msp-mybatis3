package com.jnetdata.msp.manage.publish.core.common.exception;


/**
 * The type Base exception.
 *
 * @description 基础异常
 * @date 2018 -11-13 10:15
 */
public class BaseException extends RuntimeException {

    /**
     * 基础异常
     *
     * @param msg the msg
     * @param e   the e
     */
    public BaseException(String msg, Throwable e) {
        super(msg, e);
    }

    private BaseException() {
        super();
    }

    private BaseException(String message) {
        super(message);
    }

    private BaseException(Throwable cause) {
        super(cause);
    }

    /**
     * 基础异常
     *
     * @param message            消息
     * @param cause              导致
     * @param enableSuppression  使抑制
     * @param writableStackTrace 可写的堆栈跟踪
     */
    protected BaseException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
