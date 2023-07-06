package com.jnetdata.msp.visual.enums;

/**
 * 逻辑意义上的是和否
 */
public enum Logic {
    YES("1"),
    NO("0")
    ;

    private String code;

    Logic(String code){
        this.code = code;
    }

    public String getCode() {
        return code;
    }}
