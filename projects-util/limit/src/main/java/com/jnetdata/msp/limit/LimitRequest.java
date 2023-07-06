package com.jnetdata.msp.limit;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
@Order(Ordered.HIGHEST_PRECEDENCE)        // 设置顺序为最高优先级
public @interface LimitRequest {

    /**
     * 时长(秒)
     * @return
     */
    long time() default 60L;

    /**
     * 访问次数
     * @return
     */
    long limitCounts() default  5L;

    /**
     * 限制等待时长(秒)
     * @return
     */
    long waits() default 300L;

}
