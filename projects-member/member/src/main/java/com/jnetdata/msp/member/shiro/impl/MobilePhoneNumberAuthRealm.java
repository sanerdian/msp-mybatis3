package com.jnetdata.msp.member.shiro.impl;

//import com.jnetdata.store.member.user.model.User;
//import com.jnetdata.store.member.user.service.UserService;

import com.jnetdata.msp.member.user.model.User;
import com.jnetdata.msp.member.user.service.UserService;
import lombok.val;
import org.apache.shiro.authc.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;

/**
 * @author Administrator
 */
public class MobilePhoneNumberAuthRealm extends AuthRealm {

    @Lazy @Autowired
    private UserService userService;

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authcToken) throws AuthenticationException {
        UsernamePasswordToken token = (UsernamePasswordToken)authcToken;
        User user = userService.getByCellPhone((String)token.getPrincipal());
        if (null == user) {
            throw new UnknownAccountException();
        }
//        if(Boolean.TRUE.equals(user.getLocked())) {
//            throw new LockedAccountException();
//        }
        val cloneUser = cloneToCache(user);
        return new SimpleAuthenticationInfo(
                cloneUser,
                "OK",
                getName());
    }

}
