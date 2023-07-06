package com.jnetdata.msp.member.shiro.impl;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.mgt.RealmSecurityManager;

/**
 * 清除身份验证
 */
public class ShiroUtil {
    public static void clearAuth() {
        RealmSecurityManager rsm = (RealmSecurityManager) SecurityUtils.getSecurityManager();
        AuthRealm realm = (AuthRealm) rsm.getRealms().iterator().next();
        realm.clearAuthz();
    }
}
