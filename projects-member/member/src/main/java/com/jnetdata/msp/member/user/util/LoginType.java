package com.jnetdata.msp.member.user.util;

import lombok.Data;

public enum LoginType {
    SYS_USERNAME_PHONE(0), USERNAME_PHONE(1), USERNAME(2), PHONE(3), EMAIL(4),IT_USERNAME_PHONE(5),PASS_USERNAME_PHONE(6), WX(10), WB(11), QQ(12), APPLE(13);

    private int loginType;

    private LoginType(int type) {
        this.loginType = type;
    }

    @Override
    public String toString() {
        return String.valueOf(this.loginType);
    }

    public int getValue(){
        return loginType;
    }
}
