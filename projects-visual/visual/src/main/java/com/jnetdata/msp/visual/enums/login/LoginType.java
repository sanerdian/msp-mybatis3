package com.jnetdata.msp.visual.enums.login;

/**
 * 登录类型
 */
public enum LoginType {

    ACCOUNT("1", "账号密码", "visual_login_userName"),
    PHONE("2", "手机验证码", "visual_login_phone"),
    APP("3", "APP扫码", "visual_login_code"),

    ;

    private String code;
    private String name;
    private String label;//标签标识，用于隐藏没有选择的登录类型对应的标签

    LoginType(String code, String name, String label){
        this.code = code;
        this.name = name;
        this.label = label;
    }

    public String getCode(){
        return this.code;
    }
    public String getName(){
        return this.name;
    }
    public String getLabel(){
        return this.label;
    }
}
