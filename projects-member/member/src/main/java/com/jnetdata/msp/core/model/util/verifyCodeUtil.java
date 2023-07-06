package com.jnetdata.msp.core.model.util;

/**
 * @author Administrator
 */
public class verifyCodeUtil {

    private  static final String TEST_VALIDATECODE_PREFIX = "testValidateCode.";

    public static String generateValidateSessionKey(String phonenumber) {
        return TEST_VALIDATECODE_PREFIX+phonenumber;
    }


}
