package com.jnetdata.msp.limit;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.thenicesys.redis.RedisService;
import org.thenicesys.web.JsonResult;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

/**
 * @author Administrator
 */
@Slf4j
public class LimitRequestAspect {

    @Autowired
    private RedisService redisService;

    public LimitRequestAspect() {
        log.info("LimitRequestAspect()");
    }

    //@Pointcut("execution(* com.jnetdata..controller.*.*(..)) && @annotation(com.jnetdata.msp.limit.LimitRequest)")
    public void before(){
    }

    public Object requestLimit(ProceedingJoinPoint pjp) throws Throwable {

        try {
            validateLimitRequest(pjp);
        }catch (LimitRequestException e1) {
            return JsonResult.renderError(e1.getMessage());
        } catch (Exception e2) {
            return JsonResult.renderError("调用出错了");
        }

        Object result = pjp.proceed();
        return result;

    }

    private void validateLimitRequest(ProceedingJoinPoint pjp) throws Exception {

        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        // 判断request不能为空
        if (request == null) {
            throw new LimitRequestException("HttpServletRequest有误...");
        }

        LimitRequest limitRequest = getAnnotation(pjp);
        if (limitRequest == null) {
            return;
        }

        Object[] args = pjp.getArgs();
        if (args.length==0 || !(args[0] instanceof LimitRequestidentification)) {
            return;
        }
        String identification = ((LimitRequestidentification)(args[0])).identification();
        String uri = request.getRequestURI().toString();

        String waitKey = waitKey(identification, uri);
        if (redisService.hasKey(waitKey)) {
            log.error("用户[{}]访问地址[{}]太多频繁，请稍后重试", identification, uri);
            throw new LimitRequestException("[" + identification + "]访问太多频繁，请稍后重试");
        }

        String timesKey = operateTimesKey(identification, uri);
        // 设置在redis中的缓存，累加1
        Long count = redisService.increment(timesKey, 1L);
        // 如果该key不存在，则从0开始计算，并且当count为1的时候，设置过期时间
        if (count == 1L) {
            redisService.set(timesKey, count, limitRequest.time(), TimeUnit.SECONDS);
        }

        // 如果redis中的count大于限制的次数，则报错
        if (count > limitRequest.limitCounts()) {
            redisService.remove(timesKey);
            redisService.set(waitKey, 0L, limitRequest.waits(), TimeUnit.SECONDS);
            log.error("用户[{}]访问地址[{}]超过了限定的次数[{}]" + identification, uri, limitRequest.limitCounts());
            throw new LimitRequestException("[" + identification + "]访问太多频繁");
        }
    }

    private String operateTimesKey(String identification, String uri) {
        return "vendor:time-limit-request:" + uri + ":" + identification;
    }
    private String waitKey(String identification, String uri) {
        return "vendor:wait-limit-request:" + uri + ":" + identification;
    }

    private LimitRequest getAnnotation(JoinPoint joinPoint) throws Exception {
        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        Method method = methodSignature.getMethod();

        if (method != null) {
            return method.isAnnotationPresent(LimitRequest.class) ? method.getAnnotation(LimitRequest.class) : null;
        }
        return null;
    }

}
