package com.jnetdata.msp.visual.enums.login;

/**
 * 验证方式
 */
public enum VerifyMode {

    GRAPH("1", "图形验证码"),
    SLIDE("2", "滑动拼图验证码"),
    WORD("3", "文字点选验证码"),
    ;

    private String code;
    private String name;

    VerifyMode(String code, String name){
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
