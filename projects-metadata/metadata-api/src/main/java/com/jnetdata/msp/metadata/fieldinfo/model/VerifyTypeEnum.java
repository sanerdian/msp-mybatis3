package com.jnetdata.msp.metadata.fieldinfo.model;

/**
 * @author Administrator
 */

public enum VerifyTypeEnum {

    NotEmpty("非空", 1, true),
    Number("数字",2, true),
    EnglishLetter("字母", 3, true),
    UserName("用户名", 4, true),
    Password("密码", 5, true),
    Phone("手机号", 6, false),
    Email("邮箱", 7, false),
    IdCard("身份证", 8, false),
    BankCard("银行卡号", 9, false),
    CarNum("车牌号", 10, false),
    Chinese("中文", 11, true),
    Regula("正则", 90, false),
    ;

    private final String name;
    private final Integer value;
    private final Boolean hasLength;

    VerifyTypeEnum(String name, Integer value, Boolean hasLength) {
        this.name = name;
        this.value = value;
        this.hasLength = hasLength;
    }

    public String getName() {
        return name;
    }

    public Integer getValue() {
        return value;
    }

    public Boolean getHasLength() {
        return hasLength;
    }
}
