package com.jnetdata.msp.verifycodesaver.service;

import com.jnetdata.msp.verifycodesaver.exception.VerifyCodeSaverException;
import lombok.SneakyThrows;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.mgt.SessionsSecurityManager;
import org.apache.shiro.session.Session;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Collection;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({
        "classpath:spring/springcontext-service.xml",
        "classpath:spring/springcontext-shiro.xml"
})
public class VerifyCodeSaverServiceTest {

    @Autowired
    private SessionsSecurityManager securityManager;
    @Autowired
    private VerifyCodeSaverService verifyCodeSaverService;
    private Session session;
    @Before
    public void setUpVerifyCodeSaverServiceTest() {
        SecurityUtils.setSecurityManager(securityManager);
        session = SecurityUtils.getSubject().getSession();
    }

    @Test
    public void test_1() {
        String key = "key1";
        String value = "15";
        verifyCodeSaverService.set(session, key, value, 10L, 2);
        Object obj = verifyCodeSaverService.get(session, key);
        Assert.assertTrue(value.equals(obj));

        Collection<Object> attributeKeys = session.getAttributeKeys();
        Assert.assertTrue(attributeKeys.contains(key));
    }

    @Test
    public void test_2() {
        String key = "key1";
        String value = "15";
        verifyCodeSaverService.set(session, key, value, 10L, 2);
        Object obj1 = verifyCodeSaverService.get(session, key);
        Assert.assertTrue(value.equals(obj1));
        Object obj2 = verifyCodeSaverService.get(session, key);
        Assert.assertTrue(value.equals(obj2));

        Collection<Object> attributeKeys = session.getAttributeKeys();
        Assert.assertTrue(attributeKeys.contains(key));
    }

    @Test(expected = VerifyCodeSaverException.class)
    public void test_3_exception_1() {
        String key = "key1";
        String value = "15";
        verifyCodeSaverService.set(session, key, value, 10L, 2);
        Object obj1 = verifyCodeSaverService.get(session, key);
        Object obj2 = verifyCodeSaverService.get(session, key);
        try {
            Object obj3 = verifyCodeSaverService.get(session, key);
            Assert.fail("不能到这里");
        }catch (VerifyCodeSaverException e) {
            Assert.assertTrue(e.getMessage().contains("超过验证码最大访问次数"));
            Collection<Object> attributeKeys = session.getAttributeKeys();
            Assert.assertTrue(!attributeKeys.contains(key));
            throw e;
        }
    }

    @Test(expected = VerifyCodeSaverException.class)
    @SneakyThrows
    public void test_3_exception_2() {
        String key = "key1";
        String value = "15";
        verifyCodeSaverService.set(session, key, value, 2L, 2);
        Thread.sleep(1000*5L);
        try {
            Object obj1 = verifyCodeSaverService.get(session, key);
            Assert.fail("不能到这里");
        }catch (VerifyCodeSaverException e) {
            Assert.assertTrue(e.getMessage().contains("验证码已过期"));
            Collection<Object> attributeKeys = session.getAttributeKeys();
            Assert.assertTrue(!attributeKeys.contains(key));
            throw e;
        }
    }


    @Test(expected = VerifyCodeSaverException.class)
    @SneakyThrows
    public void test_3_exception_3() {
        String key = "key1";
        String value = "15";
        verifyCodeSaverService.set(session, key, value, 2L, 2);
        try {
            Object obj1 = verifyCodeSaverService.get(session, key+"_1");
            Assert.fail("不能到这里");
        }catch (VerifyCodeSaverException e) {
            Assert.assertTrue(e.getMessage().contains("验证码不存在"));
            throw e;
        }
    }


}
