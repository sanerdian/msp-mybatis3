package com.jnetdata.msp.flowable.enums;

public enum NewsStatus {
    NEW("0", "新稿"),
    EDIT("1", "待编"),
    AUDIT("2", "待审"),
    SIGN("3", "待签"),
    CANCELED("19", "已撤销"),
    TO_ISSUE("20", "待发布"),
    ISSUE("21", "已发布"),
    ;

    NewsStatus(String code, String name){
        this.code = code;
        this.name = name;
    }
    private String code;
    private String name;

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }
}
