package com.jnetdata.msp.visual.enums.login;

/**
 * 第三方登录
 */
public enum ThirdLogin {
    WEIXIN("1", "微信登录"),
    QQ("2", "QQ登录"),
    ;

    private String code;
    private String name;

    ThirdLogin(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return this.code;
    }

    public String getName() {
        return this.name;
    }
}
