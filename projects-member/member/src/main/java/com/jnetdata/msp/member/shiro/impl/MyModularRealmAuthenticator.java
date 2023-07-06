package com.jnetdata.msp.member.shiro.impl;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.pam.ModularRealmAuthenticator;
import org.apache.shiro.realm.Realm;

import java.util.Collection;

/**
 * @author Administrator
 */
public class MyModularRealmAuthenticator extends ModularRealmAuthenticator {

    @Override
    protected AuthenticationInfo doAuthenticate(AuthenticationToken authenticationToken)
            throws AuthenticationException {
        // 判断getRealms()是否返回为空
        assertRealmsConfigured();
        // 强制转换回自定义的CustomizedToken
        UserNamePasswordTelphoneToken token = (UserNamePasswordTelphoneToken) authenticationToken;
        // 所有Realm
        Collection<Realm> realms = getRealms();
        // 判断是单Realm还是多Realm
        return (realms.size() == 1) ? doSingleRealmAuthentication(realms.iterator().next(), token)
                : doMultiRealmAuthentication(realms, token);
    }

}
