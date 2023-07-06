package com.jnetdata.msp.member.user.util;

public enum ThirdPartyType {
    WX("微信"), WB("微博"), QQ("qq");

    private String type;

    private ThirdPartyType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return String.valueOf(this.type);
    }

    public String getValue(){
        return type;
    }
}
