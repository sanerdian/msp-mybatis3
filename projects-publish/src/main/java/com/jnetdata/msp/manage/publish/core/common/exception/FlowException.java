package com.jnetdata.msp.manage.publish.core.common.exception;

public class FlowException extends BaseException {
    /**
     * 指定一个是否追踪栈信息的异常
     *
     * @param msg              异常消息
     * @param recordStackTrace 是否追踪栈信息
     */
    public FlowException(String msg, boolean recordStackTrace) {
        super(msg, null, false, recordStackTrace);
    }

    /**
     * 指定一个不追踪栈信息的异常
     *
     * @param msg 异常消息
     */
    public FlowException(String msg) {
        this(msg, false);
    }

    /**
     * 指定一个带有自定义消息的包装异常(会生追踪栈信息)
     *
     * @param msg   异常消息
     * @param cause 要包装的异常信息
     */
    public FlowException(String msg, Throwable cause) {
        super(msg, cause, false, true);
    }

    /**
     * 指定一个包装异常(会生追踪栈信息)
     *
     * @param cause 要包装的信息
     */
    public FlowException(Throwable cause) {
        super(cause.getMessage(), cause, false, true);
    }
}
