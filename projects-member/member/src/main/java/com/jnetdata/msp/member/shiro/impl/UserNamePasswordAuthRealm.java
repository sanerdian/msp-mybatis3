package com.jnetdata.msp.member.shiro.impl;


import com.jnetdata.msp.member.user.model.User;
import com.jnetdata.msp.member.user.service.UserService;
import com.jnetdata.msp.member.user.util.LoginType;
import lombok.val;
import org.apache.shiro.authc.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;

import java.util.Date;

/**
 * @author Administrator
 */
public class UserNamePasswordAuthRealm extends AuthRealm {

    @Lazy @Autowired
    private UserService userService;
    @Value("${user-login-lock-times}")
    private int locktimes;


    /**
     * 登录
     * @param authcToken
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authcToken) throws AuthenticationException {
        UserNamePasswordTelphoneToken token = (UserNamePasswordTelphoneToken)authcToken;
        int loginType = token.getLoginType();
        SimpleAuthenticationInfo simpleAuthenticationInfo= null;
        switch (loginType){
            case 0:simpleAuthenticationInfo = sysUserNamePhoneLogin(token);break;
            case 1:simpleAuthenticationInfo = userNamePhoneLogin(token);break;
            case 2:simpleAuthenticationInfo = userNameLogin(token);break;
            case 3:simpleAuthenticationInfo = userPhoneLogin(token);break;
            case 4:simpleAuthenticationInfo = userEmailLogin(token);break;
            case 5:simpleAuthenticationInfo = itUserNamePhoneLogin(token);break;
            case 6:simpleAuthenticationInfo = qyUserNamePhoneLogin(token);break;
            case 7:simpleAuthenticationInfo = passUserNamePhoneLogin(token);break;
            case 10:simpleAuthenticationInfo = thirdPartyLogin(token,LoginType.WX);break;
            case 11:simpleAuthenticationInfo = thirdPartyLogin(token,LoginType.WB);break;
            case 12:simpleAuthenticationInfo = thirdPartyLogin(token,LoginType.QQ);break;
            case 13:simpleAuthenticationInfo = thirdPartyLogin(token,LoginType.APPLE);break;
            default:
                throw new IllegalStateException("Unexpected value: " + loginType);
        }
        return simpleAuthenticationInfo;
    }

    private SimpleAuthenticationInfo sysUserNamePhoneLogin(UserNamePasswordTelphoneToken token) {
        return checkUserByNamePhone(token,0);
    }

    private SimpleAuthenticationInfo itUserNamePhoneLogin(UserNamePasswordTelphoneToken token) {
        return checkUserByNamePhone(token,3);
    }

    private SimpleAuthenticationInfo qyUserNamePhoneLogin(UserNamePasswordTelphoneToken token) {
        return checkUserByNamePhone(token,21);
    }

    private SimpleAuthenticationInfo passUserNamePhoneLogin(UserNamePasswordTelphoneToken token) {
        return checkUserByNamePhone(token,4);
    }

    private SimpleAuthenticationInfo thirdPartyLogin(UserNamePasswordTelphoneToken token,LoginType type) {
        User user = userService.getByOpenId(type.name(),token.getUsername());
        return checkUser(user);
    }

    private SimpleAuthenticationInfo userEmailLogin(UserNamePasswordTelphoneToken token) {
        User user = userService.getByEmail((String)token.getPrincipal());
        return checkUser(user);
    }

    private SimpleAuthenticationInfo userPhoneLogin(UserNamePasswordTelphoneToken token) {
        User user = userService.getByCellPhone((String)token.getPrincipal());
        return checkUser(user);
    }

    private SimpleAuthenticationInfo userNameLogin(UserNamePasswordTelphoneToken token) {
        User user = userService.getByName((String)token.getPrincipal());
        return checkUser(user);
    }

    private SimpleAuthenticationInfo userNamePhoneLogin(UserNamePasswordTelphoneToken token) {
        return checkUserByNamePhone(token,-1);
    }

    private SimpleAuthenticationInfo checkUser(User user){
        if (null==user) {
            throw new UnknownAccountException();
        }
        if(locktimes>=0 && (locktimes <= user.getLocktimes()||user.getStatus() == 2)) throw new LockedAccountException();

        if(user.getStopState() == 1) {
            throw new LockedAccountException("用户被停用!");
        }
        if(user.getPowerState()==2){
            throw new LockedAccountException("权限到期"); //权限到期
        }
        if(user.getPowerState()==1) {
            if (user.getPowerDate().before(new Date())) {
                throw new LockedAccountException("权限到期"); //权限到期
            }
        }
        val cloneUser = cloneToCache(user);
        return new SimpleAuthenticationInfo(
                cloneUser,
                user.getMdPassWord(),
                getName());
    }

    private SimpleAuthenticationInfo checkUserByNamePhone(UserNamePasswordTelphoneToken token,int sign){
        User user = userService.getByName((String)token.getPrincipal());
        if (null == user) {
            user = userService.getByCellPhone((String)token.getPrincipal());
        }
        if (null==user) {
            throw new UnknownAccountException();
        }
        if(sign>=0 && user.getSign() != sign){
            throw new UnknownAccountException();
        }
        return checkUser(user);
    }

}
