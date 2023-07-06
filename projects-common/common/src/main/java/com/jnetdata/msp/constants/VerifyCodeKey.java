package com.jnetdata.msp.constants;

public class VerifyCodeKey {

    private  static final String TEST_VALIDATECODE_PREFIX = "testValidateCode.";

    public static String generateValidateSessionKeyByCellPhone(String phonenumber) {
        return TEST_VALIDATECODE_PREFIX+phonenumber;
    }
    public static String generateValidateSessionKeyByRequestSessionId(String requestSessionId) {
        return TEST_VALIDATECODE_PREFIX+requestSessionId;
    }

}
