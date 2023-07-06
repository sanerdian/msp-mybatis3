package com.jnetdata.msp.verifycodesaver.model;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

/**
 * 验证码
 * @author Administrator
 */
@Data
public class VerifyCode implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 验证码内容
     */
    private Serializable content;

    /**
     * 过期时间
     */
    private LocalDateTime expireTime;

    /**
     * 最多访问次数
     */
    private Integer maxGetTimes;

    /**
     * 统计获取次数
     */
    private Integer totalGetTimes;

    public VerifyCode() {

    }

    public VerifyCode(Serializable content, Long expire, ChronoUnit timeUnit, Integer maxGetTimes) {
        this.content = content;
        LocalDateTime dateTime = LocalDateTime.now();
        this.expireTime= dateTime.plus(expire, timeUnit);
        this.maxGetTimes = maxGetTimes;
        this.totalGetTimes = 0;
    }

    public VerifyCode incrementTotalGetTimes() {
        this.totalGetTimes++;
        return this;
    }

}
