package com.jnetdata.msp.visual.enums.login;

/**
 * 登录跳转
 */
public enum LoginSkip {

    INNER_LINK("1", "站内链接"),
    OUTER_LINK("2", "站外链接")

    ;

    private String code;
    private String name;

    LoginSkip(String code, String name){
        this.code = code;
        this.name = name;
    }

    public String getCode(){
        return this.code;
    }
    public String getName(){
        return this.name;
    }
}
