package com.jnetdata.msp.member.shiro.impl;

import com.jnetdata.msp.member.user.util.LoginType;
import lombok.Data;
import org.apache.shiro.authc.UsernamePasswordToken;

import java.io.Serializable;

@Data
public class UserNamePasswordTelphoneToken extends UsernamePasswordToken implements Serializable {

    private String phonenumber;
    private int loginType;

    public UserNamePasswordTelphoneToken(String username, int loginType, String password, boolean rememberMe) {
        super(username, password, rememberMe);
        this.loginType = loginType;
    }

    public UserNamePasswordTelphoneToken(String username, String password, boolean rememberMe) {
        super(username, password, rememberMe);
    }

    public UserNamePasswordTelphoneToken(String phonenumber, boolean rememberMe) {
        this.phonenumber = phonenumber;
        setRememberMe(rememberMe);
    }

    @Override
    public Object getPrincipal() {
        return isEmpty(this.phonenumber) ? getUsername() : getPhonenumber();
    }

    @Override
    public Object getCredentials() {
        return isEmpty(this.phonenumber) ? getPassword() : "OK";
    }

    private boolean isEmpty(String str) {
        return null==str || "".equals(str.trim());
    }
}
