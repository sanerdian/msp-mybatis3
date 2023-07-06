package com.jnetdata.msp.verifycodesaver.service.impl;

import com.jnetdata.msp.verifycodesaver.exception.VerifyCodeSaverException;
import com.jnetdata.msp.verifycodesaver.model.VerifyCode;
import com.jnetdata.msp.verifycodesaver.service.VerifyCodeSaverService;
import org.apache.shiro.session.Session;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

/**
 * @author Administrator
 */
@Service
public class VerifyCodeSaverServiceImpl implements VerifyCodeSaverService {

    @Override
    public void set(Session session, String key, Serializable value, Long expireTime, Integer maxGetTimes) {
        VerifyCode verifyCode = new VerifyCode(value, expireTime, ChronoUnit.MILLIS, maxGetTimes);
        session.setAttribute(key, verifyCode);
    }

    @Override
    public Object get(Session session, String key) {
        VerifyCode verifyCode = (VerifyCode)session.getAttribute(key);
        if (Objects.isNull(verifyCode)) {
            throw new VerifyCodeSaverException("验证码不存在");
        }
        try {
            validateLimit(verifyCode);
        }catch (VerifyCodeSaverException e) {
            session.removeAttribute(key);
            throw e;
        }
        session.setAttribute(key, verifyCode.incrementTotalGetTimes());
        return verifyCode.getContent();
    }

    private void validateLimit(VerifyCode verifyCode) {
        if (verifyCode.getExpireTime().isBefore(LocalDateTime.now())) {
            throw new VerifyCodeSaverException("验证码已过期");
        }
        if (verifyCode.getTotalGetTimes() >= verifyCode.getMaxGetTimes()) {
            throw  new VerifyCodeSaverException("超过验证码最大访问次数，请重新刷新验证码");
        }
    }

}
